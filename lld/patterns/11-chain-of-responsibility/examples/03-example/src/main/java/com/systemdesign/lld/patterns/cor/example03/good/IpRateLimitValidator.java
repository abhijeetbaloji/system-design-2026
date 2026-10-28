package com.systemdesign.lld.patterns.cor.example03.good;

import java.util.HashMap;
import java.util.Map;

public class IpRateLimitValidator extends RequestValidator {

    private final int maxRequestsPerIp;
    private final Map<String, Integer> ipCounts = new HashMap<>();

    public IpRateLimitValidator(int maxRequestsPerIp) {
        this.maxRequestsPerIp = maxRequestsPerIp;
    }

    public IpRateLimitValidator() {
        this(5);
    }

    @Override
    public ValidationResult validate(HttpRequest request) {
        if (request == null) return ValidationResult.fail("IpRateLimitValidator", "Null request", 400);

        int count = ipCounts.getOrDefault(request.ipAddress(), 0);
        if (count >= maxRequestsPerIp) {
            System.err.println("[IpRateLimitValidator] 429 Too Many Requests for IP: " + request.ipAddress());
            return ValidationResult.fail("IpRateLimitValidator", "Rate limit exceeded (" + maxRequestsPerIp + " max)", 429);
        }

        ipCounts.put(request.ipAddress(), count + 1);
        System.out.println("[IpRateLimitValidator] IP " + request.ipAddress() + " check passed (count: " + (count + 1) + ")");
        return validateNext(request);
    }
}
