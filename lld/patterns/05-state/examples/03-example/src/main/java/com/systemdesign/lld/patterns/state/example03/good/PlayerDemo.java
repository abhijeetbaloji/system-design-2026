package com.systemdesign.lld.patterns.state.example03.good;

import java.util.List;

/**
 * Runner demonstrating the Media Player State pattern.
 */
public class PlayerDemo {
    public static void main(String[] args) {
        MediaPlayer player = new MediaPlayer(List.of(
            "Pink Floyd - Comfortably Numb",
            "Led Zeppelin - Kashmir",
            "Queen - Bohemian Rhapsody"
        ));

        System.out.println("Initial State: " + player.getCurrentState().getStateName());

        // 1. Play from stopped
        player.play();
        System.out.println("State: " + player.getCurrentState().getStateName());

        // 2. Pause
        player.pause();
        System.out.println("State: " + player.getCurrentState().getStateName());

        // 3. Lock while paused
        player.toggleLock();
        System.out.println("State: " + player.getCurrentState().getStateName());

        // 4. Try pressing play while locked
        player.play();

        // 5. Unlock (restores to paused)
        player.toggleLock();
        System.out.println("Restored State: " + player.getCurrentState().getStateName());

        // 6. Resume playback
        player.play();
        System.out.println("State: " + player.getCurrentState().getStateName());

        // 7. Stop
        player.stop();
        System.out.println("Final State: " + player.getCurrentState().getStateName());
    }
}
