/**
 * Connect4TextConsole
 *
 * Presents a non-interactive view of the data.
 * @author William Brubaker
 * @version 0.3.0
 */
package ui;

import core.Model;

/**
 * Connect4TextConsole
 *
 * Handles presenting the game to the user.
 *
 *
 */
public class Connect4TextConsole {
    private char [][] view;
    private Model board;

    /**
     * Connect4TextConsole
     *
     * @param model a reference to the current model.
     */
    public Connect4TextConsole(Model model){
        this.view = new char[6][18];
        this.board = model;
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 17; j++) {
                if (j % 2 == 0) {
                    this.view[i][j] = '|';
                } else {
                    this.view[i][j] = ' ';
                }
            }
            view[i][15] = '\n';
        }
    }

    /**
     * draw
     *
     * Display the current view.
     */
    public void draw(){
        for (int i = 0; i < 6; i++) {
            for (int j = 2; j < 18; j++) {
                printChar(i, j);
            }
            println("");
        }
    }

    /**
     * printChar
     *
     * Puts one character on screen.
     *
     * @param row index to printable character
     * @param column column index to printable character.
     */
    public void printChar(int row, int column){
        if(column % 2 != 0){
            view[row][column] = board.get(row, getIndexForModelFromViewIndex(column));
        }
        print(view[row][column]);
    }

    /**
     *
     * message
     *
     * Displays pre-formatted messages.
     *
     * @param message request which message to display.
     */
    public void message(String message){
        switch (message){
            case "game_over":
                println("Game over.");
                break;
            case "col_limited":
                println("You must only enter 1 - 7. Try again.");
                break;
            case "column_full":
                println("That column is full. Choose another");
                break;
            case "player_turn":
                println("Player" + board.xORo + ", your turn. Choose a column number 1-7.");
                break;
            case "player_won":
                println("Player" + board.xORo + " won.");
                break;
            case "game_type":
                println("Please type P for Player-versus-player, and C for Player-versus-Computer.");
                break;
            case "game_view":
                println("Please type C for console, and G for GUI.");
                break;
            default:
                println("unknown case given.");
                break;
        }
    }

    /**
     * Helps the model interface with the view.
     *
     * @param viewIndex is an index from the view.
     * @return An index to the model.
     */
    private int getIndexForModelFromViewIndex(int viewIndex) {
        if (viewIndex < 2) {
            return 0;
        }
        return (viewIndex / 2) - 1;
    }

    /**
     * Abstraction of System.out.println.
     *
     * @param message Any printable Object.
     */
    private void println(Object message) {
        System.out.println(message);
    }

    /**
     * Abstraction of System.out.print.
     *
     * @param message Any printable Object.
     */
    private void print(Object message) {
        System.out.print(message);
    }
}
