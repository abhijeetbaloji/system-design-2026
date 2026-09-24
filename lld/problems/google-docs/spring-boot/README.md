# Spring Boot Production-Oriented Implementation: Google Docs Service

## 1. How Plain Java LLD Maps to Spring Boot

The clean object-oriented architecture from `interview-ready` is adapted into an enterprise-ready Spring Boot service without compromising domain purity:
- **Clean Architecture / DDD Separation**: Core editing logic, operational transformations, revision sequencing, and visitors remain pure Java classes in the `domain` package. They have zero dependencies on Spring framework annotations.
- **Application Service Layer**: `DocumentAppService` wraps domain entities and sessions, managing transaction boundaries, DTO conversions, and exception translation.
- **HTTP / REST Boundary**: `DocumentController` exposes RESTful endpoints with validation annotations (`@Valid`, `@NotBlank`, `@NotNull`).
- **Persistence Boundary**: `DocumentRepository` abstracts storage. We provide an `InMemoryDocumentRepository` using thread-safe concurrent collections, allowing the service to run out-of-the-box while maintaining an swappable interface for JPA/MongoDB.

---

## 2. Spring Components vs. Domain Objects

### Spring-Managed Components (Beans)
- `GoogleDocsApplication`: Spring Boot bootstrap application entrypoint.
- `DocumentController` (`@RestController`): Exposes REST APIs, handles HTTP status codes.
- `DocumentAppService` (`@Service`): Orchestrates collaboration sessions, authorization, and repositories.
- `InMemoryDocumentRepository` (`@Repository`): In-memory thread-safe storage implementation of `DocumentRepository`.
- `GlobalExceptionHandler` (`@RestControllerAdvice`): Translates domain/validation exceptions into standardized JSON responses.

### Pure Domain Objects (Unmanaged by Spring Container)
- `Document`: Aggregate root managing paragraphs, text runs, locking, and revisions.
- `Paragraph`, `TextRun`, `TextStyle`: Rich content composite structure.
- `Operation`, `InsertOperation`, `DeleteOperation`, `FormatOperation`: Mutation commands.
- `OperationTransformer`: Operational Transformation algorithm.
- `CollaborationSession`: Per-document collaboration hub and sequencer.
- `RevisionLog`, `CommittedOperation`: Audit history.
- `DocumentVisitor`, `PlainTextVisitor`, `MarkdownVisitor`, `HtmlVisitor`: Format exporters.

---

## 3. Dependency Injection (DI)

Constructor-based dependency injection is strictly utilized across all Spring components:
```java
@Service
public class DocumentAppService {
    private final DocumentRepository documentRepository;

    public DocumentAppService(DocumentRepository documentRepository) {
        this.documentRepository = documentRepository;
    }
}
```
Field injection (`@Autowired` on private fields) is avoided to guarantee immutability, simple testability, and clear dependency contracts.

---

## 4. Persistence Architecture

Persistence is decoupled via the `DocumentRepository` interface.
- **Current Implementation**: `InMemoryDocumentRepository` backed by a `ConcurrentHashMap<String, Document>`.
- **Reason**: The LLD problem focuses on in-memory data structures, OT concurrency, and domain behavior. An in-memory repository allows immediate execution and unit/integration testing without requiring an external PostgreSQL or Docker instance.
- **Production Extension**: Simply create a `JpaDocumentRepository implements DocumentRepository` using Spring Data JPA or MongoDB without changing a single line of business logic.

---

## 5. API Endpoints

| Method | Endpoint | Description | Status Code |
| :--- | :--- | :--- | :--- |
| `POST` | `/api/v1/documents` | Create a new document with an owner | `201 Created` |
| `GET` | `/api/v1/documents/{id}?userId={uid}` | Get current document snapshot | `200 OK` |
| `POST` | `/api/v1/documents/{id}/operations` | Submit an edit operation (Insert/Delete/Format) | `200 OK` |
| `POST` | `/api/v1/documents/{id}/share` | Share document with another user (Role: OWNER, EDITOR, VIEWER) | `204 No Content` |
| `GET` | `/api/v1/documents/{id}/export?userId={uid}&format={fmt}` | Export document to Markdown, HTML, or TXT | `200 OK` |
| `GET` | `/api/v1/documents/{id}/revisions?userId={uid}&sinceRev={rev}` | Fetch historical operations since a baseline revision | `200 OK` |

---

## 6. DTOs and Why They Exist

1. `CreateDocumentRequest`: Validates required input fields (`documentId`, `title`, `ownerId`) using Jakarta Bean Validation.
2. `DocumentResponse`: Decouples internal tree structures (`List<Paragraph>`) into a flat, consumer-friendly JSON payload.
3. `OperationRequest`: Accepts generic polymorphic client operations from JSON payloads.
4. `CommittedOperationResponse`: Returns the authoritative server revision number, transformed position, and commit timestamp.
5. `ShareDocumentRequest`: Enforces role typing during sharing.

---

## 7. Exception Handling

Handled centrally by `GlobalExceptionHandler` (`@RestControllerAdvice`):
- `DocumentNotFoundException` $\rightarrow$ `404 Not Found`
- `UnauthorizedAccessException` $\rightarrow$ `403 Forbidden`
- `InvalidOperationException` / `IllegalArgumentException` $\rightarrow$ `400 Bad Request`
- `MethodArgumentNotValidException` $\rightarrow$ `400 Bad Request` (with field-level error messages)

---

## 8. Testing Strategy

- **Slice / MockMvc Tests (`DocumentControllerTest`)**: Uses `@SpringBootTest` and `@AutoConfigureMockMvc` to verify HTTP contracts, JSON serialization, validation constraints, and security error codes.
- **Domain Unit Tests**: Pure JUnit 5 tests inherited from the domain package verifying transformation algorithms, boundary conditions, and visitor outputs.

---

## 9. Production System Evolution

In a real hyperscale production deployment:
1. **WebSockets / STOMP / WebRTC**: Replace HTTP polling with persistent WebSocket connections for sub-50ms operation streaming and cursor presence.
2. **Distributed Sequencer Partitioning**: Route all requests for a given `documentId` to the same server node using consistent hashing or an Actor system (e.g. Akka/Pekko), with Redis Streams or Apache Kafka serving as the multi-node durable revision bus.
3. **Snapshotting & Compaction**: Compact the `RevisionLog` into persistent blob storage (e.g. AWS S3 / Google Cloud Storage) every 100 revisions to keep active memory bounded.
