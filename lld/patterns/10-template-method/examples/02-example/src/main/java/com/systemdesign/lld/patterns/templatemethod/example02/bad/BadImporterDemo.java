package com.systemdesign.lld.patterns.templatemethod.example02.bad;

public class BadImporterDemo {
    public static void main(String[] args) {
        BadCsvDataImporter csvImporter = new BadCsvDataImporter();
        csvImporter.importData("1, Alice, alice@example.com\n2, Bob, invalid-email");

        BadJsonDataImporter jsonImporter = new BadJsonDataImporter();
        jsonImporter.importData("{\"id\": \"3\", \"name\": \"Charlie\", \"email\": \"charlie@test.com\"}");
    }
}
