# Lazy Image Loading Virtual Proxy Example

## 1. Scenario Overview

A photo gallery application (like Google Photos, Flickr, or an e-commerce catalog) displays hundreds of high-resolution RAW or 4K images.
- Each full-resolution image requires 20MB to 100MB of RAM once loaded from disk or cloud storage.
- If the application initializes full `RealHighResolutionImage` instances for all 100 images upon opening the album, it consumes 5GB of memory and stalls the main UI thread for several seconds while disk I/O occurs.
- In reality, the user only scrolls and views 2 or 3 images on their screen at any given time.

A **Virtual Proxy** (`ProxyImage`) solves this:
- The gallery instantiates lightweight `ProxyImage` placeholders holding only lightweight metadata (filename, dimension, placeholder icon).
- Memory footprint during initial load is virtually zero (a few kilobytes).
- When the user scrolls to an image and `display()` is called, the `ProxyImage` lazily loads the `RealHighResolutionImage` from disk, caches the instance, and renders the pixels. Subsequent calls to `display()` reuse the already loaded instance.

---

## 2. Bad Design vs Good Design

### Bad Design (Eager Heavy Instantiation)
- `BadHighResolutionImage` loads raw byte buffers from disk eagerly in its constructor.
- Instantiating a gallery of 50 images causes 50 heavy file read operations and consumes gigabytes of memory immediately.
- If the user closes the app after 5 seconds, all that disk I/O was completely wasted.

### Good Design (Virtual Proxy Pattern)
- `Image` interface defines `display()`, `getFileName()`, `getFileSizeBytes()`, and `isLoaded()`.
- `RealHighResolutionImage` performs the expensive disk read only when constructed.
- `ProxyImage` implements `Image`, holding a `null` reference to `RealHighResolutionImage` until `display()` is invoked.
- `ProxyImage` uses thread-safe double-checked locking to ensure concurrent rendering does not trigger duplicate disk reads.

---

## 3. Class Structure

```
com.systemdesign.lld.patterns.proxy.example01
├── bad
│   ├── BadGalleryDemo.java
│   ├── BadHighResolutionImage.java
│   └── BadImageGallery.java
└── good
    ├── GalleryDemo.java
    ├── Image.java
    ├── ImageGalleryService.java
    ├── ProxyImage.java
    └── RealHighResolutionImage.java
```

---

## 4. Execution Flow & SOLID Principles

- **On-Demand Loading**: `new ProxyImage("nature.raw", 50_000_000)` executes instantaneously. Calling `display()` triggers the disk read on first invocation. Subsequent calls execute immediately using the cached `RealHighResolutionImage`.
- **Single Responsibility Principle (SRP)**: `RealHighResolutionImage` manages pixel processing; `ProxyImage` manages lazy instantiation and caching lifecycle.
- **Open/Closed Principle (OCP)**: You can introduce caching proxies or thumbnail generation proxies without modifying `RealHighResolutionImage`.
