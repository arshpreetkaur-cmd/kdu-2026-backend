package org.example.quickshiplogisticshub.controller;

import org.example.quickshiplogisticshub.model.PackageItem;
import org.example.quickshiplogisticshub.service.PackageService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/packages")
public class PackageController {

    private final PackageService service;

    public PackageController(PackageService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<?> addPackage(@RequestBody PackageItem pkg) throws InterruptedException {

        if (pkg.getWeight() <= 0) {
            return ResponseEntity.badRequest().body("Invalid weight");
        }

        PackageItem accepted = service.addPackage(pkg);

        return ResponseEntity.ok(accepted);
    }

    @GetMapping("/stats")
    public ResponseEntity<?> getWarehouseStats() {
        return ResponseEntity.ok(service.getWarehouseStats());
    }
}