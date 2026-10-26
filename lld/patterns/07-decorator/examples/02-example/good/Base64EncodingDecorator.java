package good;

import java.io.IOException;
import java.util.Base64;

/**
 * Concrete Decorator that applies Base64 encoding/decoding.
 *
 * Responsibilities:
 * - writeData: Encodes raw binary data into ASCII-safe Base64 bytes.
 * - readData: Decodes Base64 bytes back into binary data.
 */
public class Base64EncodingDecorator extends DataSourceDecorator {

    public Base64EncodingDecorator(DataSource wrappee) {
        super(wrappee);
    }

    @Override
    public void writeData(byte[] data) throws IOException {
        byte[] encoded = Base64.getEncoder().encode(data);
        super.writeData(encoded);
    }

    @Override
    public byte[] readData() throws IOException {
        byte[] encoded = super.readData();
        if (encoded.length == 0) {
            return encoded;
        }
        return Base64.getDecoder().decode(encoded);
    }
}
