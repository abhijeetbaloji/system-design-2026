package com.systemdesign.lld.patterns.abstractfactory.example03.bad;

public class PostgreSqlCommand {
    public String executeQuery(String sql) {
        return "Executing PostgreSQL dialect query [placeholder '$1']: " + sql;
    }
}
