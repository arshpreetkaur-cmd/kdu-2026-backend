package com.company.config;

public class Main {
    public static void main(String[] args) {
        // Valid
        SystemConfig ok = new SystemConfig(8, 2500);
        ConfigValidator.validate(ok);

        // Invalid (maxThreads too high)
        SystemConfig bad = new SystemConfig(20, 2500);
        try{
            ConfigValidator.validate(bad);
        } catch(ConfigValidationException e){
            System.out.println("VALIDATION FAILED" + e.getMessage());
        }
    }
}

