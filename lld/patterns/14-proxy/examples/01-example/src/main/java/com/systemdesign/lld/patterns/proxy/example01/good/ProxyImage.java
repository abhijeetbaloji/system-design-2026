package com.systemdesign.lld.patterns.proxy.example01.good;

import java.util.Objects;

/**
 * Virtual Proxy providing lightweight metadata initially and delaying heavy instantiation until display().
 */
public class ProxyImage implements Image {
    private final String fileName;
    private final long estimatedSize;
    private volatile RealHighResolutionImage realImage;

    public ProxyImage(String fileName, long estimatedSize) {
        this.fileName = Objects.requireNonNull(fileName, "File name cannot be null");
        this.estimatedSize = estimatedSize;
        this.realImage = null; // Lightweight creation; zero I/O
    }

    @Override
    public String display() {
        // Thread-safe double-checked locking lazy instantiation
        if (realImage == null) {
            synchronized (this) {
                if (realImage == null) {
                    System.out.println("[VIRTUAL PROXY] First display request. Instantiating RealHighResolutionImage for: " + fileName);
                    this.realImage = new RealHighResolutionImage(fileName, estimatedSize);
                }
            }
        }
        return realImage.display();
    }

    @Override
    public String getFileName() {
        return fileName;
    }

    @Override
    public long getFileSizeBytes() {
        return realImage != null ? realImage.getFileSizeBytes() : estimatedSize;
    }

    @Override
    public boolean isLoaded() {
        return realImage != null && realImage.isLoaded();
    }

    public RealHighResolutionImage getRealImage() {
        return realImage;
    }
}
