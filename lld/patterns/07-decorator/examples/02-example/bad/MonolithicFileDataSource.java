package bad;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Base64;
import java.util.zip.Deflater;
import java.util.zip.Inflater;
import java.io.ByteArrayOutputStream;

/**
 * BAD DESIGN: Monolithic FileDataSource attempting to handle file persistence,
 * compression, encryption, and base64 encoding in a single god-class.
 *
 * Flaws:
 * 1. Violates SRP: File reading/writing is tangled with compression algorithms,
 *    crypto keys, and encoding rules.
 * 2. Violates OCP: Adding a new step (e.g., Checksum, Gzip, AES) requires modifying
 *    this class and adding more boolean flags.
 * 3. Rigid Pipeline: Order of operations is hardcoded. You cannot choose to
 *    encrypt before compressing, or compress without encoding.
 * 4. Zero Reusability: If you need an In-Memory DataSource or S3 DataSource,
 *    all this transformation logic must be copy-pasted.
 */
public class MonolithicFileDataSource {
    private final Path filePath;
    private final boolean enableCompression;
    private final boolean enableEncryption;
    private final boolean enableBase64;
    private final byte xorKey;

    public MonolithicFileDataSource(Path filePath, boolean enableCompression,
                                    boolean enableEncryption, boolean enableBase64) {
        this.filePath = filePath;
        this.enableCompression = enableCompression;
        this.enableEncryption = enableEncryption;
        this.enableBase64 = enableBase64;
        this.xorKey = (byte) 0x5A;
    }

    public void writeData(byte[] data) throws IOException {
        byte[] result = data;

        // 1. Hardcoded compression step
        if (enableCompression) {
            result = compress(result);
        }

        // 2. Hardcoded encryption step
        if (enableEncryption) {
            result = xorTransform(result);
        }

        // 3. Hardcoded Base64 encoding step
        if (enableBase64) {
            result = Base64.getEncoder().encode(result);
        }

        // 4. File I/O
        Files.write(filePath, result);
    }

    public byte[] readData() throws IOException {
        byte[] raw = Files.readAllBytes(filePath);

        // Reverse pipeline - hardcoded order
        if (enableBase64) {
            raw = Base64.getDecoder().decode(raw);
        }
        if (enableEncryption) {
            raw = xorTransform(raw);
        }
        if (enableCompression) {
            raw = decompress(raw);
        }
        return raw;
    }

    private byte[] xorTransform(byte[] input) {
        byte[] output = new byte[input.length];
        for (int i = 0; i < input.length; i++) {
            output[i] = (byte) (input[i] ^ xorKey);
        }
        return output;
    }

    private byte[] compress(byte[] input) {
        Deflater deflater = new Deflater();
        deflater.setInput(input);
        deflater.finish();
        ByteArrayOutputStream bos = new ByteArrayOutputStream(input.length);
        byte[] buffer = new byte[1024];
        while (!deflater.finished()) {
            int count = deflater.deflate(buffer);
            bos.write(buffer, 0, count);
        }
        deflater.end();
        return bos.toByteArray();
    }

    private byte[] decompress(byte[] input) {
        Inflater inflater = new Inflater();
        inflater.setInput(input);
        ByteArrayOutputStream bos = new ByteArrayOutputStream(input.length);
        byte[] buffer = new byte[1024];
        try {
            while (!inflater.finished()) {
                int count = inflater.inflate(buffer);
                bos.write(buffer, 0, count);
            }
            inflater.end();
            return bos.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("Decompression failed", e);
        }
    }
}
