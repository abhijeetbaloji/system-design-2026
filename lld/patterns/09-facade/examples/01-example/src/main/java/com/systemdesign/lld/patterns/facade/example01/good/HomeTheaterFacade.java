package com.systemdesign.lld.patterns.facade.example01.good;

/**
 * Facade Class:
 * Unified, simple interface coordinating the complex home theater subsystem.
 *
 * Responsibilities:
 * 1. Exposes clean, high-level methods: watchMovie, pauseMovie, resumeMovie, endMovie.
 * 2. Enforces proper hardware initialization sequence and graceful shutdown sequence.
 * 3. Does not isolate the subsystem; power users can still access individual components.
 */
public class HomeTheaterFacade {

    private final PopcornPopper popper;
    private final TheaterLights lights;
    private final Projector projector;
    private final Amplifier amp;
    private final StreamingPlayer player;

    public HomeTheaterFacade(PopcornPopper popper,
                              TheaterLights lights,
                              Projector projector,
                              Amplifier amp,
                              StreamingPlayer player) {
        this.popper = popper != null ? popper : new PopcornPopper();
        this.lights = lights != null ? lights : new TheaterLights();
        this.projector = projector != null ? projector : new Projector();
        this.amp = amp != null ? amp : new Amplifier();
        this.player = player != null ? player : new StreamingPlayer();
    }

    public void watchMovie(String movie) {
        System.out.println("=== Initiating Cinema Experience: " + movie + " ===");
        popper.on();
        popper.pop();
        lights.dim(10);
        projector.on();
        projector.wideScreenMode();
        amp.on();
        amp.setSurroundSound();
        amp.setVolume(12);
        player.on();
        player.play(movie);
        System.out.println("=== Cinema is ready. Enjoy the show! ===\n");
    }

    public void pauseMovie() {
        System.out.println("=== Pausing Movie (Intermission) ===");
        player.pause();
        lights.dim(35); // Raise lights slightly for snacks
    }

    public void resumeMovie() {
        System.out.println("=== Resuming Movie ===");
        lights.dim(10);
        player.resume();
    }

    public void endMovie() {
        System.out.println("=== Shutting Down Cinema ===");
        popper.off();
        lights.on();
        projector.off();
        amp.off();
        player.stop();
        player.off();
        System.out.println("=== Home theater safely shut down ===\n");
    }

    // Direct accessors for advanced users who need custom adjustments
    public PopcornPopper getPopper() { return popper; }
    public TheaterLights getLights() { return lights; }
    public Projector getProjector() { return projector; }
    public Amplifier getAmp() { return amp; }
    public StreamingPlayer getPlayer() { return player; }
}
