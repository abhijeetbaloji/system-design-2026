package com.systemdesign.lld.patterns.abstractfactory.example03.good;

/**
 * Abstract Factory interface declaring creation methods for database driver families.
 */
public interface DatabaseProviderFactory {
    DbConnection createConnection();

    DbCommand createCommand();

    DbTransaction createTransaction();

    String getProviderName();
}
