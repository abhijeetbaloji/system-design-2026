package com.systemdesign.lld.patterns.facade.example03.bad;

public class BadMediaDemo {
    public static void main(String[] args) {
        BadMediaClient client = new BadMediaClient();
        byte[] converted = client.convertVideo("holiday_trip.ogg", "mp4");
        System.out.println("Output byte length: " + converted.length);
    }
}
