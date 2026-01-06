package com.company.config;

import java.lang.reflect.Field;

public class ConfigValidator {

    public static void validate(Object configObject) {
        if (configObject == null) {
            throw new ConfigValidationException("Config object cannot be null.");
        }

        Class<?> clazz = configObject.getClass();

        for (Field field : clazz.getDeclaredFields()) {
            RangeCheck rangeCheck = field.getAnnotation(RangeCheck.class);

            // Only validate fields that have @RangeCheck
            if (rangeCheck == null) continue;

            // This system is for int fields (per requirements)
            if (field.getType() != int.class) {
                throw new ConfigValidationException(
                        "Field '" + field.getName() + "' must be an int to use @RangeCheck."
                );
            }

            field.setAccessible(true);

            try {
                int value = field.getInt(configObject);
                int min = rangeCheck.min();
                int max = rangeCheck.max();

                if (value < min || value > max) {
                    throw new ConfigValidationException(
                            "Invalid config: '" + field.getName() + "'=" + value +
                                    " (allowed range: " + min + " to " + max + ")"
                    );
                }

                SystemConfig.logSuccess(
                        "Validated '" + field.getName() + "'=" + value +
                                " within range [" + min + ", " + max + "]"
                );

            } catch (IllegalAccessException e) {
                throw new ConfigValidationException(
                        "Unable to access field '" + field.getName() + "'", e
                );
            }
        }
    }
}

