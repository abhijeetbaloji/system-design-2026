package com.systemdesign.lld.patterns.facade.example03.bad;

/**
 * BAD DESIGN:
 * Client directly interacts with multiple low-level video transcoding classes:
 * VideoFile, CodecFactory, Codec, BitrateReader, AudioMixer.
 *
 * Flaws:
 * 1. Excessive Knowledge (Violates Law of Demeter):
 *    Client must know how to inspect codecs, extract streams, transcode byte arrays,
 *    and resample audio in exact sequence.
 * 2. High Fragility:
 *    If caller forgets AudioMixer, the resulting video is silent or corrupted.
 * 3. Cannot easily optimize or replace internal codecs.
 */
public class BadMediaClient {

    public byte[] convertVideo(String fileName, String targetFormat) {
        System.out.println("Beginning conversion of " + fileName + " to " + targetFormat);
        VideoFile file = new VideoFile(fileName);
        Codec sourceCodec = CodecFactory.extract(file);

        Codec destinationCodec;
        if ("mp4".equalsIgnoreCase(targetFormat)) {
            destinationCodec = new MPEG4CompressionCodec();
        } else {
            destinationCodec = new OggCompressionCodec();
        }

        byte[] rawStream = BitrateReader.read(file, sourceCodec);
        byte[] convertedBuffer = BitrateReader.convert(rawStream, destinationCodec);

        // Required step easily forgotten by callers!
        AudioMixer mixer = new AudioMixer();
        byte[] finalResult = mixer.fix(convertedBuffer);

        System.out.println("Conversion completed successfully.");
        return finalResult;
    }
}
