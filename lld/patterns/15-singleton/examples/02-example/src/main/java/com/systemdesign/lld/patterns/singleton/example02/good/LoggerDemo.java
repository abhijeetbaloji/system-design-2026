package com.systemdesign.lld.patterns.singleton.example02.good;

public class LoggerDemo {
    public static void main(String[] args) {
        ProcessLoggerSingleton logger1 = ProcessLoggerSingleton.getInstance();
        ProcessLoggerSingleton logger2 = ProcessLoggerSingleton.getInstance();

        System.out.println("Do both logger references point to the same singleton instance? " + (logger1 == logger2));

        AuthService authService = new AuthService(logger1);
        PaymentService paymentService = new PaymentService(logger2);

        // Default level is INFO
        authService.authenticate("admin", "wrong_pass");
        authService.authenticate("admin", "secret");
        paymentService.processPayment("ORD-991", 250.00);
        paymentService.processPayment("ORD-992", -10.00);

        System.out.println("\nTotal unified log records recorded in process: " + logger1.getLogs().size());
    }
}
