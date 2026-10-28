package com.systemdesign.lld.patterns.abstractfactory.example03.good;

public class DatabaseDemo {
    public static void main(String[] args) {
        String sampleQuery = "UPDATE accounts SET balance = balance - 100 WHERE id = ?";

        // 1. MySQL Run
        DatabaseProviderFactory mysqlFactory = new MySqlProviderFactory();
        DatabaseRepository mysqlRepo = new DatabaseRepository(mysqlFactory);
        System.out.println("=== MySQL Transaction Workflow ===");
        System.out.println(mysqlRepo.executeTransactionalQuery("jdbc:mysql://prod-db:3306/finance", sampleQuery));
        System.out.println();

        // 2. PostgreSQL Run
        DatabaseProviderFactory postgresFactory = new PostgreSqlProviderFactory();
        DatabaseRepository postgresRepo = new DatabaseRepository(postgresFactory);
        System.out.println("=== PostgreSQL Transaction Workflow ===");
        System.out.println(postgresRepo.executeTransactionalQuery("jdbc:postgresql://analytics-db:5432/finance", sampleQuery));
        System.out.println();

        // 3. Oracle Run
        DatabaseProviderFactory oracleFactory = new OracleProviderFactory();
        DatabaseRepository oracleRepo = new DatabaseRepository(oracleFactory);
        System.out.println("=== Oracle Transaction Workflow ===");
        System.out.println(oracleRepo.executeTransactionalQuery("jdbc:oracle:thin:@legacy-db:1521:XE", sampleQuery));
    }
}
