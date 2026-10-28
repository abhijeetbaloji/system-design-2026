package com.systemdesign.lld.patterns.proxy.example01.bad;

public class BadHighResolutionImage {
    private final String fileName;
    private final byte[] memoryBuffer;

    public BadHighResolutionImage(String fileName, int sizeBytes) {
        this.fileName = fileName;
        // Flawed: Heavy simulated disk I/O and large heap allocation directly in constructor
        System.out.println("[EAGER LOADING] Reading " + sizeBytes + " bytes from disk for: " + fileName);
        this.memoryBuffer = new byte[sizeBytes];
    }

    public String display() {
        return "Displaying " + fileName + " (" + memoryBuffer.length + " bytes in RAM).";
    }

    public String getFileName() {
        return fileName;
    }

    public int getSizeBytes() {
        return memoryBuffer.length;
    }
}
