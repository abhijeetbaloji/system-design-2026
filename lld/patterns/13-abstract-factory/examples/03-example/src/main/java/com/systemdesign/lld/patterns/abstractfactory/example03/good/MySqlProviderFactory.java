package com.systemdesign.lld.patterns.abstractfactory.example03.good;

public class MySqlProviderFactory implements DatabaseProviderFactory {

    @Override
    public DbConnection createConnection() {
        return new MySqlConnection();
    }

    @Override
    public DbCommand createCommand() {
        return new MySqlCommand();
    }

    @Override
    public DbTransaction createTransaction() {
        return new MySqlTransaction();
    }

    @Override
    public String getProviderName() {
        return "MySQL";
    }
}
