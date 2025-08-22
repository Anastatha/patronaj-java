package org.example.pat.repository;

import io.micrometer.common.lang.Nullable;
import org.example.pat.dto.user.UpdateUserInput;
import org.example.pat.entity.User;
import org.example.pat.entity.consts.Label;
import org.example.pat.mapper.UserResultSetMapper;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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

    public User softDelete(Long id) {
        String sql = """
            UPDATE public."user"
            SET deleted_at = ?
            WHERE id = ?
            RETURNING id,
                      user_telegram_id,
                      name,
                      surname,
                      patronymic,
                      email,
                      organization_name,
                      inn,
                      phone,
                      code,
                      status,
                      category,
                      curator_id      AS user_curator_id,
                      label,
                      okved,
                      deleted_at
            """;

        return jdbcClient.sql(sql)
                .param(LocalDateTime.now()) // deleted_at
                .param(id)                 // id
                .query(userMapper)
                .single();
    }


    public List<User> getUsers() {
        String sql = """
                SELECT
                    u.id,
                    u.user_telegram_id,
                    u.name,
                    u.surname,
                    u.patronymic,
                    u.email,
                    u.organization_name,
                    u.inn,
                    u.phone,
                    u.code,
                    u.status,
                    u.category,
                    u.curator_id      AS user_curator_id,
                    u.label,
                    u.okved,
                    u.deleted_at,
                    c.id              AS curator_id,
                    c.name            AS curator_name,
                    c.surname         AS curator_surname,
                    c.email           AS curator_email,
                    c.phone           AS curator_phone
                FROM public."user" u
                LEFT JOIN public.curator c ON u.curator_id = c.id
                WHERE u.deleted_at IS NULL;
                """;
        return jdbcClient.sql(sql)
                .query(userMapper)
                .list();
    }

    public User getUser(Long id) {
        String sql = """
                SELECT\s
                    u.id,
                    u.user_telegram_id,
                    u.name,
                    u.surname,
                    u.patronymic,
                    u.email,
                    u.organization_name,
                    u.inn,
                    u.phone,
                    u.code,
                    u.status,
                    u.category,
                    u.curator_id AS user_curator_id,
                    u.label,
                    u.okved,
                    u.deleted_at,
                    c.id       AS curator_id,
                    c.name     AS curator_name,
                    c.surname  AS curator_surname,
                    c.email    AS curator_email,
                    c.phone    AS curator_phone
                FROM public.user u
                LEFT JOIN public.curator c ON u.curator_id = c.id
                WHERE u.deleted_at IS NULL
                  AND u.id = ?;
                """;
        return jdbcClient.sql(sql)
                .param(id)
                .query(userMapper)
                .single();
    }

    public List<User> getUsersByCuratorId(Long id) {
        String sql = """
                  SELECT u.id,
                      u.user_telegram_id,
                      u.name,
                      u.surname,
                      u.patronymic,
                      u.email,
                      u.organization_name,
                      u.inn,
                      u.phone,
                      u.code,
                      u.status,
                      u.category,
                      u.curator_id AS user_curator_id,
                      u.label,
                      u.okved,
                      u.deleted_at,
                      c.id       AS curator_id,
                      c.name     AS curator_name,
                      c.surname  AS curator_surname,
                      c.email    AS curator_email,
                      c.phone    AS curator_phone
                      FROM public.user u
                    LEFT JOIN public.curator c ON u.curator_id = c.id
                      WHERE u.curator_id = ?;
                """;
        return jdbcClient.sql(sql)
                .param(id)
                .query(userMapper)
                .list();
    }

//    public List<User> getUsersByLabel(List<Label> labels) {
//        if (labels == null || labels.isEmpty()) return Collections.emptyList();
//
//        String labelsCsv = labels.stream()
//                .map(Enum::name)
//                .collect(Collectors.joining(","));
//
//        String sql = """
//        SELECT u.id,
//               u.user_telegram_id,
//               u.name,
//               u.surname,
//               u.patronymic,
//               u.email,
//               u.organization_name,
//               u.inn,
//               u.phone,
//               u.code,
//               u.status,
//               u.category,
//               u.curator_id AS user_curator_id,
//               u.label,
//               u.okved,
//               u.deleted_at,
//               c.id AS curator_id,
//               c.name AS curator_name,
//               c.surname AS curator_surname,
//               c.email AS curator_email,
//               c.phone AS curator_phone
//        FROM public."user" u
//        LEFT JOIN public.curator c ON u.curator_id = c.id
//        WHERE u.label && string_to_array(:labels, ',')::labels[]
//          AND u.user_telegram_id IS NOT NULL
//          AND u.deleted_at IS NULL
//    """;
//
//        return jdbcClient.sql(sql)
//                .param("labels", labelsCsv)
//                .query(userMapper)
//                .list();
//    }

    public List<User> getUsersByOkved(String okved) {
        String sql = """
            SELECT u.id,
                   u.user_telegram_id,
                   u.name,
                   u.surname,
                   u.patronymic,
                   u.email,
                   u.organization_name,
                   u.inn,
                   u.phone,
                   u.code,
                   u.status,
                   u.category,
                   u.curator_id AS user_curator_id,
                   u.label,
                   u.okved,
                   u.deleted_at
            FROM public.user u
            WHERE u.okved = ?
              AND u.user_telegram_id IS NOT NULL
              AND u.deleted_at IS NULL
            """;

        return jdbcClient.sql(sql)
                .param(okved)
                .query(userMapper)
                .list();
    }

    public List<User> getUsersLabelOrOkved(List<String> okved, List<Label> labels) {
        String sql = """
                SELECT 
                    u.id,
                    u.user_telegram_id,
                    u.name,
                    u.surname,
                    u.patronymic,
                    u.email,
                    u.organization_name,
                    u.inn,
                    u.phone,
                    u.code,
                    u.status,
                    u.category,
                    u.curator_id AS user_curator_id,
                    u.label,
                    u.okved,
                    u.deleted_at,
                    c.id AS curator_id,
                    c.name AS curator_name,
                    c.surname AS curator_surname,
                    c.email AS curator_email,
                    c.phone AS curator_phone
                FROM public.user u
                WHERE (
                    (u.label && CAST(? AS labels[]) AND u.user_telegram_id IS NOT NULL)
                    OR
                    (u.okved = ANY(?) AND u.user_telegram_id IS NOT NULL)
                )
                AND u.deleted_at IS NULL
                """;

        // Преобразуем labels в массив строк
        String[] labelNames = labels.stream()
                .map(Enum::name)
                .toArray(String[]::new);

        // Преобразуем okvedList в массив строк
        String[] okvedArray = okved.toArray(new String[0]);

        return jdbcClient.sql(sql)
                .param(labelNames)
                .param(okvedArray)
                .query(userMapper)
                .list();
    }

    public User update(Long id, UpdateUserInput input) {
        StringBuilder sql = new StringBuilder("UPDATE user SET ");
        Map<String, Object> params = new HashMap<>();
        params.put("id", id);

        if (input.userTelegramId() != null) {
            sql.append("user_telegram_id = :userTelegramId, ");
            params.put("userTelegramId", input.userTelegramId());
        }
        if (input.name() != null) {
            sql.append("name = :name, ");
            params.put("name", input.name());
        }
        if (input.surname() != null) {
            sql.append("surname = :surname, ");
            params.put("surname", input.surname());
        }
        if (input.patronymic() != null) {
            sql.append("patronymic = :patronymic, ");
            params.put("patronymic", input.patronymic());
        }
        if (input.email() != null) {
            sql.append("email = :email, ");
            params.put("email", input.email());
        }
        if (input.organizationName() != null) {
            sql.append("organization_name = :organizationName, ");
            params.put("organizationName", input.organizationName());
        }
        if (input.inn() != null) {
            sql.append("inn = :inn, ");
            params.put("inn", input.inn());
        }
        if (input.phone() != null) {
            sql.append("phone = :phone, ");
            params.put("phone", input.phone());
        }
        if (input.status() != null) {
            sql.append("status = CAST(:status AS status), ");
            params.put("status", input.status().name());
        }
        if (input.deletedAt() != null) {
            sql.append("deleted_at = :deletedAt, ");
            params.put("deletedAt", input.deletedAt());
        }
        if (input.category() != null) {
            sql.append("category = CAST(:category AS categories), ");
            params.put("category", input.category().name());
        }
        if (input.curatorId() != null) {
            sql.append("curator_id = :curatorId, ");
            params.put("curatorId", input.curatorId());
        }
        if (input.label() != null) {
            sql.append("label = :label, ");
            // Преобразуем List<Label> в массив для PostgreSQL
            String[] labelArray = input.label().stream()
                    .map(Enum::name)
                    .toArray(String[]::new);
            params.put("label", labelArray);
        }
        if (input.okved() != null) {
            sql.append("okved = :okved, ");
            params.put("okved", input.okved());
        }

        sql.append("updated_at = :updatedAt, ");
        params.put("updatedAt", LocalDateTime.now());

        if (sql.toString().endsWith(", ")) {
            sql.delete(sql.length() - 2, sql.length());
        }

        sql.append(" WHERE id = :id RETURNING *");

        return jdbcClient.sql(sql.toString())
                .params(params)
                .query(userMapper)
                .single();
    }

    public List<User> getdUsersBetween(@Nullable String from, @Nullable String to) {
        StringBuilder sql = new StringBuilder("""
                SELECT 
                    u.id,
                    u.user_telegram_id,
                    u.name,
                    u.surname,
                    u.patronymic,
                    u.email,
                    u.organization_name,
                    u.inn,
                    u.phone,
                    u.code,
                    u.status,
                    u.category,
                    u.curator_id AS user_curator_id,
                    u.label,
                    u.okved,
                    u.deleted_at,
                    u.updated_at,
                    c.id AS curator_id,
                    c.name AS curator_name,
                    c.surname AS curator_surname,
                    c.email AS curator_email,
                    c.phone AS curator_phone
                FROM public.user u
                LEFT JOIN public.user c ON u.curator_id = c.id
                WHERE u.deleted_at IS NULL
                """);

        Map<String, Object> params = new HashMap<>();

        if (from != null && !from.isBlank()) {
            sql.append(" AND u.updated_at >= :from");
            params.put("from", LocalDateTime.parse(from));
        }

        if (to != null && !to.isBlank()) {
            sql.append(" AND u.updated_at <= :to");
            // Устанавливаем время на конец дня (23:59:59.999)
            LocalDateTime endOfDay = LocalDateTime.parse(to)
                    .toLocalDate()
                    .atTime(23, 59, 59, 999000000);
            params.put("to", endOfDay);
        }

        JdbcClient.StatementSpec statement = jdbcClient.sql(sql.toString());

        for (Map.Entry<String, Object> entry : params.entrySet()) {
            statement = statement.param(entry.getKey(), entry.getValue());
        }

        return statement.query(userMapper).list();
    }
}
