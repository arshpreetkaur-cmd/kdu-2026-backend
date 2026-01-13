package com.smartcity.hospital.api;

import com.smartcity.hospital.db.ShiftTypeRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/shift-types")
public class ShiftTypeController {

    private final ShiftTypeRepository repository;

    public ShiftTypeController(ShiftTypeRepository repository) {
        this.repository = repository;
    }

    @PostMapping
    public Map<String, String> create(@RequestBody Map<String, Object> body) {
        String id = repository.insertShiftType(
                (String) body.get("name"),
                (String) body.get("description"),
                (Boolean) body.get("active"),
                (String) body.get("tenantId")
        );

        return Map.of("id", id);
    }

    @GetMapping
    public List<Map<String, Object>> getByTenant(@RequestParam String tenantId) {
        return repository.findByTenant(tenantId);
    }
}
