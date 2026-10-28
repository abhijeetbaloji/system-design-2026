package com.systemdesign.lld.patterns.state.example03.good;

/**
 * Concrete State: Audio is actively streaming.
 */
public class PlayingState implements PlayerState {

    @Override
    public void play(MediaPlayer player) {
        // Pressing Play while Playing acts as Pause toggle
        pause(player);
    }

    @Override
    public void pause(MediaPlayer player) {
        player.pauseAudioDecoder();
        player.setState(player.getPausedState());
    }

    @Override
    public void stop(MediaPlayer player) {
        player.stopAudioDecoder();
        player.setState(player.getStoppedState());
    }

    @Override
    public void next(MediaPlayer player) {
        player.advanceTrack();
        player.startAudioDecoder();
    }

    @Override
    public void previous(MediaPlayer player) {
        player.retreatTrack();
        player.startAudioDecoder();
    }

    @Override
    public String getStateName() {
        return "PLAYING";
    }
}
