package com.systemdesign.lld.patterns.singleton.example01.good;

public interface ConfigurationService {
    String getString(String key);

    String getString(String key, String defaultValue);

    int getInt(String key, int defaultValue);

    boolean getBoolean(String key, boolean defaultValue);

    void setProperty(String key, String value);

    boolean contains(String key);
}
