package com.systemdesign.lld.patterns.abstractfactory.example03.good;

public class PostgreSqlCommand implements DbCommand {

    @Override
    public String execute(String sql) {
        return "[PostgreSQL Frontend/Backend Protocol on :5432] Successfully executed: " + sql;
    }

    @Override
    public String getParameterPlaceholder(int index) {
        return "$" + index;
    }

    @Override
    public String getDatabaseType() {
        return "PostgreSQL";
    }
}
