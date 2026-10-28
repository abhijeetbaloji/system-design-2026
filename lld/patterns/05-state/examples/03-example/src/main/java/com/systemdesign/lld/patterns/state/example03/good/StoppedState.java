package com.systemdesign.lld.patterns.state.example03.good;

/**
 * Concrete State: Player is stopped and audio decoder is idle.
 */
public class StoppedState implements PlayerState {

    @Override
    public void play(MediaPlayer player) {
        player.setPlaybackPositionSeconds(0);
        player.startAudioDecoder();
        player.setState(player.getPlayingState());
    }

    @Override
    public void pause(MediaPlayer player) {
        System.out.println("No effect: Cannot pause when stopped.");
    }

    @Override
    public void stop(MediaPlayer player) {
        System.out.println("No effect: Player is already stopped.");
    }

    @Override
    public void next(MediaPlayer player) {
        player.advanceTrack();
    }

    @Override
    public void previous(MediaPlayer player) {
        player.retreatTrack();
    }

    @Override
    public String getStateName() {
        return "STOPPED";
    }
}
