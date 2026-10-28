package com.systemdesign.lld.patterns.abstractfactory.example03.good;

public class MySqlTransaction implements DbTransaction {
    private boolean active;

    @Override
    public void begin() {
        this.active = true;
    }

    @Override
    public void commit() {
        if (!active) {
            throw new IllegalStateException("Cannot commit inactive transaction");
        }
        this.active = false;
    }

    @Override
    public void rollback() {
        if (!active) {
            throw new IllegalStateException("Cannot rollback inactive transaction");
        }
        this.active = false;
    }

    @Override
    public boolean isActive() {
        return active;
    }

    @Override
    public String getDatabaseType() {
        return "MySQL";
    }
}
