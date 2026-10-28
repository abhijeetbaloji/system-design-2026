package good;

/**
 * Domain record encapsulating the result of an export operation.
 */
public record ExportResult(
        String filename,
        String format,
        String mimeType,
        int byteSize,
        String checksumHex,
        byte[] rawBytes
) {}
