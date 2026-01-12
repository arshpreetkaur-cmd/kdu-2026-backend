package org.example.quickshiplogisticshub.service;

import org.example.quickshiplogisticshub.model.PackageItem;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import static org.example.quickshiplogisticshub.constants.AppConstants.*;

import java.util.Map;

@Service
public class PackageScanService {

    @Async
    public void scanPackage(String packageId, Map<String, PackageItem> warehouse) {
        try {
            Thread.sleep(SCAN_DELAY_MS); // simulate scan

            PackageItem pkg = warehouse.get(packageId);
            if (pkg != null) {
                pkg.setStatus(STATUS_SORTED);
            }

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
