package org.example.pat.service;

import org.example.pat.dto.user.CreateUserInput;
import org.example.pat.dto.user.UserResponse;
import org.example.pat.entity.Curator;
import org.example.pat.entity.User;
import org.example.pat.exception.*;
import org.example.pat.repository.CuratorRepository;
import org.example.pat.repository.UserRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {
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
    public UserResponse createUser(Long curatorId, CreateUserInput createUserInput) {
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

            mailService.sendMail(user, curator);

            return UserResponse.fromEntity(createdUser);

        } catch (DataIntegrityViolationException e) {
            throw new UserAlreadyExistsException();
        }
    }

}