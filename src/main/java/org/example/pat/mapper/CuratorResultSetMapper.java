package org.example.pat.mapper;

import org.example.pat.entity.Curator;
import org.example.pat.entity.consts.Roles;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

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

        return curator;
    }
}
