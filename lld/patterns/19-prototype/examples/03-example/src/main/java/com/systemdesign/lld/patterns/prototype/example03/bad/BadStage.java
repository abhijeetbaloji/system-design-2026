package com.systemdesign.lld.patterns.prototype.example03.bad;

import java.util.ArrayList;
import java.util.List;

public class BadStage {
    public String name;
    public List<BadTask> tasks = new ArrayList<>();

    public BadStage(String name) {
        this.name = name;
    }
}
