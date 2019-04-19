package core;

import ui.Connect4TextConsole;

import java.util.Scanner;

/**
 * Controller.
 *
 * Passes commands from user to view, and from user to
 * model.
 */
public class Controller {
    Model model;
    Scanner scanner;
    String input;
    Connect4TextConsole view;

    /**
     * Controller constructor.
     * @param model reference to current model
     * @param scanner reference to current scanner.
     * @param input reference to input.
     * @param view reference to current view.
     */
    public Controller(Model model, Scanner scanner, String input, Connect4TextConsole view) {
        this.model = model;
        this.scanner = scanner;
        this.input = input;
        this.view = view;
    }

    /**
     * getInput
     *
     * Retrieves inupt from user.
     * @return Input string from user.
     */
    public String getInput(){
        return getValidInput();
    }

    /**
     * getValidInput
     *
     * Retrieves a valid user input value.
     * @return a valid String.
     */
    public String getValidInput() {
        input = scanner.nextLine().trim();
        while(!checkInput(input)) {
            view.message("col_limited");
            input = scanner.nextLine().trim();
        }
        return input;
    }

    /**
     * checkInput
     *
     * Make sure the input is allowed and only one character.
     *
     * @param input from the console.
     * @return true if the input is correct.
     */
    public boolean checkInput(String input) {
        return input.matches("[1-7Qq]{1}");
    }

    /**
     * Place a piece into a valid column.
     * @param column the player selected column.
     */
    public void placeInValidColumn(String column) {
        if (!column.equals("q")){
            while(!model.placePiece(model.xORo, convertStringDigitToInt(column))){
                view.message("column_full");
                column = getValidInput();
            }
        }
    }

    /**
     * convertStringDigitToInt
     *
     * @param digit A stringified numeric digit.
     * @return an Integer parsed from digit.
     */
    public int convertStringDigitToInt(String digit) {
        return Integer.parseInt(digit);
    }

    public boolean checkForGameOver(Model model) {
        return model.checkForFullModel() || model.checkForMatchingFour();
    }
}