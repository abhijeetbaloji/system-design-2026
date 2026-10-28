package com.systemdesign.lld.patterns.state.example03.good;

/**
 * State Interface:
 * Declares all control operations for the media player.
 */
public interface PlayerState {

    void play(MediaPlayer player);

    void pause(MediaPlayer player);

    void stop(MediaPlayer player);

    void next(MediaPlayer player);

    void previous(MediaPlayer player);

    String getStateName();
}
