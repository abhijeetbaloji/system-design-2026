package com.systemdesign.lld.patterns.abstractfactory.example03.good;

public interface DbConnection {
    boolean connect(String connectionString);

    void disconnect();

    boolean isConnected();

    String getDatabaseType();
}
