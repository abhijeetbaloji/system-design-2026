package com.systemdesign.lld.patterns.abstractfactory.example03.good;

public class OracleConnection implements DbConnection {
    private boolean connected;
    private String connectionUrl;

    @Override
    public boolean connect(String connectionString) {
        this.connectionUrl = connectionString;
        this.connected = true;
        return true;
    }

    @Override
    public void disconnect() {
        this.connected = false;
    }

    @Override
    public boolean isConnected() {
        return connected;
    }

    @Override
    public String getDatabaseType() {
        return "Oracle";
    }

    public String getConnectionUrl() {
        return connectionUrl;
    }
}
