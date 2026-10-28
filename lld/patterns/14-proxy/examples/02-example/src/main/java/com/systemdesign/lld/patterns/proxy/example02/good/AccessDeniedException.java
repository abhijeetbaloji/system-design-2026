package com.systemdesign.lld.patterns.proxy.example02.good;

public class AccessDeniedException extends RuntimeException {
    public AccessDeniedException(String message) {
        super(message);
    }
}
