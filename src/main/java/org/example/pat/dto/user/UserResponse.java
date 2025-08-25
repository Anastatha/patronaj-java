package org.example.pat.dto.user;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.example.pat.dto.curator.CuratorResponse;
import org.example.pat.entity.User;
import org.example.pat.entity.consts.Categorie;
import org.example.pat.entity.consts.Label;
import org.example.pat.entity.consts.Status;

import java.util.List;

public record UserResponse(
        Long id,
        String userTelegramId,
        String email,
        String name,
        String phone,
        String patronymic,
        String surname,
        String organizationName,
        String inn,
        Status status,
        Categorie category,
        Long curatorId,
        List<Label> label,
        String okved,
        CuratorResponse curator
) {
    public static UserResponse fromEntity(User user) {
        return new UserResponse(
                user.getId(),
                user.getUserTelegramId(),
                user.getEmail(),
                user.getName(),
                user.getPhone(),
                user.getPatronymic(),
                user.getSurname(),
                user.getOrganizationName(),
                user.getInn(),
                user.getStatus(),
                user.getCategory(),
                user.getCuratorId(),
                user.getLabel(),
                user.getOkved(),
                user.getCurator() != null
                        ? new CuratorResponse(
                        user.getCurator().getId(),
                        user.getCurator().getName(),
                        user.getCurator().getSurname(),
                        user.getCurator().getEmail(),
                        user.getCurator().getPhone(),
                        user.getCurator().getPatronymic(),
                        user.getCurator().getRole()
                )
                        : null
        );
    }
}
