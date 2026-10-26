package good;

import java.io.IOException;
import java.util.Objects;

/**
 * Concrete Decorator that applies symmetric encryption to data streams.
 *
 * Responsibilities:
 * - writeData: Encrypts plain bytes before passing to the next decorator or storage.
 * - readData: Retrieves ciphertext from the wrappee and decrypts it into plain bytes.
 */
public class EncryptionDecorator extends DataSourceDecorator {
    private final byte[] secretKey;

    public EncryptionDecorator(DataSource wrappee, byte[] secretKey) {
        super(wrappee);
        this.secretKey = Objects.requireNonNull(secretKey, "Secret key cannot be null");
        if (secretKey.length == 0) {
            throw new IllegalArgumentException("Secret key cannot be empty");
        }
    }

    @Override
    public void writeData(byte[] data) throws IOException {
        byte[] encrypted = transform(data);
        super.writeData(encrypted);
    }

    @Override
    public byte[] readData() throws IOException {
        byte[] encrypted = super.readData();
        return transform(encrypted);
    }

    private byte[] transform(byte[] input) {
        byte[] output = new byte[input.length];
        for (int i = 0; i < input.length; i++) {
            output[i] = (byte) (input[i] ^ secretKey[i % secretKey.length]);
        }
        return output;
    }
}
