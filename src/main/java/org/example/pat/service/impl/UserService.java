package org.example.pat.service.impl;

import io.micrometer.common.lang.Nullable;
import org.example.pat.dto.user.CreateUserInput;
import org.example.pat.dto.user.UpdateUserInput;
import org.example.pat.entity.Curator;
import org.example.pat.entity.User;
import org.example.pat.entity.consts.Label;
import org.example.pat.exception.*;
import org.example.pat.infrastructure.DadataService;
import org.example.pat.infrastructure.MailService;
import org.example.pat.infrastructure.RedisService;
import org.example.pat.repository.CuratorRepository;
import org.example.pat.repository.UserRepository;
import org.example.pat.service.IUserService;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserService implements IUserService {
    private final UserRepository userRepository;
    private final CuratorRepository curatorRepository;
    private final DadataService dadataService;
    private final RedisService redisService;
    private final MailService mailService;

    public UserService(UserRepository userRepository,
                       CuratorRepository curatorRepository,
                       DadataService dadataService,
                       RedisService redisService,
                       MailService mailService) {
        this.userRepository = userRepository;
        this.curatorRepository = curatorRepository;
        this.dadataService = dadataService;
        this.redisService = redisService;
        this.mailService = mailService;
    }

    @Transactional
    public User createUser(Long curatorId, CreateUserInput createUserInput) {
        try {
            Curator curator = curatorRepository.getCurator(curatorId);

            User user = new User(createUserInput);
            user.setCuratorId(curatorId);
            user.setStatusDisabled();
            user.setCode();

            // Получаем ОКВЭД если нужно
            if (user.isOkvedCategory()) {
                String okved = dadataService.getOkved(user.getInn());
                String okvedName = dadataService.getOkvedName(okved);
                System.out.println("OKVED???: " + okved);
                user.setOkved(okved);
                // Сохраняем в Redis
                if (!redisService.hashHasKey("okveds", okvedName)) {
                    redisService.setHashValue("okveds", okvedName, okved);
                }
            }

            User createdUser = userRepository.create(user);

//            mailService.sendMail(user, curator);

            return createdUser;

        } catch (DataIntegrityViolationException e) {
            throw new UserAlreadyExistsException();
        }
    }

    public User getUser(Long id) {
        try {
            return userRepository.getUser(id);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw new NotFoundException("Пользователь с id = " + id + " не найден");
        }
    }

    public List<User> getAllUsers() {
        try {
            return userRepository.getUsers();
        } catch (Exception e) {
            throw new RuntimeException("Ошибка при получении списка пользователей: " + e.getMessage(), e);
        }
    }

    public User softDeleteUser(Long id) {
        try {
            return userRepository.softDelete(id);
        } catch (Exception e) {
            throw new NotFoundException("Не удалось удалить пользователя с id = " + id);
        }
    }

    public List<User> getUsersByCuratorId(Long curatorId) {
        try {
            return userRepository.getUsersByCuratorId(curatorId);
        } catch (Exception e) {
            throw new NotFoundException("Пользователи куратора с id = " + curatorId + " не найдены");
        }
    }

    public List<User> getUsersByLabel(List<Label> labels) {
        try {
            return userRepository.getUsersByLabel(labels);
        } catch (Exception e) {
            throw new RuntimeException("Ошибка при поиске пользователей по labels: " + e.getMessage(), e);
        }
    }

    public List<User> getUsersByOkved(String okved) {
        try {
            return userRepository.getUsersByOkved(okved);
        } catch (Exception e) {
            throw new RuntimeException("Ошибка при поиске пользователей по ОКВЭД: " + e.getMessage(), e);
        }
    }

    public List<User> getUsersByLabelOrOkved(@Nullable List<String> okved, @Nullable List<Label> labels) {
        try {
            return userRepository.getUsersLabelOrOkved(okved, labels);
        } catch (Exception e) {
            throw new RuntimeException("Ошибка при поиске пользователей по labels или ОКВЭД: " + e.getMessage(), e);
        }
    }

    public User updateUser(Long id, UpdateUserInput input) {
        getUser(id);
        try {
            return userRepository.update(id, input);
        } catch (Exception e) {
            throw new RuntimeException("Ошибка при обновлении пользователя: " + e.getMessage(), e);
        }
    }

    public List<User> getUsersBetween(@Nullable String from, @Nullable String to) {
        try {
            return userRepository.getUsersBetween(from, to);
        } catch (Exception e) {
            throw new RuntimeException("Ошибка при поиске пользователей по дате обновления: " + e.getMessage(), e);
        }
    }
}