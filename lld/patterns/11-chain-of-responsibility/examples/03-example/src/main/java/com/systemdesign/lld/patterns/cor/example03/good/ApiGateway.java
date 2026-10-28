package com.systemdesign.lld.patterns.cor.example03.good;

/**
 * Client Service:
 * Ingests HTTP requests and passes them into the configured filter validation chain.
 */
public class ApiGateway {

    private final RequestValidator chainHead;

    public ApiGateway(RequestValidator chainHead) {
        this.chainHead = chainHead;
    }

    public ValidationResult handleRequest(HttpRequest request) {
        System.out.println("=== Inbound Request: " + request.path() + " from " + request.ipAddress() + " ===");
        return chainHead.validate(request);
    }
}
