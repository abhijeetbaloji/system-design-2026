package com.systemdesign.lld.googledocs;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.systemdesign.lld.googledocs.domain.model.Role;
import com.systemdesign.lld.googledocs.dto.CreateDocumentRequest;
import com.systemdesign.lld.googledocs.dto.OperationRequest;
import com.systemdesign.lld.googledocs.dto.ShareDocumentRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class DocumentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testCreateAndRetrieveDocument() throws Exception {
        CreateDocumentRequest createReq = new CreateDocumentRequest("doc-rest-1", "Spring Boot Doc", "alice");

        mockMvc.perform(post("/api/v1/documents")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createReq)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.documentId").value("doc-rest-1"))
                .andExpect(jsonPath("$.revision").value(0));

        mockMvc.perform(get("/api/v1/documents/doc-rest-1?userId=alice"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Spring Boot Doc"));
    }

    @Test
    void testApplyOperationAndExport() throws Exception {
        CreateDocumentRequest createReq = new CreateDocumentRequest("doc-rest-2", "OT Collaboration Doc", "alice");
        mockMvc.perform(post("/api/v1/documents")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createReq)))
                .andExpect(status().isCreated());

        // Insert operation
        OperationRequest opReq = new OperationRequest(
                "INSERT",
                "alice",
                0,
                0,
                "Spring Framework",
                null,
                true,
                false,
                14
        );

        mockMvc.perform(post("/api/v1/documents/doc-rest-2/operations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(opReq)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.revisionNumber").value(1));

        // Export markdown
        mockMvc.perform(get("/api/v1/documents/doc-rest-2/export?userId=alice&format=MARKDOWN"))
                .andExpect(status().isOk());
    }

    @Test
    void testUnauthorizedAccessReturnsForbidden() throws Exception {
        CreateDocumentRequest createReq = new CreateDocumentRequest("doc-rest-3", "Private Doc", "alice");
        mockMvc.perform(post("/api/v1/documents")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createReq)))
                .andExpect(status().isCreated());

        // Bob tries to read without permission
        mockMvc.perform(get("/api/v1/documents/doc-rest-3?userId=bob"))
                .andExpect(status().isForbidden());

        // Alice shares as VIEWER
        ShareDocumentRequest shareReq = new ShareDocumentRequest("alice", "bob", Role.VIEWER);
        mockMvc.perform(post("/api/v1/documents/doc-rest-3/share")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(shareReq)))
                .andExpect(status().isNoContent());

        // Bob can now read
        mockMvc.perform(get("/api/v1/documents/doc-rest-3?userId=bob"))
                .andExpect(status().isOk());

        // Bob tries to write -> Forbidden
        OperationRequest bobWrite = new OperationRequest("INSERT", "bob", 0, 0, "Hack", null, false, false, 12);
        mockMvc.perform(post("/api/v1/documents/doc-rest-3/operations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(bobWrite)))
                .andExpect(status().isForbidden());
    }

    @Test
    void testNonExistentDocumentReturnsNotFound() throws Exception {
        mockMvc.perform(get("/api/v1/documents/non-existent?userId=alice"))
                .andExpect(status().isNotFound());
    }
}
