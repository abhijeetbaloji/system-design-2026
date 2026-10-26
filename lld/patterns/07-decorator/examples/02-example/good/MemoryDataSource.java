package good;

import java.util.Arrays;

/**
 * Concrete Component that stores data in an in-memory byte buffer.
 *
 * Demonstrates that decorators work uniformly across any DataSource implementation,
 * adhering to the Liskov Substitution Principle and Dependency Inversion Principle.
 */
public class MemoryDataSource implements DataSource {
    private byte[] buffer = new byte[0];

    @Override
    public void writeData(byte[] data) {
        this.buffer = Arrays.copyOf(data, data.length);
    }

    @Override
    public byte[] readData() {
        return Arrays.copyOf(buffer, buffer.length);
    }

    public int size() {
        return buffer.length;
    }
}
