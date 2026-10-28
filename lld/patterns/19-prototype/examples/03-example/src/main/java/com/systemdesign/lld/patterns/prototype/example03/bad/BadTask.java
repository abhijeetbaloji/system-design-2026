package com.systemdesign.lld.patterns.prototype.example03.bad;

import java.util.HashMap;
import java.util.Map;

public class BadTask {
    public String name;
    public Map<String, String> env = new HashMap<>();

    public BadTask(String name) {
        this.name = name;
    }
}
