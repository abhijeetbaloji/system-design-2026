package com.systemdesign.lld.googledocs.repository;

import com.systemdesign.lld.googledocs.domain.model.Document;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class InMemoryDocumentRepository implements DocumentRepository {

    private final Map<String, Document> store = new ConcurrentHashMap<>();

    @Override
    public Document save(Document document) {
        store.put(document.getId(), document);
        return document;
    }

    @Override
    public Optional<Document> findById(String documentId) {
        return Optional.ofNullable(store.get(documentId));
    }

    @Override
    public boolean existsById(String documentId) {
        return store.containsKey(documentId);
    }

    @Override
    public void deleteById(String documentId) {
        store.remove(documentId);
    }
}
