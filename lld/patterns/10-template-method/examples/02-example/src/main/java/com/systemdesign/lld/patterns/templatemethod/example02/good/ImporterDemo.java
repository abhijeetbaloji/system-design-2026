package com.systemdesign.lld.patterns.templatemethod.example02.good;

public class ImporterDemo {
    public static void main(String[] args) {
        System.out.println("--- 1. Importing CSV Data ---");
        DataImportPipeline csvPipeline = new CsvDataImporter();
        String csvData = """
                USR-1, Sarah Connor, sarah@skynet.com
                USR-2, John Connor, john@resistance.org
                USR-3, T-800, invalid-email
                """;
        int importedCsv = csvPipeline.executeImport(csvData);
        System.out.println("Imported valid CSV records: " + importedCsv);

        System.out.println("\n--- 2. Importing JSON Data ---");
        DataImportPipeline jsonPipeline = new JsonDataImporter();
        String jsonData = """
                {"id": "USR-4", "name": "Kyle Reese", "email": "kyle@resistance.org"}
                """;
        int importedJson = jsonPipeline.executeImport(jsonData);
        System.out.println("Imported valid JSON records: " + importedJson);
    }
}
