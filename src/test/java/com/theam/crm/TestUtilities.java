package com.theam.crm;

import com.fasterxml.jackson.databind.ObjectMapper;

public class TestUtilities {

    public static String convertToJson(final Object obj) {
        try {
            final ObjectMapper mapper = new ObjectMapper();
            return mapper.writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
