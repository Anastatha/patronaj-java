package org.example.pat.dto.curator;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.example.pat.dto.user.UserResponse;
import org.example.pat.entity.Curator;
import org.example.pat.entity.consts.Roles;

public record CuratorWithUserResponse(
        Long id,
        String email,
        String name,
        String phone,
        String patronymic,
        String surname,
        Roles role,
        UserResponse user
) {
    public static CuratorWithUserResponse fromEntity(Curator curator) {
        return new CuratorWithUserResponse(

                curator.getId(),
                curator.getEmail(),
                curator.getName(),
                curator.getPhone(),
                curator.getPatronymic(),
                curator.getSurname(),
                curator.getRole(),
                curator.getUser() != null ? UserResponse.fromEntity(curator.getUser()) : null
        );
    }
}
