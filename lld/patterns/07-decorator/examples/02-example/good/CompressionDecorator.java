package good;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

/**
 * Concrete Decorator that applies Deflate/Inflate compression.
 *
 * Responsibilities:
 * - writeData: Compresses payload before delegating down the chain.
 * - readData: Fetches compressed bytes from wrappee and decompresses them.
 */
public class CompressionDecorator extends DataSourceDecorator {
    private final int compressionLevel;

    public CompressionDecorator(DataSource wrappee) {
        this(wrappee, Deflater.DEFAULT_COMPRESSION);
    }

    public CompressionDecorator(DataSource wrappee, int compressionLevel) {
        super(wrappee);
        this.compressionLevel = compressionLevel;
    }

    @Override
    public void writeData(byte[] data) throws IOException {
        byte[] compressed = compress(data);
        super.writeData(compressed);
    }

    @Override
    public byte[] readData() throws IOException {
        byte[] compressed = super.readData();
        if (compressed.length == 0) {
            return compressed;
        }
        return decompress(compressed);
    }

    private byte[] compress(byte[] input) {
        Deflater deflater = new Deflater(compressionLevel);
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

    private byte[] decompress(byte[] input) throws IOException {
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
            throw new IOException("Failed to decompress payload", e);
        }
    }
}
