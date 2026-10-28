package com.systemdesign.lld.patterns.abstractfactory.example03.bad;

public class BadDatabaseClient {
    private Object connection;
    private Object command;

    public void initializeDriver(String dbType, String url) {
        if ("MYSQL".equalsIgnoreCase(dbType)) {
            MySqlConnection conn = new MySqlConnection();
            conn.open(url);
            this.connection = conn;
            // BUG: Misconfigured command! Injected a Postgres command object instead of MySQL!
            this.command = new PostgreSqlCommand();
        } else if ("POSTGRES".equalsIgnoreCase(dbType)) {
            PostgreSqlConnection conn = new PostgreSqlConnection();
            conn.open(url);
            this.connection = conn;
            this.command = new PostgreSqlCommand();
        } else {
            throw new IllegalArgumentException("Unsupported database: " + dbType);
        }
    }

    public Object getConnection() {
        return connection;
    }

    public Object getCommand() {
        return command;
    }
}
