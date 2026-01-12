package org.example.quickshiplogisticshub.controller;

import org.example.quickshiplogisticshub.service.PackageService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/analytics")
public class AnalyticsController {

    private final PackageService service;

    public AnalyticsController(PackageService service) {
        this.service = service;
    }

    @GetMapping("/revenue")
    public ResponseEntity<?> getRevenue() {

        double revenue = service.calculateRevenue();

        return ResponseEntity.ok(
                Map.of("revenue", revenue)
        );
    }
}