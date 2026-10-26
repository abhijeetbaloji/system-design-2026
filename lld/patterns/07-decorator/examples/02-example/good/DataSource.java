package good;

import java.io.IOException;

/**
 * Component interface.
 * Defines the contract for read and write data streaming.
 *
 * Concrete storage providers (files, memory buffers) and processing
 * decorators (compression, encryption, encoding) all conform to this interface.
 */
public interface DataSource {
    void writeData(byte[] data) throws IOException;
    byte[] readData() throws IOException;
}
