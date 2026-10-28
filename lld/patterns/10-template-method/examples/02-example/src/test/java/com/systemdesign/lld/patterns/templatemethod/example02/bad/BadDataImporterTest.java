package com.systemdesign.lld.patterns.templatemethod.example02.bad;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Bad Data Importer Unit Tests")
class BadDataImporterTest {

    @Test
    @DisplayName("Should import CSV data using duplicated class logic")
    void testBadCsvImporter() {
        BadCsvDataImporter importer = new BadCsvDataImporter();
        int count = importer.importData("1, User, user@test.com");
        assertEquals(1, count);
        assertEquals(1, importer.getDatabase().size());
    }

    @Test
    @DisplayName("Should import JSON data using duplicated class logic")
    void testBadJsonImporter() {
        BadJsonDataImporter importer = new BadJsonDataImporter();
        int count = importer.importData("{\"id\": \"1\", \"name\": \"User\", \"email\": \"user@test.com\"}");
        assertEquals(1, count);
        assertEquals(1, importer.getDatabase().size());
    }
}
