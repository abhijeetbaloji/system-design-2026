# Database-Provider Families Abstract Factory Example

## 1. Scenario Overview

An enterprise application or ORM (such as Hibernate, Entity Framework, or custom JDBC abstraction) must support multiple relational database management systems (RDBMS):
1. **MySQL**: Uses default port 3306, `?` parameter placeholders, and MySQL binary network protocol.
2. **PostgreSQL**: Uses default port 5432, `$1, $2` parameter placeholders, and Postgres frontend/backend protocol.
3. **Oracle**: Uses default port 1521, `:name` / `:1` bind variables, and Oracle TNS wire protocol.

Each database ecosystem requires three strictly interdependent components:
- `DbConnection`: Connects to and disconnects from the database server.
- `DbCommand`: Prepares and executes SQL queries using provider-specific parameter placeholders.
- `DbTransaction`: Manages transactional boundaries (`begin`, `commit`, `rollback`).

Attempting to run a MySQL command over a PostgreSQL connection or attaching an Oracle transaction to a MySQL connection results in catastrophic driver crashes. The driver objects must always originate from the same vendor family.

---

## 2. Bad Design vs Good Design

### Bad Design (Scattered Instantiation & Driver Mismatch)
- `BadDatabaseClient` uses string parameters and `switch` statements to pick connections and commands.
- A typo or misplaced assignment easily pairs a `PostgreSqlConnection` with a `MySqlCommand`.
- Adding a new database provider (such as Oracle or SQL Server) requires finding and editing branching logic across all repository and data access classes.

### Good Design (Abstract Factory Pattern)
- `DatabaseProviderFactory` declares `createConnection()`, `createCommand()`, and `createTransaction()`.
- `MySqlProviderFactory`, `PostgreSqlProviderFactory`, and `OracleProviderFactory` create components strictly from their respective database family.
- `DatabaseRepository` depends exclusively on `DatabaseProviderFactory` and the abstract interfaces (`DbConnection`, `DbCommand`, `DbTransaction`), guaranteeing 100% vendor compatibility.

---

## 3. Class Structure

```
com.systemdesign.lld.patterns.abstractfactory.example03
├── bad
│   ├── BadDatabaseClient.java
│   ├── BadDatabaseDemo.java
│   ├── MySqlCommand.java
│   ├── MySqlConnection.java
│   ├── PostgreSqlCommand.java
│   └── PostgreSqlConnection.java
└── good
    ├── DatabaseDemo.java
    ├── DatabaseProviderFactory.java
    ├── DatabaseRepository.java
    ├── DbCommand.java
    ├── DbConnection.java
    ├── DbTransaction.java
    ├── MySqlCommand.java
    ├── MySqlConnection.java
    ├── MySqlProviderFactory.java
    ├── MySqlTransaction.java
    ├── OracleCommand.java
    ├── OracleConnection.java
    ├── OracleProviderFactory.java
    ├── OracleTransaction.java
    ├── PostgreSqlCommand.java
    ├── PostgreSqlConnection.java
    ├── PostgreSqlProviderFactory.java
    └── PostgreSqlTransaction.java
```

---

## 4. Execution Flow & SOLID Principles

- **Driver Ecosystem Consistency**: A repository configured with `PostgreSqlProviderFactory` will only ever receive PostgreSQL connections, commands, and transactions.
- **Dependency Inversion Principle (DIP)**: High-level business repositories depend on abstract database interfaces, never on concrete vendor drivers.
- **Open/Closed Principle (OCP)**: Adding support for SQLite or Microsoft SQL Server requires only adding a new `DatabaseProviderFactory` implementation.
