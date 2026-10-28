package com.systemdesign.lld.patterns.abstractfactory.example03.bad;

public class PostgreSqlConnection {
    private boolean connected;

    public void open(String url) {
        System.out.println("Connected to PostgreSQL server via port 5432: " + url);
        this.connected = true;
    }

    public boolean isConnected() {
        return connected;
    }
}
