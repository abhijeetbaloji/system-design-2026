package good;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;

/**
 * Concrete Component that reads and writes raw bytes to a disk file.
 * Has zero knowledge of encryption, compression, or encoding.
 */
public class FileDataSource implements DataSource {
    private final Path path;

    public FileDataSource(Path path) {
        this.path = Objects.requireNonNull(path, "File path cannot be null");
    }

    @Override
    public void writeData(byte[] data) throws IOException {
        Files.write(path, data);
    }

    @Override
    public byte[] readData() throws IOException {
        if (!Files.exists(path)) {
            return new byte[0];
        }
        return Files.readAllBytes(path);
    }

    public Path getPath() {
        return path;
    }
}
