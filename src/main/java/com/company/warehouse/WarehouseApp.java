package com.company.warehouse;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class WarehouseApp {

    public static Object safeLookup(String id) {
        return Optional.ofNullable(Inventory.findItem(id))
                    .<Object>map(inv -> inv)
                    .orElseGet(ItemPlaceholder::new); // LAZY: created only if Optional is empty
    }

    public static Set<String> getUniqueItemIds(Inventory inventory) {
        return inventory.getPalletItemIds().stream()
                .flatMap(pallet -> pallet.stream())
                .collect(Collectors.toSet());   //Set is used so that there is unique item id
    }

    public static void main(String[] args) {

        System.out.println("---- Successful lookup ----");
        Object result1 = safeLookup("A100");

        if (result1 instanceof Inventory inv) {
            Set<String> ids = getUniqueItemIds(inv);
            System.out.println("Unique item IDs: " + ids);
        }

        System.out.println("\n---- Failed lookup ----");
        Object result2 = safeLookup("X999");

        if (result2 instanceof ItemPlaceholder placeholder) {
            // ALERT message prints ONLY here
            System.out.println(placeholder.getInfo());
        }
    }

}
