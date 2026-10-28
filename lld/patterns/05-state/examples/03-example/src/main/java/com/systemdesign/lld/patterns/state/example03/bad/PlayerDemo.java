package com.systemdesign.lld.patterns.state.example03.bad;

import java.util.List;

/**
 * Runner demonstrating bad design execution.
 */
public class PlayerDemo {
    public static void main(String[] args) {
        MediaPlayer player = new MediaPlayer(List.of("Bohemian Rhapsody", "Stairway to Heaven", "Hotel California"));
        player.play();
        player.next();
        player.toggleLock();
        player.play(); // blocked
        player.toggleLock();
        player.stop();
    }
}
