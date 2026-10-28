package com.systemdesign.lld.patterns.abstractfactory.example03.good;

public class OracleCommand implements DbCommand {

    @Override
    public String execute(String sql) {
        return "[Oracle Database TNS Protocol on :1521] Successfully executed: " + sql;
    }

    @Override
    public String getParameterPlaceholder(int index) {
        return ":" + index;
    }

    @Override
    public String getDatabaseType() {
        return "Oracle";
    }
}
