package com.systemdesign.lld.patterns.proxy.example01.bad;

import java.util.ArrayList;
import java.util.List;

public class BadImageGallery {
    private final List<BadHighResolutionImage> images = new ArrayList<>();

    public void addImage(String fileName, int sizeBytes) {
        // Eagerly allocates memory for every image when added to gallery
        images.add(new BadHighResolutionImage(fileName, sizeBytes));
    }

    public String viewImage(int index) {
        if (index < 0 || index >= images.size()) {
            throw new IndexOutOfBoundsException("Invalid index: " + index);
        }
        return images.get(index).display();
    }

    public int getTotalMemoryAllocated() {
        int total = 0;
        for (BadHighResolutionImage img : images) {
            total += img.getSizeBytes();
        }
        return total;
    }
}
