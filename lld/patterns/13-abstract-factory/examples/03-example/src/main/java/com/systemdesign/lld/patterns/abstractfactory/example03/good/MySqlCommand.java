package com.systemdesign.lld.patterns.abstractfactory.example03.good;

public class MySqlCommand implements DbCommand {

    @Override
    public String execute(String sql) {
        return "[MySQL Engine on :3306] Successfully executed: " + sql;
    }

    @Override
    public String getParameterPlaceholder(int index) {
        return "?";
    }

    @Override
    public String getDatabaseType() {
        return "MySQL";
    }
}
