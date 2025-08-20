package org.example.pat.dto.curator;

import jakarta.validation.constraints.Email;
import org.example.pat.entity.consts.Roles;

public record CreateCuratorInput(
        @Email
        String email,
        String password,
        String name,
        String patronymic,
        String surname,
        String phone,
        Roles role
) {
}
