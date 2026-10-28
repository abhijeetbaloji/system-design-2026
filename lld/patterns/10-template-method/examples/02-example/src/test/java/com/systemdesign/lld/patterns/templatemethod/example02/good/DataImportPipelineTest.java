package com.systemdesign.lld.patterns.templatemethod.example02.good;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Good DataImportPipeline Template Method Unit Tests")
class DataImportPipelineTest {

    @Test
    @DisplayName("Should parse CSV and persist only validated records while ensuring cleanup")
    void testCsvImportPipeline() {
        DataImportPipeline pipeline = new CsvDataImporter();
        String csv = """
                1, Alice, alice@test.com
                2, Bob, not-an-email
                3, Charlie, charlie@corp.com
                """;

        int importedCount = pipeline.executeImport(csv);

        // 2 valid records (Alice, Charlie)
        assertEquals(2, importedCount);
        assertEquals(2, pipeline.getDatabaseTable().size());
        assertTrue(pipeline.isResourceClosed());
        assertTrue(pipeline.getLifecycleAudit().stream().anyMatch(msg -> msg.contains("Dropped invalid record")));
    }

    @Test
    @DisplayName("Should parse JSON and execute onImportCompleted hook")
    void testJsonImportPipeline() {
        JsonDataImporter pipeline = new JsonDataImporter();
        String json = """
                {"id": "10", "name": "David", "email": "david@test.com"}
                """;

        int count = pipeline.executeImport(json);

        assertEquals(1, count);
        assertTrue(pipeline.isPostHookTriggered());
        assertTrue(pipeline.isResourceClosed());
    }

    @Test
    @DisplayName("Should handle empty payloads gracefully")
    void testEmptyPayload() {
        DataImportPipeline pipeline = new CsvDataImporter();
        int count = pipeline.executeImport("");
        assertEquals(0, count);
        assertEquals(0, pipeline.getDatabaseTable().size());
    }
}
