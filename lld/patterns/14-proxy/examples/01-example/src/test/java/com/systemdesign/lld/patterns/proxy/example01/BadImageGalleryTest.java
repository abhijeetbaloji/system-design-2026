package com.systemdesign.lld.patterns.proxy.example01;

import com.systemdesign.lld.patterns.proxy.example01.bad.BadHighResolutionImage;
import com.systemdesign.lld.patterns.proxy.example01.bad.BadImageGallery;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Bad Design: Eager High-Resolution Image Loading Tests")
class BadImageGalleryTest {

    @Test
    @DisplayName("BadImageGallery eagerly allocates full memory buffers upon addition")
    void testEagerMemoryAllocation() {
        BadImageGallery gallery = new BadImageGallery();
        gallery.addImage("pic1.raw", 5000);
        gallery.addImage("pic2.raw", 10000);

        assertEquals(15000, gallery.getTotalMemoryAllocated());

        String view = gallery.viewImage(0);
        assertTrue(view.contains("pic1.raw"));
    }

    @Test
    @DisplayName("Invalid index in bad gallery throws IndexOutOfBoundsException")
    void testInvalidIndex() {
        BadImageGallery gallery = new BadImageGallery();
        assertThrows(IndexOutOfBoundsException.class, () -> gallery.viewImage(0));
    }
}
