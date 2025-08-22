package org.example.pat.mapper;

import org.example.pat.entity.Curator;
import org.example.pat.entity.User;
import org.example.pat.entity.consts.Categorie;
import org.example.pat.entity.consts.Label;
import org.example.pat.entity.consts.Status;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

@Component
public class UserResultSetMapper implements RowMapper<User> {
    @Override
    public User mapRow(ResultSet rs, int rowNum) throws SQLException {
        User user = new User();

        user.setId(rs.getLong("id"));
        user.setUserTelegramId(getNullableString(rs, "user_telegram_id"));
        user.setName(getNullableString(rs, "name"));
        user.setSurname(getNullableString(rs, "surname"));
        user.setPatronymic(getNullableString(rs, "patronymic"));
        user.setOrganizationName(getNullableString(rs, "organization_name"));
        user.setInn(rs.getString("inn"));
        user.setEmail(rs.getString("email"));
        user.setPhone(rs.getString("phone"));
        user.setCode(rs.getString("code"));
        user.setCuratorId(rs.getLong("user_curator_id"));
        user.setOkved(getNullableString(rs, "okved"));

        java.sql.Array labelArray = rs.getArray("label");
        if (labelArray != null) {
            Object[] rawArray = (Object[]) labelArray.getArray();
            if (rawArray != null && rawArray.length > 0) {
                List<Label> labels = Arrays.stream(rawArray)
                        .map(Object::toString)   // PGObject -> String
                        .map(Label::valueOf)     // String -> Enum
                        .collect(Collectors.toList());
                user.setLabel(labels);
            } else {
                user.setLabel(Collections.emptyList());
            }
        } else {
            user.setLabel(Collections.emptyList());
        }

        Timestamp deletedAtTimestamp = rs.getTimestamp("deleted_at");
        user.setDeletedAt(deletedAtTimestamp != null ? deletedAtTimestamp.toLocalDateTime() : null);

        String categoryStr = rs.getString("category");
        if (categoryStr != null) {
            user.setCategory(Categorie.valueOf(categoryStr));
        }

        String statusStr = rs.getString("status");
        if (statusStr != null) {
            user.setStatus(Status.valueOf(statusStr));
        }

        Long curatorId = rs.getLong("curator_id");
        if (curatorId != 0 && hasColumn(rs, "curator_name")) {
            Curator curator = new Curator();
            curator.setId(curatorId);
            curator.setName(getNullableString(rs, "curator_name"));
            curator.setSurname(getNullableString(rs, "curator_surname"));
            curator.setEmail(getNullableString(rs, "curator_email"));
            user.setCurator(curator);
        }
        return user;
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