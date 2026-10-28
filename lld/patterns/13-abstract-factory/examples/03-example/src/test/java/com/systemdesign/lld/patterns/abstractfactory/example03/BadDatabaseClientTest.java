package com.systemdesign.lld.patterns.abstractfactory.example03;

import com.systemdesign.lld.patterns.abstractfactory.example03.bad.BadDatabaseClient;
import com.systemdesign.lld.patterns.abstractfactory.example03.bad.MySqlConnection;
import com.systemdesign.lld.patterns.abstractfactory.example03.bad.PostgreSqlCommand;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Bad Design: Database Client Tests")
class BadDatabaseClientTest {

    @Test
    @DisplayName("BadDatabaseClient mixes MySqlConnection with PostgreSqlCommand")
    void testDriverMismatchBug() {
        BadDatabaseClient client = new BadDatabaseClient();
        client.initializeDriver("MYSQL", "jdbc:mysql://localhost:3306/db");

        // Verifies the accidental mismatch
        assertInstanceOf(MySqlConnection.class, client.getConnection());
        assertInstanceOf(PostgreSqlCommand.class, client.getCommand());
    }

    @Test
    @DisplayName("Unsupported database throws IllegalArgumentException")
    void testUnsupportedDatabase() {
        BadDatabaseClient client = new BadDatabaseClient();
        assertThrows(IllegalArgumentException.class, () -> client.initializeDriver("REDIS", "localhost:6379"));
    }
}
