package com.systemdesign.lld.patterns.cor.example03.bad;

public class BadGatewayDemo {
    public static void main(String[] args) {
        BadApiGatewayValidator validator = new BadApiGatewayValidator();

        HttpRequest r1 = new HttpRequest("/api/data", "192.168.1.1", "Bearer valid-token-123", "normal payload", "USER");
        HttpRequest r2 = new HttpRequest("/api/data", "192.168.1.1", "invalid-token", "hello", "USER");

        System.out.println("R1 valid: " + validator.validateRequest(r1));
        System.out.println("R2 valid: " + validator.validateRequest(r2));
    }
}
