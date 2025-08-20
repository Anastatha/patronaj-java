package org.example.pat.dto.curator;

import org.example.pat.entity.Curator;
import org.example.pat.entity.consts.Roles;

public record CuratorResponse(
        Long id,
        String email,
        String name,
        String phone,
        String patronymic,
        String surname,
        Roles role
) {
    public static CuratorResponse fromEntity(Curator curator) {
        return new CuratorResponse(
                curator.getId(),
                curator.getEmail(),
                curator.getName(),
                curator.getPhone(),
                curator.getPatronymic(),
                curator.getSurname(),
                curator.getRole()
        );
    }
}
