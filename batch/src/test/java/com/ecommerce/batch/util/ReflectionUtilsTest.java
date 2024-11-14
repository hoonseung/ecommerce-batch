package com.ecommerce.batch.util;

import org.junit.jupiter.api.Test;

class ReflectionUtilsTest {


    @Test
    void 클래스정보를넘겼을떄_해당클래스의정적필드를제외한_필드이름들을_제대로반환하는지() {
        String[] fieldNames = ReflectionUtils.getFieldNames(TestClass.class);
        for (String fieldName : fieldNames) {
            System.out.println("fieldName: " + fieldName);
        }

        assert fieldNames.length == 2;
    }

    static class TestClass {

        private String name;
        private String email;
        private static final String staticField = "static";
    }
}