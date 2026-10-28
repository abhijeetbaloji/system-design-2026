package com.systemdesign.lld.patterns.proxy.example01.bad;

public class BadGalleryDemo {
    public static void main(String[] args) {
        BadImageGallery gallery = new BadImageGallery();

        System.out.println("Initializing Gallery (Notice immediate disk I/O and RAM spikes):");
        gallery.addImage("lake_tahoe_4k.raw", 20_000_000);
        gallery.addImage("yosemite_pano.raw", 45_000_000);
        gallery.addImage("alps_summit.raw", 35_000_000);

        System.out.println("Total RAM locked: " + (gallery.getTotalMemoryAllocated() / 1_000_000) + " MB");
        System.out.println("User views only image 0:");
        System.out.println(gallery.viewImage(0));
    }
}
