package com.systemdesign.lld.patterns.abstractfactory.example03.bad;

public class MySqlConnection {
    private boolean connected;

    public void open(String url) {
        System.out.println("Connected to MySQL server via port 3306: " + url);
        this.connected = true;
    }

    public boolean isConnected() {
        return connected;
    }
}
