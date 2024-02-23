package com.codingee.ranked.ranktracker.util;

import com.codingee.ranked.ranktracker.util.exceptions.ValidationException;

import java.util.Collection;
import java.util.Objects;

public class ValidationUtil {

    public static void ensureEqual(Integer val1, Integer val2, String errorMessage) {
        if (!val1.equals(val2)) {
            throw new ValidationException(errorMessage.concat(String.format("; %s IS NOT EQUAL TO %s", val1, val2)));
        }
    }

    public static void ensureEqual(String val1, String val2, String errorMessage) {
        if (!val1.equals(val2)) {
            throw new ValidationException(errorMessage.concat(String.format("; %s IS NOT EQUAL TO %s", val1, val2)));
        }
    }
    public static void ensureTrue(Boolean bool, String message) {
        if(!bool) {
            throw new ValidationException(message);
        }
    }

    public static void ensureFalse(Boolean bool, String message) {
        if (bool) {
            throw new ValidationException(message);
        }
    }

    public static void ensureNotNull(Object value, String name){
        if (Objects.isNull(value)) {
            throw new ValidationException(name + " cannot be null");
        }
    }

    public static void ensureNotEmpty(String value, String name){
        if (Objects.isNull(value) || value.isEmpty()) {
            throw new ValidationException(name + " cannot be null or empty");
        }
    }

    public static <T extends Collection> void ensureHasElements(T data, String name){
        if (data == null || data.size() < 1) {
            throw new ValidationException(name + " must contain elements.");
        }
    }

}
