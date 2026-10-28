package com.systemdesign.lld.patterns.proxy.example01.good;

public class GalleryDemo {
    public static void main(String[] args) {
        ImageGalleryService galleryService = new ImageGalleryService();

        // 1. Initializing gallery using lightweight proxies (instantaneous)
        System.out.println("Step 1: Instantiating album with 3 virtual image proxies:");
        galleryService.addImage(new ProxyImage("aurora_borealis_8k.raw", 60_000_000));
        galleryService.addImage(new ProxyImage("milky_way_deep_space.raw", 85_000_000));
        galleryService.addImage(new ProxyImage("sahara_dunes_hdr.raw", 40_000_000));

        // 2. Rendering album list — no images loaded in memory yet
        System.out.println(galleryService.renderGalleryPlaceholders());

        // 3. User views image 1 on screen
        System.out.println("Step 2: User taps on image [1]:");
        System.out.println(galleryService.viewImage(1));
        System.out.println();

        // 4. Album status now shows image 1 is loaded, others still unloaded
        System.out.println("Step 3: Checking gallery status after viewing one image:");
        System.out.println(galleryService.renderGalleryPlaceholders());

        // 5. Viewing image 1 a second time reuses cached instance (no re-reading from disk)
        System.out.println("Step 4: Viewing image [1] again (notice no disk read log):");
        System.out.println(galleryService.viewImage(1));
    }
}
