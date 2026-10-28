package bad;

import java.nio.file.Path;

/**
 * BAD DESIGN: Alternative subclass explosion attempt.
 *
 * Developers try to avoid boolean flags by creating rigid subclasses:
 * - FileDataSource
 * - EncryptedFileDataSource
 * - CompressedFileDataSource
 * - CompressedAndEncryptedFileDataSource
 * - EncryptedAndCompressedFileDataSource (order matters!)
 *
 * For N pipeline steps, subclassing requires N! * 2^N combinations.
 */
public class CompressedAndEncryptedFileDataSource extends MonolithicFileDataSource {
    public CompressedAndEncryptedFileDataSource(Path path) {
        // Hardcoded boolean configuration
        super(path, true, true, false);
    }
}
