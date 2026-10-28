package com.systemdesign.lld.patterns.command.example03.good;

public class EmailDigestCommand implements TaskCommand {

    private final EmailService emailService;
    private final String recipient;
    private final String subject;
    private final String body;
    private final int priority;

    public EmailDigestCommand(EmailService emailService, String recipient, String subject, String body, int priority) {
        this.emailService = emailService;
        this.recipient = recipient;
        this.subject = subject;
        this.body = body;
        this.priority = priority;
    }

    @Override
    public String getName() {
        return "EmailDigest:" + recipient;
    }

    @Override
    public int getPriority() {
        return priority;
    }

    @Override
    public boolean isRetryable() {
        return false; // Do not spam recipients on network failure
    }

    @Override
    public void execute() {
        emailService.sendEmail(recipient, subject, body);
    }
}
