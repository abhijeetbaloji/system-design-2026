package bad;

/**
 * BAD DESIGN: Monolithic manager handling both business workflow AND direct object creation.
 *
 * Flaws exposed:
 * 1. Violates Open/Closed Principle (OCP): Adding Push notifications or WhatsApp
 *    requires editing the switch statement and adding new imports.
 * 2. Violates Single Responsibility Principle (SRP): It manages alert policies
 *    and knows the exact constructor arguments for every transport technology.
 * 3. High Coupling: Directly depends on EmailService and SmsGateway.
 */
public class BadNotificationManager {

    public void sendAlert(String channelType, String recipient, String message) {
        if (recipient == null || recipient.isBlank()) {
            throw new IllegalArgumentException("Recipient cannot be empty");
        }

        // FLAW: Direct instantiation based on type codes and conditional logic.
        // As channels grow, this switch statement balloons.
        switch (channelType.toUpperCase()) {
            case "EMAIL" -> {
                // Hardcoded construction logic coupled to business logic
                EmailService emailService = new EmailService("smtp.internal.corp");
                emailService.sendEmail(recipient, message);
            }
            case "SMS" -> {
                // Completely different construction parameters and method signatures
                SmsGateway smsGateway = new SmsGateway("CORP-ALERT");
                smsGateway.transmitText(recipient, message);
            }
            default -> throw new IllegalArgumentException("Unsupported channel type: " + channelType);
        }

        // Shared post-processing audit
        System.out.printf("[Audit] Dispatched %s alert to: %s%n%n", channelType, recipient);
    }
}
