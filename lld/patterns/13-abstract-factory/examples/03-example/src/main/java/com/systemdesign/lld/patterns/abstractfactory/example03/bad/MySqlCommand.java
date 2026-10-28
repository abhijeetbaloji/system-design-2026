package com.systemdesign.lld.patterns.abstractfactory.example03.bad;

public class MySqlCommand {
    public String executeQuery(String sql) {
        return "Executing MySQL dialect query [placeholder '?']: " + sql;
    }
}
