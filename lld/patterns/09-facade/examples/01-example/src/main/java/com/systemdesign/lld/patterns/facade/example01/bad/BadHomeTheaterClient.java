package com.systemdesign.lld.patterns.facade.example01.bad;

/**
 * BAD DESIGN:
 * Client directly controls every individual hardware subsystem class.
 *
 * Flaws:
 * 1. Extreme Coupling:
 *    The client has direct references to 5 separate hardware classes.
 * 2. Brittle Sequencing:
 *    If caller forgets to dim the lights, configure the amp surround sound, or turn off the popper,
 *    the system ends up in an inconsistent or dangerous state.
 * 3. Boilerplate Duplication:
 *    Every client (mobile app, voice control, IR remote) must re-implement this 10-step sequence.
 */
public class BadHomeTheaterClient {

    private final PopcornPopper popper = new PopcornPopper();
    private final TheaterLights lights = new TheaterLights();
    private final Projector projector = new Projector();
    private final Amplifier amp = new Amplifier();
    private final StreamingPlayer player = new StreamingPlayer();

    public void watchMovie(String movie) {
        System.out.println("Get ready to watch a movie...");
        popper.on();
        popper.pop();
        lights.dim(10);
        projector.on();
        projector.wideScreenMode();
        amp.on();
        amp.setSurroundSound();
        amp.setVolume(7);
        player.on();
        player.play(movie);
    }

    public void endMovie() {
        System.out.println("Shutting movie theater down...");
        popper.off();
        lights.on();
        projector.off();
        amp.off();
        player.stop();
        player.off();
    }

    public PopcornPopper getPopper() { return popper; }
    public TheaterLights getLights() { return lights; }
    public Projector getProjector() { return projector; }
    public Amplifier getAmp() { return amp; }
    public StreamingPlayer getPlayer() { return player; }
}
