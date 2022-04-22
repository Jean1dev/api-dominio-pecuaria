package com.binno.dominio.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

public final class TestUtils {

    public static Integer getTenantId() {
        return 1;
    }

    private static ObjectMapper mapper = new ObjectMapper();

    public static String objectToJson(Object value) throws Exception {
        mapper.registerModule(new JavaTimeModule());
        mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        return mapper.writeValueAsString(value);
    }
}
