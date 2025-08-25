package org.example.pat.mapper;

import org.example.pat.entity.Curator;
import org.example.pat.entity.User;
import org.example.pat.entity.consts.Categorie;
import org.example.pat.entity.consts.Label;
import org.example.pat.entity.consts.Roles;

import org.example.pat.entity.consts.Status;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class CuratorResultSetMapper implements RowMapper<Curator> {
    @Override
    public Curator mapRow(ResultSet rs, int rowNum) throws SQLException {
        Curator curator = new Curator();

        curator.setId(rs.getLong("id"));
        curator.setEmail(rs.getString("email"));
        curator.setPassword(rs.getString("password"));
        curator.setName(rs.getString("name"));
        curator.setPatronymic(rs.getString("patronymic"));
        curator.setSurname(rs.getString("surname"));
        curator.setPhone(rs.getString("phone"));

        Timestamp deletedAtTimestamp = rs.getTimestamp("deleted_at");
        if (deletedAtTimestamp != null) {
            curator.setDeletedAt(deletedAtTimestamp.toLocalDateTime());
        } else {
            curator.setDeletedAt(null);
        }

        String roleStr = rs.getString("role");
        if (roleStr != null) {
            curator.setRole(Roles.valueOf(roleStr));
        }

        Long userEntityId = null;
        if(hasColumn(rs, "user_entity_id")) {
            long tmp = rs.getLong("user_entity_id");
            if(!rs.wasNull()) {
                userEntityId = tmp;
            }
        }
        if(userEntityId != null) {
            User user = new User();
            user.setId(userEntityId);
            user.setEmail(rs.getString("user_email"));
            user.setName(getNullableString(rs, "user_name"));
            user.setSurname(getNullableString(rs, "user_surname"));
            user.setPatronymic(getNullableString(rs, "user_patronymic"));
            user.setInn(getNullableString(rs, "user_inn"));
            user.setEmail(getNullableString(rs, "user_email"));
            user.setPhone(getNullableString(rs, "user_phone"));
            user.setOkved(getNullableString(rs, "user_okved"));
            String categoryStr = rs.getString("user_category");
            if (categoryStr != null) {
                user.setCategory(Categorie.valueOf(categoryStr));
            }

            String statusStr = rs.getString("user_status");
            if (statusStr != null) {
                user.setStatus(Status.valueOf(statusStr));
            }

            user.setCode(getNullableString(rs, "user_code"));
            user.setOrganizationName(getNullableString(rs, "user_organization_name"));
            user.setUserTelegramId(getNullableString(rs, "user_telegram_id"));
            Timestamp user_deleted_at = rs.getTimestamp("user_deleted_at");
            user.setDeletedAt(user_deleted_at != null ? user_deleted_at.toLocalDateTime() : null);

            java.sql.Array labelArray = rs.getArray("user_label");
            if (labelArray != null) {
                Object[] rawArray = (Object[]) labelArray.getArray();
                if (rawArray != null && rawArray.length > 0) {
                    List<Label> labels = Arrays.stream(rawArray)
                            .map(Object::toString)
                            .map(Label::valueOf)
                            .collect(Collectors.toList());
                    user.setLabel(labels);
                } else {
                    user.setLabel(Collections.emptyList());
                }
            } else {
                user.setLabel(Collections.emptyList());
            }

            curator.setUser(user);
        }

        return curator;
    }

    private String getNullableString(ResultSet rs, String columnName) throws SQLException {
        String value = rs.getString(columnName);
        return rs.wasNull() ? null : value;
    }

    private boolean hasColumn(ResultSet rs, String columnName) throws SQLException {
        try {
            rs.findColumn(columnName);
            return true;
        } catch (SQLException e) {
            return false;
        }
    }
}
