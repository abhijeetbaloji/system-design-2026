package com.systemdesign.lld.googledocs.repository;

import com.systemdesign.lld.googledocs.domain.model.Document;
import java.util.Optional;

/*
 * Repository interface decoupling domain logic from persistent storage.
 */
public interface DocumentRepository {

    Document save(Document document);

    Optional<Document> findById(String documentId);

    boolean existsById(String documentId);

    void deleteById(String documentId);
}
