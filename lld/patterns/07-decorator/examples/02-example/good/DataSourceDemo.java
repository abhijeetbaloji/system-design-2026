package good;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public class DataSourceDemo {
    public static void main(String[] args) throws IOException {
        System.out.println("=== Data Source Pipeline - Decorator Pattern Demo ===\n");

        Path tempFile = Files.createTempFile("good_datasource_", ".bin");
        tempFile.toFile().deleteOnExit();

        byte[] secretKey = "SuperSecretKey2026".getBytes(StandardCharsets.UTF_8);
        String payload = "System Design 2026: The Decorator Pattern attaches dynamic responsibilities "
                       + "to objects transparently through recursive composition and delegation!";
        byte[] originalData = payload.getBytes(StandardCharsets.UTF_8);

        System.out.printf("Original plain data size: %d bytes%n", originalData.length);
        System.out.printf("Original text: %s%n%n", payload);

        // --- Scenario 1: Full Pipeline (Logging -> Base64 -> Encryption -> Compression -> FileDataSource) ---
        System.out.println("--- Scenario 1: Writing through layered decorators to disk ---");

        // The pipeline is constructed from the inside out:
        // Core component: FileDataSource
        // Layer 1: Compression (reduces payload size)
        // Layer 2: Encryption (secures compressed bytes)
        // Layer 3: Base64Encoding (produces printable ASCII ciphertext)
        // Layer 4: Logging (telemetry at client entry point)
        DataSource pipeline = new LoggingDataSourceDecorator(
                new Base64EncodingDecorator(
                        new EncryptionDecorator(
                                new CompressionDecorator(
                                        new FileDataSource(tempFile)
                                ),
                                secretKey
                        )
                ),
                "Client-Pipeline"
        );

        pipeline.writeData(originalData);

        // Inspect physical bytes written to disk
        byte[] rawDiskBytes = Files.readAllBytes(tempFile);
        System.out.printf("%nStored on disk (%d bytes): %s%n",
                rawDiskBytes.length, new String(rawDiskBytes, StandardCharsets.UTF_8));

        // Read data back through the exact same decorator stack (unwinding in reverse)
        System.out.println("\n--- Reading through layered decorators from disk ---");
        byte[] recoveredData = pipeline.readData();
        String recoveredText = new String(recoveredData, StandardCharsets.UTF_8);
        System.out.printf("Recovered text: %s%n", recoveredText);
        System.out.printf("Fidelity check passed: %b%n%n", payload.equals(recoveredText));

        // --- Scenario 2: Reusing the same decorators with MemoryDataSource (LSP & DIP) ---
        System.out.println("--- Scenario 2: Swapping storage to MemoryDataSource with zero code changes ---");
        MemoryDataSource memoryStorage = new MemoryDataSource();

        // Exactly the same transformation layers wrapping an in-memory buffer instead of a file!
        DataSource memoryPipeline = new Base64EncodingDecorator(
                new EncryptionDecorator(
                        new CompressionDecorator(memoryStorage),
                        secretKey
                )
        );

        memoryPipeline.writeData(originalData);
        System.out.printf("Memory buffer size: %d bytes%n", memoryStorage.size());
        byte[] memoryRecovered = memoryPipeline.readData();
        System.out.printf("Memory recovery passed: %b%n",
                payload.equals(new String(memoryRecovered, StandardCharsets.UTF_8)));

        // --- Scenario 3: Architectural Insight on Decorator Ordering ---
        System.out.println("\n--- Scenario 3: Decorator Ordering Significance ---");
        System.out.println("Decorator order matters in non-commutative pipelines:");
        System.out.println("  1. Compress THEN Encrypt: High compression ratio because plaintext has redundancy.");
        System.out.println("  2. Encrypt THEN Compress: Ciphertext has near-maximum entropy (pseudo-random bytes),");
        System.out.println("     meaning GZIP/Deflate CANNOT compress it effectively!");
    }
}
