package com.example.sreepooja.Utility;

import java.util.Arrays;
import java.util.stream.Collectors;

public class StringCommaUtil {

    private StringCommaUtil() {
    }

    public static String normalizeCommaSeparatedValues(
            String value
    ) {

        if (value == null || value.isBlank()) {
            return null;
        }

        return Arrays.stream(value.split(","))
                .map(String::trim)
                .filter(s -> !s.isBlank())
                .distinct()
                .collect(Collectors.joining(","));
    }
}
