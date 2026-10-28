package com.systemdesign.lld.patterns.facade.example01.bad;

public class PopcornPopper {
    private boolean on = false;
    public void on() { on = true; System.out.println("Popcorn Popper ON"); }
    public void pop() { System.out.println("Popcorn Popper POPPING!"); }
    public void off() { on = false; System.out.println("Popcorn Popper OFF"); }
    public boolean isOn() { return on; }
}
