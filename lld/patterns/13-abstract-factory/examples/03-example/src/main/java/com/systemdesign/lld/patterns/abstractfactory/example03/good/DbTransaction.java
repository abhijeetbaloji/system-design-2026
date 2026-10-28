package com.systemdesign.lld.patterns.abstractfactory.example03.good;

public interface DbTransaction {
    void begin();

    void commit();

    void rollback();

    boolean isActive();

    String getDatabaseType();
}
