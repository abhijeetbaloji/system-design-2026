package com.systemdesign.lld.patterns.proxy.example01;

import com.systemdesign.lld.patterns.proxy.example01.good.ImageGalleryService;
import com.systemdesign.lld.patterns.proxy.example01.good.ProxyImage;
import com.systemdesign.lld.patterns.proxy.example01.good.RealHighResolutionImage;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Good Design: Virtual Proxy Lazy Image Loading Tests")
class VirtualProxyImageTest {

    @Test
    @DisplayName("ProxyImage should not instantiate RealHighResolutionImage upon creation")
    void testLazyInstantiation() {
        ProxyImage proxy = new ProxyImage("sunset.raw", 45_000_000);

        assertEquals("sunset.raw", proxy.getFileName());
        assertEquals(45_000_000, proxy.getFileSizeBytes());
        assertFalse(proxy.isLoaded());
        assertNull(proxy.getRealImage());
    }

    @Test
    @DisplayName("Calling display() should trigger RealHighResolutionImage creation exactly once")
    void testDisplayLoadsImage() {
        ProxyImage proxy = new ProxyImage("mountain.raw", 30_000_000);

        String render1 = proxy.display();
        assertNotNull(render1);
        assertTrue(render1.contains("mountain.raw"));
        assertTrue(proxy.isLoaded());
        assertNotNull(proxy.getRealImage());
        assertEquals(1, proxy.getRealImage().getLoadCount());

        // Subsequent call to display() reuses existing loaded instance
        String render2 = proxy.display();
        assertEquals(render1, render2);
        assertEquals(1, proxy.getRealImage().getLoadCount());
    }

    @Test
    @DisplayName("ImageGalleryService manages image placeholders and lazy access")
    void testGalleryService() {
        ImageGalleryService service = new ImageGalleryService();
        ProxyImage img1 = new ProxyImage("img1.raw", 10_000_000);
        ProxyImage img2 = new ProxyImage("img2.raw", 20_000_000);

        service.addImage(img1);
        service.addImage(img2);

        assertEquals(2, service.getGallery().size());
        assertFalse(img1.isLoaded());
        assertFalse(img2.isLoaded());

        String viewOutput = service.viewImage(0);
        assertTrue(viewOutput.contains("img1.raw"));

        assertTrue(img1.isLoaded());
        assertFalse(img2.isLoaded()); // img2 remains unloaded
    }

    @Test
    @DisplayName("Invalid gallery index throws IndexOutOfBoundsException")
    void testInvalidIndex() {
        ImageGalleryService service = new ImageGalleryService();
        assertThrows(IndexOutOfBoundsException.class, () -> service.viewImage(5));
    }

    @Test
    @DisplayName("Null file name in ProxyImage throws NullPointerException")
    void testNullValidation() {
        assertThrows(NullPointerException.class, () -> new ProxyImage(null, 100));
    }
}
