package com.systemdesign.lld.patterns.facade.example01.bad;

public class BadHomeTheaterDemo {
    public static void main(String[] args) {
        BadHomeTheaterClient client = new BadHomeTheaterClient();
        client.watchMovie("Interstellar");
        System.out.println("-------------------------");
        client.endMovie();
    }
}
