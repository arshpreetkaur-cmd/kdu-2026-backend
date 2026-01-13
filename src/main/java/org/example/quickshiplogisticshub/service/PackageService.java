package org.example.quickshiplogisticshub.service;

import org.example.quickshiplogisticshub.model.PackageItem;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import static org.example.quickshiplogisticshub.constants.AppConstants.*;

@Service
public class PackageService {

    // Thread-safe in-memory warehouse
    private final Map<String, PackageItem> warehouse = new ConcurrentHashMap<>();
    private final PackageScanService scanService;
    public PackageService(PackageScanService scanService) {
        this.scanService = scanService;
    }

    //PHASE 1 (SYNCHRONOUS)
    /*
    public PackageItem addPackage(PackageItem pkg) throws InterruptedException {

        pkg.setId(UUID.randomUUID().toString());
        pkg.setStatus(STATUS_PENDING);

        warehouse.put(pkg.getId(), pkg);

        // blocking scan (slow)
        Thread.sleep(3000);

        pkg.setStatus(STATUS_SORTED);

        return pkg;
    }
    */

    //phase-2
    public PackageItem addPackage(PackageItem pkg) {

        pkg.setId(UUID.randomUUID().toString());
        pkg.setStatus(STATUS_PENDING);

        warehouse.put(pkg.getId(), pkg);

        scanService.scanPackage(pkg.getId(), warehouse);

        return pkg;
    }

    public Map<String, Long> getWarehouseStats() {

        long pendingCount = warehouse.values().stream()
                .filter(p -> STATUS_PENDING.equals(p.getStatus()))
                .count();

        long sortedCount = warehouse.values().stream()
                .filter(p -> STATUS_SORTED.equals(p.getStatus()))
                .count();

        return Map.of(
                "PENDING", pendingCount,
                "SORTED", sortedCount
        );
    }

    public double calculateRevenue() {

        return warehouse.values().stream()
                // only SORTED packages
                .filter(pkg -> "SORTED".equals(pkg.getStatus()))
                // revenue = weight * 2.50
                .mapToDouble(pkg -> pkg.getWeight() * 2.50)
                .sum();
    }

}

