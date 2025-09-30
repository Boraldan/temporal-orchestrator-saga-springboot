package ru.boraldan.apiservice.tool;

import java.math.BigDecimal;

/**
 *  Утилиты для сравнения BigDecimal
*/

public final class BigDecimalUtils {

    private BigDecimalUtils() {
    }

    public static boolean isZero(BigDecimal value) {
        return value != null && value.compareTo(BigDecimal.ZERO) == 0;
    }

    public static boolean isPositive(BigDecimal value) {
        return value != null && value.compareTo(BigDecimal.ZERO) > 0;
    }

    public static boolean isNegative(BigDecimal value) {
        return value != null && value.compareTo(BigDecimal.ZERO) < 0;
    }

    public static boolean isZeroOrNegative(BigDecimal value) {
        return value != null && value.compareTo(BigDecimal.ZERO) <= 0;
    }

    public static boolean isZeroOrPositive(BigDecimal value) {
        return value != null && value.compareTo(BigDecimal.ZERO) >= 0;
    }

    public static boolean equals(BigDecimal a, BigDecimal b) {
        if (a == null && b == null) return true;
        if (a == null || b == null) return false;
        return a.compareTo(b) == 0;
    }
}
