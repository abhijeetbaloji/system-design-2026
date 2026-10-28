package com.systemdesign.lld.patterns.facade.example01.good;

public class PopcornPopper {
    private boolean on = false;

    public void on() { on = true; System.out.println("[PopcornPopper] Powered ON"); }
    public void pop() { System.out.println("[PopcornPopper] Hot buttered popcorn popping!"); }
    public void off() { on = false; System.out.println("[PopcornPopper] Powered OFF"); }
    public boolean isOn() { return on; }
}
