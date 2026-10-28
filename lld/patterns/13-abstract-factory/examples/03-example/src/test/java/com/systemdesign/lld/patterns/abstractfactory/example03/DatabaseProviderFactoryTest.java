package com.systemdesign.lld.patterns.abstractfactory.example03;

import com.systemdesign.lld.patterns.abstractfactory.example03.good.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Good Design: Abstract Factory Database Provider Tests")
class DatabaseProviderFactoryTest {

    @Test
    @DisplayName("MySqlProviderFactory creates exclusively MySQL driver components")
    void testMySqlFactory() {
        DatabaseProviderFactory factory = new MySqlProviderFactory();
        assertEquals("MySQL", factory.getProviderName());

        DbConnection conn = factory.createConnection();
        DbCommand cmd = factory.createCommand();
        DbTransaction tx = factory.createTransaction();

        assertInstanceOf(MySqlConnection.class, conn);
        assertInstanceOf(MySqlCommand.class, cmd);
        assertInstanceOf(MySqlTransaction.class, tx);

        assertEquals("MySQL", conn.getDatabaseType());
        assertEquals("MySQL", cmd.getDatabaseType());
        assertEquals("MySQL", tx.getDatabaseType());

        assertEquals("?", cmd.getParameterPlaceholder(1));
        assertTrue(cmd.execute("SELECT 1").contains("MySQL Engine on :3306"));
    }

    @Test
    @DisplayName("PostgreSqlProviderFactory creates exclusively PostgreSQL driver components")
    void testPostgreSqlFactory() {
        DatabaseProviderFactory factory = new PostgreSqlProviderFactory();
        assertEquals("PostgreSQL", factory.getProviderName());

        DbConnection conn = factory.createConnection();
        DbCommand cmd = factory.createCommand();
        DbTransaction tx = factory.createTransaction();

        assertInstanceOf(PostgreSqlConnection.class, conn);
        assertInstanceOf(PostgreSqlCommand.class, cmd);
        assertInstanceOf(PostgreSqlTransaction.class, tx);

        assertEquals("PostgreSQL", conn.getDatabaseType());
        assertEquals("$1", cmd.getParameterPlaceholder(1));
        assertTrue(cmd.execute("SELECT 1").contains("5432"));
    }

    @Test
    @DisplayName("OracleProviderFactory creates exclusively Oracle driver components")
    void testOracleFactory() {
        DatabaseProviderFactory factory = new OracleProviderFactory();
        assertEquals("Oracle", factory.getProviderName());

        DbConnection conn = factory.createConnection();
        DbCommand cmd = factory.createCommand();
        DbTransaction tx = factory.createTransaction();

        assertInstanceOf(OracleConnection.class, conn);
        assertInstanceOf(OracleCommand.class, cmd);
        assertInstanceOf(OracleTransaction.class, tx);

        assertEquals("Oracle", conn.getDatabaseType());
        assertEquals(":1", cmd.getParameterPlaceholder(1));
        assertTrue(cmd.execute("SELECT 1").contains("1521"));
    }

    @Test
    @DisplayName("DatabaseRepository executes transaction successfully with guaranteed consistency")
    void testTransactionalQueryExecution() {
        DatabaseRepository repo = new DatabaseRepository(new PostgreSqlProviderFactory());
        assertTrue(repo.isConsistent());

        String log = repo.executeTransactionalQuery("jdbc:postgresql://localhost:5432/test", "SELECT * FROM orders WHERE id = ?");
        assertNotNull(log);
        assertTrue(log.contains("Connected: true"));
        assertTrue(log.contains("Transaction Started: true"));
        assertTrue(log.contains("Transaction Committed: true"));
        assertTrue(log.contains("Disconnected: true"));
    }

    @Test
    @DisplayName("Null factory in DatabaseRepository throws NullPointerException")
    void testNullFactoryValidation() {
        assertThrows(NullPointerException.class, () -> new DatabaseRepository(null));
    }
}
