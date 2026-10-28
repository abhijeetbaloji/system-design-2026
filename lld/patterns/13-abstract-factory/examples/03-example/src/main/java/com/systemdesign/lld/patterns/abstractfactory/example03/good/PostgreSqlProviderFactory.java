package com.systemdesign.lld.patterns.abstractfactory.example03.good;

public class PostgreSqlProviderFactory implements DatabaseProviderFactory {

    @Override
    public DbConnection createConnection() {
        return new PostgreSqlConnection();
    }

    @Override
    public DbCommand createCommand() {
        return new PostgreSqlCommand();
    }

    @Override
    public DbTransaction createTransaction() {
        return new PostgreSqlTransaction();
    }

    @Override
    public String getProviderName() {
        return "PostgreSQL";
    }
}
