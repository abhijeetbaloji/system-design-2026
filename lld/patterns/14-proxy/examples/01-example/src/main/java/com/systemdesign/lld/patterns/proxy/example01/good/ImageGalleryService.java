package com.systemdesign.lld.patterns.proxy.example01.good;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Service demonstrating client-side consumption of images via the Image interface.
 */
public class ImageGalleryService {
    private final List<Image> gallery = new ArrayList<>();

    public void addImage(Image image) {
        gallery.add(Objects.requireNonNull(image, "Image cannot be null"));
    }

    public String renderGalleryPlaceholders() {
        StringBuilder sb = new StringBuilder();
        sb.append("========================================\n");
        sb.append("         PHOTO ALBUM GALLERY            \n");
        sb.append("========================================\n");
        for (int i = 0; i < gallery.size(); i++) {
            Image img = gallery.get(i);
            String status = img.isLoaded() ? "[LOADED IN RAM]" : "[PLACEHOLDER / UNLOADED]";
            sb.append("[").append(i).append("] ")
              .append(img.getFileName())
              .append(" (~").append(img.getFileSizeBytes() / 1_000_000).append(" MB) ")
              .append(status).append("\n");
        }
        sb.append("========================================\n");
        return sb.toString();
    }

    public String viewImage(int index) {
        if (index < 0 || index >= gallery.size()) {
            throw new IndexOutOfBoundsException("Invalid gallery index: " + index);
        }
        return gallery.get(index).display();
    }

    public List<Image> getGallery() {
        return Collections.unmodifiableList(gallery);
    }
}
