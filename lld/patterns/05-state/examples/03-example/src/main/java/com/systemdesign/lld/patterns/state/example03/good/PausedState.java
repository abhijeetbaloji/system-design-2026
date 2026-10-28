package com.systemdesign.lld.patterns.state.example03.good;

/**
 * Concrete State: Playback is suspended at a specific second offset.
 */
public class PausedState implements PlayerState {

    @Override
    public void play(MediaPlayer player) {
        player.startAudioDecoder();
        player.setState(player.getPlayingState());
    }

    @Override
    public void pause(MediaPlayer player) {
        System.out.println("No effect: Player is already paused.");
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
        player.setState(player.getPlayingState());
    }

    @Override
    public void previous(MediaPlayer player) {
        player.retreatTrack();
        player.startAudioDecoder();
        player.setState(player.getPlayingState());
    }

    @Override
    public String getStateName() {
        return "PAUSED";
    }
}
