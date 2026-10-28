package com.systemdesign.lld.patterns.abstractfactory.example03.bad;

public class BadDatabaseDemo {
    public static void main(String[] args) {
        BadDatabaseClient client = new BadDatabaseClient();
        client.initializeDriver("MYSQL", "jdbc:mysql://localhost:3306/ecommerce");

        System.out.println("Connection: " + client.getConnection().getClass().getSimpleName());
        System.out.println("Command: " + client.getCommand().getClass().getSimpleName());
        System.out.println("Query execution attempt: " + ((PostgreSqlCommand) client.getCommand()).executeQuery("SELECT * FROM users"));
    }
}
