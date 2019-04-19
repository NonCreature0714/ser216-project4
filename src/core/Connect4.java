/**
 *
 * Connect4
 *
 * A simple connect 4 console game.
 *
 * @author William Brubaker
 * @version 0.6.0
 */
package core;

import java.util.Scanner;

import ui.Connect4GUI;
import ui.Connect4TextConsole;

/**
 * main class.
 *
 */
public class Connect4 {
    static String input;
    static Scanner scanner = new Scanner(System.in);
    static final String [] player = {"X", "O"};

    public static void main(String[] args) {
        int countTurns = 0;
        boolean gameOver;

        Model model = new Model();
        Connect4TextConsole view = new Connect4TextConsole(model);
        Controller controller = new Controller(model, scanner, input, view);
        Connect4ComputerPlayer adversary = new Connect4ComputerPlayer(controller);

        String gameType = null;
        String gameView = null;
        try {
            do {
                view.message("game_type");
                gameType = scanner.nextLine().trim();
            } while (!gameType.matches("[PpCc]{1}"));


            do {
                view.message("game_view");
                gameView = scanner.nextLine().trim();
            } while (!gameView.matches("[GgCc]{1}"));
        } catch (Exception e) {
            println("There was an error gathering input.");
            System.exit(1);
        }

        try {
            if (gameView.matches("[Cc]{1}")) {
                do {
                    model.xORo = player[countTurns%2];
                    view.draw();
                    view.message("player_turn");

                    if (gameType.matches("[Pp]{1}") ) {
                        input = controller.getInput();
                        controller.placeInValidColumn(input);
                    } else if (gameType.matches("[Cc]{1}") && model.xORo == "X") {
                        input = controller.getInput();
                        controller.placeInValidColumn(input);
                    } else {
                        model.placePiece(model.xORo, adversary.makeMove());
                    }

                    gameOver = controller.checkForGameOver(model);
                    countTurns++;
                } while (!input.matches("[Qq]{1}") && !gameOver);
                scanner.close();
                view.draw();
                view.message("game_over");
                view.message("player_won");
            } else if (gameView.matches("[Gg]{1}")){
                Connect4GUI gui = new Connect4GUI();
                gui.play();
            }
        } catch (Exception e) {
            System.out.println("The game was interrupted by an unknown error.");
        }
    }

    public static void print(String message) {
        System.out.print(message);
    }

    public static void println(String message) {
        print(message + "\n");
    }
}
