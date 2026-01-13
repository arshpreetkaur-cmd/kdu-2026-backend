package com.smartcity.hospital.db;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Repository
public class UserRepository {

    private final JdbcTemplate jdbcTemplate;

    public UserRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // Exercise 1 - Task 1: Native INSERT
    public String insertUser(String username, boolean loggedIn, String timezone, String tenantId) {
        String id = UUID.randomUUID().toString();

        String sql = """
            INSERT INTO users (id, username, logged_in, timezone, tenant_id)
            VALUES (?, ?, ?, ?, ?)
        """;

        jdbcTemplate.update(sql, id, username, loggedIn, timezone, tenantId);
        return id;
    }

    // Exercise 1 - Task 2: Fetch users by tenant
    public List<Map<String, Object>> findUsersByTenant(String tenantId) {
        String sql = """
            SELECT id, username, logged_in, timezone, tenant_id, created_at
            FROM users
            WHERE tenant_id = ?
        """;

        return jdbcTemplate.queryForList(sql, tenantId);
    }

    // Exercise 1 - Task 3: Native UPDATE
    public int updateUser(String userId, String tenantId, String username, String timezone) {
        String sql = """
            UPDATE users
            SET username = ?, timezone = ?
            WHERE id = ? AND tenant_id = ?
        """;

        return jdbcTemplate.update(sql, username, timezone, userId, tenantId);
    }
}
