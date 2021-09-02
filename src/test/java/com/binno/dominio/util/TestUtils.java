package com.binno.dominio.util;

import com.fasterxml.jackson.databind.ObjectMapper;

public final class TestUtils {

    public static Integer getTenantId() {
        return 1;
    }

    private static ObjectMapper mapper = new ObjectMapper();

    public static String objectToJson(Object value) throws Exception {
        return mapper.writeValueAsString(value);
    }
}
