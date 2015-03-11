package com.dgsd.android.data.model;

import java.util.Arrays;

class ModelUtils {

    static int getInt(String field) {
        try {
            return Integer.valueOf(field);
        } catch(NumberFormatException | NullPointerException ex) {
            return -1;
        }
    }

    static float getFloat(String field) {
        try {
            return Float.valueOf(field);
        } catch(NumberFormatException | NullPointerException ex) {
            return -1;
        }
    }

    static void assertImport(String[] csvImport, int expectedSize) {
        if (csvImport == null || csvImport.length != expectedSize) {
            throw new IllegalArgumentException(
                    "Invalid csv import data: " + Arrays.toString(csvImport));
        }
    }
}
