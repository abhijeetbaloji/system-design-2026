# Example 02: Data Source Compression and Encryption

## 1. The Problem

In storage and streaming architectures, raw payloads frequently require dynamic preprocessing before persistence and reverse transformation upon retrieval:
- **Compression:** Reduces storage consumption and network transfer bandwidth (e.g., GZIP/Deflate).
- **Encryption:** Secures sensitive data at rest and in transit (e.g., AES/XOR cipher).
- **Encoding:** Converts binary payloads into transport-safe formats (e.g., Base64).
- **Observability:** Tracks latency, throughput, and payload sizes via logging and metrics.

Moreover, these transformations must operate transparently across different storage targets (local disk files, in-memory buffers, cloud buckets, or network sockets).

---

## 2. Bad Design Analysis

### Flaw 1: The God-Class Monolith (`bad/MonolithicFileDataSource.java`)
A common design mistake is packing every transformation directly into the persistence class using boolean flags:

```java
public class MonolithicFileDataSource {
    private final boolean enableCompression;
    private final boolean enableEncryption;
    private final boolean enableBase64;
    // ...
    public void writeData(byte[] data) {
        if (enableCompression) { ... }
        if (enableEncryption) { ... }
        if (enableBase64) { ... }
        Files.write(path, result);
    }
}
```

#### Why This Fails:
1. **Severe SRP Violation:** The class couples raw filesystem I/O with cryptography, deflate algorithms, and encoding schemes.
2. **Fragile OCP Violation:** Adding checksum verification (e.g., CRC32/SHA-256) requires editing this class and adding yet another flag.
3. **Zero Medium Reusability:** If you need an in-memory or socket-based data source, you must duplicate all compression and encryption logic.
4. **Rigid Hardcoded Pipeline:** The execution order (Compress $\rightarrow$ Encrypt $\rightarrow$ Encode) is burned into the code. The client cannot invert or selectively reconfigure steps.

### Flaw 2: Combinatorial Subclass Explosion
Attempting to replace boolean flags with inheritance leads to:
- `FileDataSource`
- `EncryptedFileDataSource`
- `CompressedFileDataSource`
- `CompressedAndEncryptedFileDataSource`
- `EncryptedAndCompressedFileDataSource` (when ordering varies!)

For $N$ transformation steps across $M$ storage media, static inheritance requires $M \times \sum_{k=1}^N \frac{N!}{(N-k)!}$ classes.

---

## 3. Improved Design (Decorator Pattern)

The improved design in `good/` isolates persistence targets from stream transformations using recursive wrappers:

```
new LoggingDataSourceDecorator(
    new Base64EncodingDecorator(
        new EncryptionDecorator(
            new CompressionDecorator(
                new FileDataSource(path)
            ),
            secretKey
        )
    ),
    "Client-Pipeline"
)
```

### Participants
- **`DataSource` (Component Interface):** Declares symmetric `void writeData(byte[] data)` and `byte[] readData()`.
- **`FileDataSource` & `MemoryDataSource` (Concrete Components):** Store raw bytes to disk or memory with zero transformation awareness.
- **`DataSourceDecorator` (Abstract Decorator):** Implements `DataSource`, holds `protected final DataSource wrappee`, and provides pass-through delegation.
- **`CompressionDecorator`, `EncryptionDecorator`, `Base64EncodingDecorator`, `LoggingDataSourceDecorator` (Concrete Decorators):** Encapsulate individual transformations.

---

## 4. Execution Flow: Symmetric Forward and Reverse Pipelines

A key property of stream decorators is **symmetry**: writes apply transformations moving *inward* toward the leaf component, while reads unwind transformations moving *outward* toward the client.

### Write Pipeline (Moving Inward)
```
Client: writeData(plainBytes)
  │
  ▼
[LoggingDecorator]       Logs write start and captures latency
  │ (plainBytes)
  ▼
[Base64Decorator]        Encodes binary to Base64 ASCII bytes
  │ (base64Bytes)
  ▼
[EncryptionDecorator]    Applies XOR/AES cipher to bytes
  │ (cipherBytes)
  ▼
[CompressionDecorator]   Compresses bytes with Deflater
  │ (compressedBytes)
  ▼
[FileDataSource]         Writes final bytes to disk
```

### Read Pipeline (Unwinding Outward)
```
[FileDataSource]         Reads raw bytes from disk
  │ (compressedBytes)
  ▼
[CompressionDecorator]   Decompresses with Inflater
  │ (cipherBytes)
  ▼
[EncryptionDecorator]    Decrypts with XOR/AES cipher
  │ (base64Bytes)
  ▼
[Base64Decorator]        Decodes Base64 to binary
  │ (plainBytes)
  ▼
[LoggingDecorator]       Logs read completion and elapsed time
  │
  ▼
Client: receives original plainBytes
```

---

## 5. Architectural Insight: Why Decorator Ordering Matters

Decorator order is **non-commutative** in transformation pipelines:

1. **Compress THEN Encrypt (Correct):** Plaintext contains patterns, repetitive words, and predictable distributions. Compression algorithms exploit this redundancy to drastically shrink payload size. The subsequent encryption layer securely scrambles the compressed output.
2. **Encrypt THEN Compress (Anti-pattern):** Modern cryptographic ciphers produce pseudorandom ciphertext with near-maximum information entropy. Because there is virtually no pattern or redundancy in ciphertext, Deflate/GZIP cannot compress it and may even slightly *increase* file size due to container overhead!

---

## 6. Relationship to SOLID Principles

- **Single Responsibility Principle (SRP):** Each decorator class encapsulates a single algorithm (`CompressionDecorator` only touches Deflater/Inflater; `FileDataSource` only touches `java.nio.file.Files`).
- **Open/Closed Principle (OCP):** Adding a `ChecksumDecorator` or `RateLimitingDecorator` requires no edits to existing components or decorators.
- **Liskov Substitution Principle (LSP):** `MemoryDataSource` can be substituted for `FileDataSource` under the exact same decorator stack without altering client behavior.
- **Dependency Inversion Principle (DIP):** Decorators depend on the `DataSource` abstraction, enabling them to wrap any concrete or decorated data source.

---

## 7. How to Identify Incorrect Implementations

- **Asymmetric Transformation:** A decorator that transforms data during `writeData()` but forgets to invert the transformation in `readData()`.
- **Breaking Interface Return Types:** A decorator returning `String` or custom wrapper objects instead of `byte[]`, breaking uniform nesting.
- **State Leaks Across Concurrent Invocations:** If a decorator stores mutable buffer state without thread synchronization, sharing a decorator instance across threads will corrupt streams.
