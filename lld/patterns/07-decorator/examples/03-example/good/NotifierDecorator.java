package good;

import java.util.Objects;

/**
 * Base Decorator for the Notifier family.
 *
 * Implements the Notifier interface and holds an aggregated reference to
 * a wrapped Notifier instance. Provides pass-through delegation.
 */
public abstract class NotifierDecorator implements Notifier {
    protected final Notifier wrappee;

    protected NotifierDecorator(Notifier wrappee) {
        this.wrappee = Objects.requireNonNull(wrappee, "Notifier to wrap cannot be null");
    }

    @Override
    public void send(String recipient, String subject, String message) {
        wrappee.send(recipient, subject, message);
    }
}
