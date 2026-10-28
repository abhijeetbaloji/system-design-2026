package com.systemdesign.lld.patterns.facade.example01.good;

public class HomeTheaterDemo {
    public static void main(String[] args) {
        HomeTheaterFacade homeTheater = new HomeTheaterFacade(
                new PopcornPopper(),
                new TheaterLights(),
                new Projector(),
                new Amplifier(),
                new StreamingPlayer()
        );

        // 1. One-line command to start the movie
        homeTheater.watchMovie("Oppenheimer");

        // 2. Pause for snack refill
        homeTheater.pauseMovie();

        // 3. Resume
        homeTheater.resumeMovie();

        // 4. One-line command to power everything down safely
        homeTheater.endMovie();
    }
}
