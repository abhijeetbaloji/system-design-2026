package com.systemdesign.lld.patterns.cor.example03.good;

/**
 * Abstract Handler for Filter/Validation Chain:
 * In this style ("All-Must-Process"), every handler validates its concern.
 * If validation succeeds, it forwards to validateNext().
 * If any handler fails, it short-circuits and immediately returns a failure result.
 */
public abstract class RequestValidator {

    private RequestValidator nextValidator;

    public RequestValidator setNext(RequestValidator next) {
        this.nextValidator = next;
        return next;
    }

    public abstract ValidationResult validate(HttpRequest request);

    protected ValidationResult validateNext(HttpRequest request) {
        if (nextValidator != null) {
            return nextValidator.validate(request);
        }
        // Reached end of pipeline successfully: All validators passed!
        return ValidationResult.pass();
    }

    public RequestValidator getNextValidator() {
        return nextValidator;
    }
}
