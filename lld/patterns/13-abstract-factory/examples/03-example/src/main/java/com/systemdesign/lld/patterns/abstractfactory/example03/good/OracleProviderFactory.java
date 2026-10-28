package com.systemdesign.lld.patterns.abstractfactory.example03.good;

public class OracleProviderFactory implements DatabaseProviderFactory {

    @Override
    public DbConnection createConnection() {
        return new OracleConnection();
    }

    @Override
    public DbCommand createCommand() {
        return new OracleCommand();
    }

    @Override
    public DbTransaction createTransaction() {
        return new OracleTransaction();
    }

    @Override
    public String getProviderName() {
        return "Oracle";
    }
}
