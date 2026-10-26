package good;

import java.io.IOException;
import java.util.Objects;

/**
 * Base Decorator class for data source pipelines.
 *
 * Implements the DataSource interface and maintains a wrapped DataSource reference.
 * Provides default pass-through delegation for all interface methods.
 */
public abstract class DataSourceDecorator implements DataSource {
    protected final DataSource wrappee;

    protected DataSourceDecorator(DataSource wrappee) {
        this.wrappee = Objects.requireNonNull(wrappee, "Wrappee DataSource cannot be null");
    }

    @Override
    public void writeData(byte[] data) throws IOException {
        wrappee.writeData(data);
    }

    @Override
    public byte[] readData() throws IOException {
        return wrappee.readData();
    }
}
