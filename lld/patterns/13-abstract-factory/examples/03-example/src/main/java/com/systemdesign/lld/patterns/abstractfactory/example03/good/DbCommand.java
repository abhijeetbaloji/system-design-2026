package com.systemdesign.lld.patterns.abstractfactory.example03.good;

public interface DbCommand {
    String execute(String sql);

    String getParameterPlaceholder(int index);

    String getDatabaseType();
}
