package com.binno.dominio.shared.utils;

import java.math.BigDecimal;
import java.util.Objects;

public final class BinnoUtills {

    public static String valueOrEmpty(String value) {
        return Objects.isNull(value) ? "N/A" : value;
    }

    public static BigDecimal valueOrEmpty(BigDecimal value) {
        return Objects.isNull(value) ? BigDecimal.ZERO : value;
    }

    public static String ifNullReturnValue(String value, String another) {
        return Objects.isNull(value) ? another : value;
    }

    public static Object ifNullReturnValue(Object value, Object another) {
        return Objects.isNull(value) ? another : value;
    }
}
