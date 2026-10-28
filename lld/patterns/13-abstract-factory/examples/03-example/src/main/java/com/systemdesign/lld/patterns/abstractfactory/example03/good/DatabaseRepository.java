package com.systemdesign.lld.patterns.abstractfactory.example03.good;

import java.util.Objects;

/**
 * Client repository executing queries using an injected DatabaseProviderFactory.
 */
public class DatabaseRepository {
    private final DbConnection connection;
    private final DbCommand command;
    private final DbTransaction transaction;
    private final String providerName;

    public DatabaseRepository(DatabaseProviderFactory factory) {
        Objects.requireNonNull(factory, "DatabaseProviderFactory cannot be null");
        this.connection = factory.createConnection();
        this.command = factory.createCommand();
        this.transaction = factory.createTransaction();
        this.providerName = factory.getProviderName();
    }

    public boolean isConsistent() {
        return connection.getDatabaseType().equalsIgnoreCase(providerName)
                && command.getDatabaseType().equalsIgnoreCase(providerName)
                && transaction.getDatabaseType().equalsIgnoreCase(providerName);
    }

    public String executeTransactionalQuery(String connectionString, String baseSql) {
        if (!isConsistent()) {
            throw new IllegalStateException("Database driver mismatch detected!");
        }

        StringBuilder log = new StringBuilder();
        connection.connect(connectionString);
        log.append("1. Connected: ").append(connection.isConnected()).append("\n");

        transaction.begin();
        log.append("2. Transaction Started: ").append(transaction.isActive()).append("\n");

        String formattedSql = baseSql.replace("?", command.getParameterPlaceholder(1));
        String executionResult = command.execute(formattedSql);
        log.append("3. Execution: ").append(executionResult).append("\n");

        transaction.commit();
        log.append("4. Transaction Committed: ").append(!transaction.isActive()).append("\n");

        connection.disconnect();
        log.append("5. Disconnected: ").append(!connection.isConnected());

        return log.toString();
    }

    public DbConnection getConnection() {
        return connection;
    }

    public DbCommand getCommand() {
        return command;
    }

    public DbTransaction getTransaction() {
        return transaction;
    }

    public String getProviderName() {
        return providerName;
    }
}
