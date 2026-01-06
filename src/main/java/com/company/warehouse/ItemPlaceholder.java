package com.company.warehouse;

class ItemPlaceholder {
        // This is a highly resource-intensive object to create.
        public ItemPlaceholder() {
            System.out.println("ALERT: Creating expensive placeholder object!");
        }
        public String getInfo() { return "ID-NOT-FOUND: Placeholder Item"; }
}
