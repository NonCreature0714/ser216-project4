/**
 * Connect4ComputerPlayer
 *
 * Plays against real player.
 *
 * @author William Brubaker
 *
 * @version 0.1.0
 *
 */

package core;

import java.util.concurrent.ThreadLocalRandom;

public class Connect4ComputerPlayer {
    private Controller controller;

    /**
     * Constructor
     *
     * @param controller is a reference to existing model.
     */
    public Connect4ComputerPlayer(Controller controller) {
        this.controller = controller;
    }

    /**
     * Make a move on the model.
     *
     * @return a randomly selected column.
     */
    public int makeMove() {
        return ThreadLocalRandom.current().nextInt(1, 7 + 1);
    }
}
