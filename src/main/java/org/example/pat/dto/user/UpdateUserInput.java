package org.example.pat.dto.user;

import jakarta.validation.constraints.Email;
import org.example.pat.entity.Curator;
import org.example.pat.entity.consts.Categorie;
import org.example.pat.entity.consts.Label;
import org.example.pat.entity.consts.Status;
import org.springframework.lang.Nullable;

import java.time.LocalDateTime;
import java.util.List;

public record UpdateUserInput(
        @Nullable String userTelegramId,
        @Nullable String name,
        @Nullable String surname,
        @Nullable String patronymic,
        @Nullable @Email String email,
        @Nullable String organizationName,
        @Nullable String inn,
        @Nullable String phone,
        @Nullable Status status,
        @Nullable LocalDateTime deletedAt,
        @Nullable Categorie category,
        @Nullable Long curatorId,
        @Nullable List<Label>label,
        @Nullable String okved,
        @Nullable Curator curator
)
{
}
