package org.example.pat.dto.curator;

import jakarta.validation.constraints.Email;
import org.springframework.lang.Nullable;
import org.example.pat.entity.consts.Roles;

public record UpdateCuratorInput(
        @Nullable @Email String email,
        @Nullable String name,
        @Nullable String patronymic,
        @Nullable String phone,
        @Nullable Roles role
) {
}
