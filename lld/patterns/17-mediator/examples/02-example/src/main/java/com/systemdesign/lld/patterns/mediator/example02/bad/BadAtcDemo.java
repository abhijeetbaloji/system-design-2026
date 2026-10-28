package com.systemdesign.lld.patterns.mediator.example02.bad;

public class BadAtcDemo {
    public static void main(String[] args) {
        BadRunway runway = new BadRunway("09L");
        BadAirplane flightA = new BadAirplane("UA-100");
        BadAirplane flightB = new BadAirplane("BA-200");

        System.out.println("Flight A landing attempt: " + flightA.attemptLanding(runway));
        System.out.println("Flight B landing attempt: " + flightB.attemptLanding(runway));

        // When Flight A vacates, Flight B has no automatic notification or queue
        flightA.vacateRunway(runway);
        System.out.println("Runway vacated, but Flight B must manually poll or guess!");
    }
}
