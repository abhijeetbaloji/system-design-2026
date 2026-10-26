package good;

import java.io.IOException;

/**
 * Concrete Decorator that adds telemetry and logging around I/O operations.
 *
 * Demonstrates an orthogonal cross-cutting concern applied cleanly via Decorator
 * without touching storage, encryption, or compression code.
 */
public class LoggingDataSourceDecorator extends DataSourceDecorator {
    private final String stageLabel;

    public LoggingDataSourceDecorator(DataSource wrappee, String stageLabel) {
        super(wrappee);
        this.stageLabel = stageLabel;
    }

    @Override
    public void writeData(byte[] data) throws IOException {
        System.out.printf("[LOG - %s] Writing payload (%d bytes)...%n", stageLabel, data.length);
        long start = System.nanoTime();
        super.writeData(data);
        long duration = System.nanoTime() - start;
        System.out.printf("[LOG - %s] Write completed in %.2f µs.%n", stageLabel, duration / 1000.0);
    }

    @Override
    public byte[] readData() throws IOException {
        System.out.printf("[LOG - %s] Reading payload...%n", stageLabel);
        long start = System.nanoTime();
        byte[] data = super.readData();
        long duration = System.nanoTime() - start;
        System.out.printf("[LOG - %s] Read completed (%d bytes) in %.2f µs.%n", stageLabel, data.length, duration / 1000.0);
        return data;
    }
}
