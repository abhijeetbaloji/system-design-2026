package com.systemdesign.lld.patterns.facade.example03.good;

/**
 * Facade Class:
 * Simplifies media transcoding pipeline by wrapping VideoFile, CodecFactory,
 * BitrateReader, and AudioMixer into a straightforward single method call.
 */
public class MediaConverterFacade {

    private final CodecFactory codecFactory;
    private final BitrateReader bitrateReader;
    private final AudioMixer audioMixer;

    public MediaConverterFacade(CodecFactory codecFactory,
                                BitrateReader bitrateReader,
                                AudioMixer audioMixer) {
        this.codecFactory = codecFactory != null ? codecFactory : new CodecFactory();
        this.bitrateReader = bitrateReader != null ? bitrateReader : new BitrateReader();
        this.audioMixer = audioMixer != null ? audioMixer : new AudioMixer();
    }

    public MediaConverterFacade() {
        this(new CodecFactory(), new BitrateReader(), new AudioMixer());
    }

    public MediaConversionResult convertMedia(String sourceFileName, String targetFormat) {
        if (sourceFileName == null || !sourceFileName.contains(".")) {
            return MediaConversionResult.fail(sourceFileName, targetFormat, "Invalid file name");
        }
        if (targetFormat == null || targetFormat.isBlank()) {
            return MediaConversionResult.fail(sourceFileName, targetFormat, "Target format cannot be empty");
        }

        System.out.println("=== [MediaConverterFacade] Starting conversion: " + sourceFileName + " -> " + targetFormat + " ===");

        try {
            // 1. Create file model
            VideoFile file = new VideoFile(sourceFileName);

            // 2. Extract source codec & create destination codec
            Codec sourceCodec = codecFactory.extract(file);
            Codec destinationCodec = codecFactory.createCodec(targetFormat);

            // 3. Read raw stream & convert bitstream
            byte[] rawBuffer = bitrateReader.read(file, sourceCodec);
            byte[] convertedBuffer = bitrateReader.convert(rawBuffer, destinationCodec);

            // 4. Resample & synchronize audio
            byte[] finalOutput = audioMixer.fix(convertedBuffer);

            String baseName = sourceFileName.substring(0, sourceFileName.lastIndexOf("."));
            String outputFileName = baseName + "." + targetFormat.toLowerCase();

            System.out.println("=== [MediaConverterFacade] Conversion completed: " + outputFileName + " ===\n");
            return MediaConversionResult.ok(sourceFileName, outputFileName, targetFormat, finalOutput);
        } catch (Exception e) {
            System.err.println("[MediaConverterFacade] Conversion failed: " + e.getMessage());
            return MediaConversionResult.fail(sourceFileName, targetFormat, e.getMessage());
        }
    }
}
