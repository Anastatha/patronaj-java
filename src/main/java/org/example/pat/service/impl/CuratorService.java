package org.example.pat.service.impl;

import org.example.pat.dto.curator.CreateCuratorInput;
import org.example.pat.dto.curator.UpdateCuratorInput;
import org.example.pat.entity.Curator;
import org.example.pat.exception.NotFoundException;
import org.example.pat.repository.CuratorRepository;
import org.example.pat.service.ICuratorService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CuratorService implements ICuratorService {
    private final CuratorRepository curatorRepository;
    private final PasswordEncoder passwordEncoder;

    public CuratorService(CuratorRepository curatorRepository, PasswordEncoder passwordEncoder) {
        this.curatorRepository = curatorRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Curator createCurator(CreateCuratorInput input) {
        try {
            Curator curator = new Curator();
            curator.setPhone(input.phone());
            curator.setPassword(passwordEncoder.encode(input.password()));
            curator.setName(input.name());
            curator.setPatronymic(input.patronymic());
            curator.setSurname(input.surname());
            curator.setEmail(input.email());
            curator.setRole(input.role());

            return curatorRepository.create(curator);
        } catch (Exception e) {
            throw new RuntimeException("Ошибка при создании куратора: " + e.getMessage(), e);
        }
    }

    public Curator getCurator(Long id) {
        try {
            return curatorRepository.getCurator(id);
        } catch (Exception e) {
            throw new NotFoundException("Куратор с id = " + id + " не найден");
        }
    }

    public Curator updateCurator(Long id, UpdateCuratorInput input) {
        getCurator(id);
        try {
            return curatorRepository.update(id, input);
        } catch (Exception e) {
            throw new RuntimeException("Ошибка при обновлении куратора: " + e.getMessage(), e);
        }
    }

    public Curator softDeleteCurator(Long id) {
        try {
            return curatorRepository.softDelete(id);
        } catch (RuntimeException e) {
            throw new NotFoundException("Не удалось удалить куратора с id = " + id);
        }
    }

    public List<Curator> getAllCurator() {
        try {
            return curatorRepository.getCurators();
        } catch (Exception e) {
            throw new RuntimeException("Ошибка при получении списка кураторов: " + e.getMessage(), e);
        }
    }

    public Curator getCuratorByEmail(String email) {
        try {
            return curatorRepository.getCuratorByEmail(email);
        } catch (RuntimeException e) {
            throw new NotFoundException("Куратор с email = " + email + " не найден");
        }
    }
}
