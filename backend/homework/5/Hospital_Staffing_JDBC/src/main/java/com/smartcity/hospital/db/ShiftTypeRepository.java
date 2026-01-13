package com.smartcity.hospital.db;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Repository
public class ShiftTypeRepository {

    private final JdbcTemplate jdbcTemplate;

    public ShiftTypeRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public String insertShiftType(String name, String description, boolean active, String tenantId) {
        String id = UUID.randomUUID().toString();

        String sql = """
            INSERT INTO shift_type (id, name, description, active, tenant_id)
            VALUES (?, ?, ?, ?, ?)
        """;

        jdbcTemplate.update(sql, id, name, description, active, tenantId);
        return id;
    }

    public List<Map<String, Object>> findByTenant(String tenantId) {
        String sql = """
            SELECT id, name, description, active
            FROM shift_type
            WHERE tenant_id = ?
        """;

        return jdbcTemplate.queryForList(sql, tenantId);
    }
}
