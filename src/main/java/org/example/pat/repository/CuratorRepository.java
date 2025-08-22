package org.example.pat.repository;

import org.example.pat.dto.curator.UpdateCuratorInput;
import org.example.pat.entity.Curator;
import org.example.pat.mapper.CuratorResultSetMapper;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class CuratorRepository {
    private final JdbcClient jdbcClient;

    public CuratorRepository(JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }

    public Curator create(Curator curator) {
        String sql = """
                INSERT INTO public.curator (email, password, name, patronymic, surname, phone, role)
                VALUES (:email, :password, :name, :patronymic, :surname, :phone, CAST(:role AS roles))
                RETURNING id, email, password, name, patronymic, surname, phone, role, deleted_at;
                """;

        return jdbcClient.sql(sql)
                .param("email", curator.getEmail())
                .param("password", curator.getPassword())
                .param("name", curator.getName())
                .param("patronymic", curator.getPatronymic())
                .param("surname", curator.getSurname())
                .param("phone", curator.getPhone())
                .param("role", curator.getRole().name())
                .query(new CuratorResultSetMapper())
                .single();
    }

    public Curator getCurator(Long id) {
        String sql = """
                    SELECT c.id, c.name, c.patronymic, c.phone, c.role, c.email,
                           c.password, c.surname, c.deleted_at
                    FROM public.curator c
                    LEFT JOIN public.user u ON c.id = u.curator_id
                    WHERE c.id = ?;
                
                """;

        return jdbcClient.sql(sql)
                .param(id)
                .query(new CuratorResultSetMapper())
                .single();
    }

    public List<Curator> getCurators() {
        String sql = """
                SELECT id, email, password, name, patronymic, surname, phone, role, deleted_at
                FROM public.curator
                """;
        return jdbcClient.sql(sql)
                .query(new CuratorResultSetMapper())
                .list();
    }

    public Curator getCuratorByEmail(String email) {
        String sql = """
                SELECT id, email, password, name, patronymic, surname, phone, role, deleted_at
                FROM curator
                WHERE email = ?;
                """;

        return jdbcClient.sql(sql)
//                .params(Map.of("email", email))
                .param(email)
                .query(new CuratorResultSetMapper())
                .single();
    }

    public Curator softDelete(Long id) {
        String sql = """
                UPDATE curator
                SET deleted_at = :deleted_at
                WHERE id = :id
                RETURNING id, email, password, name, patronymic, surname, phone, role, deleted_at;
                """;

        return jdbcClient.sql(sql)
                .param("id", id)
                .param("deleted_at", LocalDateTime.now())
                .query(new CuratorResultSetMapper())
                .single();
    }

    public Curator update(Long id, UpdateCuratorInput input) {
        StringBuilder sql = new StringBuilder("UPDATE curator SET ");
        Map<String, Object> params = new HashMap<>();
        params.put("id", id);

        if (input.email() != null) {
            sql.append("email = :email, ");
            params.put("email", input.email());
        }
        if (input.name() != null) {
            sql.append("name = :name, ");
            params.put("name", input.name());
        }
        if (input.patronymic() != null) {
            sql.append("patronymic = :patronymic, ");
            params.put("patronymic", input.patronymic());
        }
        if (input.phone() != null) {
            sql.append("phone = :phone, ");
            params.put("phone", input.phone());
        }
        if (input.role() != null) {
            sql.append("role = :role, ");
            params.put("role", input.role().name());
        }

        sql.delete(sql.length() - 2, sql.length());
        sql.append(" WHERE id = :id RETURNING *");

        return jdbcClient.sql(sql.toString())
                .params(params)
                .query(new CuratorResultSetMapper())
                .single();
    }
}
