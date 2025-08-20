//package org.example.pat.repository;
//
//import org.example.pat.entity.User;
//import org.example.pat.mapper.UserResultSetMapper;
//import org.springframework.jdbc.core.JdbcTemplate;
//import org.springframework.jdbc.support.GeneratedKeyHolder;
//import org.springframework.jdbc.support.KeyHolder;
//import org.springframework.stereotype.Repository;
//
//import javax.sql.DataSource;
//import java.sql.*;
//
//@Repository
//public class UserRepository {
//    private final JdbcTemplate jdbcTemplate;
//    private final UserResultSetMapper userMapper;
//
//    public UserRepository(JdbcTemplate jdbcTemplate, UserResultSetMapper userMapper) {
//        this.jdbcTemplate = jdbcTemplate;
//        this.userMapper = userMapper;
//    }
//
//    public User create(User user) {
//        String sql = """
//                INSERT INTO "user" (
//                    name, surname, patronymic, email, organization_name,
//                    inn, phone, code, status, category, curator_id, label, okved
//                ) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?::status, ?::categories, ?, ?::labels[], ?)
//                RETURNING id
//                """;
//
//        KeyHolder keyHolder = new GeneratedKeyHolder();
//
//        jdbcTemplate.update(connection -> {
//            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
//            ps.setString(1, user.getName());
//            ps.setString(2, user.getSurname());
//            ps.setString(3, user.getPatronymic());
//            ps.setString(4, user.getEmail());
//            ps.setString(5, user.getOrganizationName());
//            ps.setString(6, user.getInn());
//            ps.setString(7, user.getPhone());
//            ps.setString(8, user.getCode());
//            ps.setString(9, user.getStatus().name());
//            ps.setString(10, user.getCategory().name());
//            ps.setLong(11, user.getCuratorId());
//
//            Array labelArray = connection.createArrayOf(
//                    "labels",
//                    user.getLabel() != null ? user.getLabel().stream().map(Enum::name).toArray() : new String[0]
//            );
//            ps.setArray(12, labelArray);
//
//            ps.setString(13, user.getOkved());
//
//            return ps;
//        }, keyHolder);
//
//        Number key = keyHolder.getKey();
//        if (key == null) {
//            throw new IllegalStateException("Failed to retrieve generated user ID");
//        }
//        Long userId = key.longValue();
//        return findById(userId);
//    }
//
//
//    public User findById(Long id) {
//        String sql = "SELECT * FROM \"user\" WHERE id = ?";
//        return jdbcTemplate.queryForObject(sql, userMapper, id);
//    }
//}

package org.example.pat.repository;

import org.example.pat.entity.User;
import org.example.pat.mapper.UserResultSetMapper;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

import java.util.stream.Collectors;

@Repository
public class UserRepository {
    private final JdbcClient jdbcClient;
    private final UserResultSetMapper userMapper;

    public UserRepository(JdbcClient jdbcClient, UserResultSetMapper userMapper) {
        this.jdbcClient = jdbcClient;
        this.userMapper = userMapper;
    }

    public User create(User user) {
        String labelsCsv = user.getLabel() != null
                ? user.getLabel().stream().map(Enum::name).collect(Collectors.joining(","))
                : "";

        String sql = """
                INSERT INTO "user" (
                    name, surname, patronymic, email, organization_name,
                    inn, phone, code, status, category, curator_id, label, okved
                ) VALUES (
                    :name, :surname, :patronymic, :email, :organization_name,
                    :inn, :phone, :code, CAST(:status AS status), CAST(:category AS categories),
                    :curator_id, string_to_array(:label, ',')::labels[], :okved
                )
                RETURNING *
                """;

        return jdbcClient.sql(sql)
                .param("name", user.getName())
                .param("surname", user.getSurname())
                .param("patronymic", user.getPatronymic())
                .param("email", user.getEmail())
                .param("organization_name", user.getOrganizationName())
                .param("inn", user.getInn())
                .param("phone", user.getPhone())
                .param("code", user.getCode())
                .param("status", user.getStatus().name())
                .param("category", user.getCategory().name())
                .param("curator_id", user.getCuratorId())
                .param("label", labelsCsv)
                .param("okved", user.getOkved())
                .query(userMapper)
                .single();
    }

//    public User findById(Long id) {
//        String sql = "SELECT * FROM \"user\" WHERE id = ?";
//        return jdbcClient.sql(sql)
//                .param( id)
//                .query(userMapper)
//                .single();
//    }
}
