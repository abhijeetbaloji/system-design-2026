package bad;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public class BadDataSourceDemo {
    public static void main(String[] args) throws IOException {
        System.out.println("=== Bad Data Source Design Demo ===");

        Path tempFile = Files.createTempFile("bad_datasource_", ".bin");
        tempFile.toFile().deleteOnExit();

        String payload = "System Design 2026: Confidential Architectural Blueprint";
        byte[] originalBytes = payload.getBytes(StandardCharsets.UTF_8);

        // Client is forced to pass boolean flags or choose a rigid subclass
        MonolithicFileDataSource ds = new MonolithicFileDataSource(
                tempFile,
                true,  // compression
                true,  // encryption
                true   // base64
        );

        ds.writeData(originalBytes);
        byte[] storedOnDisk = Files.readAllBytes(tempFile);
        System.out.println("Stored on disk (encrypted, compressed, base64): "
                + new String(storedOnDisk, StandardCharsets.UTF_8));

        byte[] recoveredBytes = ds.readData();
        System.out.println("Recovered text: "
                + new String(recoveredBytes, StandardCharsets.UTF_8));

        System.out.println("\nFlaws identified:");
        System.out.println("1. MonolithicFileDataSource combines file I/O, encryption, deflater, and base64.");
        System.out.println("2. Cannot use these transformations with an in-memory buffer or network socket.");
        System.out.println("3. Cannot reorder transformations without changing the class source code.");
    }
}
