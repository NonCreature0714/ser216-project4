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

import javafx.stage.Stage;
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

    /**
     * Model
     *
     * model, aka, the game state information.
     */
    public static class Model {
        private static int ModelRowCount = 6;
        private static int ModelColumnCount = 8;
        public String xORo;
        private char [][] model;
        private int positionLatestRow;
        private int positionLatestColumn;
        private char lastPieceSymbol;

        public Model() {
            model = new char[ModelRowCount][ModelColumnCount];
            for (int i = 0; i < ModelRowCount; i++) {
                for (int j = 0; j < ModelColumnCount; j++) {
                    model[i][j] = ' ';
                }
            }
        }

        /**
         * placePiece
         *
         * @param xORo the player's token.
         * @param atColumn where the player is placing the token.
         * @return returns true if the piece is properly placed.
         */
        public boolean placePiece(String xORo, int atColumn) {
            println("in Model, placePiece, at column " + atColumn);
            if (atColumn > model[0].length) {
                throw new IndexOutOfBoundsException("Can't insert at Out-of-Bounds column " + atColumn);
            }

            for (int i = ModelRowCount-1; i >= 0; i--) {
                if(placementAvailable(i, atColumn)) {
                    placeCharacterInColumn(i, atColumn, xORo.charAt(0));
                    return true;
                }
            }
            return false;
        }

        /**
         *
         * placeCharacterInColumn
         *
         * @param row row height.
         * @param column desired column.
         * @param ch the piece to place.
         */
        private void placeCharacterInColumn(int row, int column, char ch) {
            model[row][column] = ch;
            captureLastMove(row, column, ch);
        }

        /**
         * captureLastMove
         *
         * @param row value of last move row.
         * @param column value of last move column
         * @param ch value of last move emplaced piece.
         */
        private void captureLastMove(int row, int column, char ch) {
            positionLatestRow = row;
            positionLatestColumn = column;
            lastPieceSymbol = ch;
        }

        /**
         *
         * checkForFullModel
         *
         * @return true if the model has no more spaces to place
         * pieces.
         */
        public boolean checkForFullModel(){
            boolean gameOver = true;
            for(int i = 0; i < ModelColumnCount; i++) {
                if(placementAvailable(0, i)) {
                    gameOver = false;
                }
            }
            return gameOver;
        }

        /**
         *
         * placementAvailable
         *
         * @param row index to the desired row.
         * @param col index to the desired column.
         * @return true if the space is empty.
         */
        private boolean placementAvailable(int row, int col) {
            return model[row][col] == ' ';
        }

        /**
         *
         * checkForMatchingFour
         *
         * @return true if any sequence of 4 is present.
         */
        public boolean checkForMatchingFour() {
            return checkHorizontal() || checkVertical()
                    || checkNegativeDiagonal() || checkPositiveDiagonal();
        }

        /**
         *
         * checkHorizontal
         *
         * @return true if there is a four-in-a-row sequence.
         */
        private boolean checkHorizontal() {
            return checkFourInSequence(positionLatestRow, 0, 0, 1);
        }

        /**
         *
         * checkVertical
         *
         * @return true if there are four in sequence vertically.
         */
        private boolean checkVertical() {
            return checkFourInSequence(0, positionLatestColumn, 1, 0);
        }

        /**
         *
         * checkNegativeDiagonal
         *
         * @return true if there are for in sequence on a Cartesian
         * negative slope.
         */
        private boolean checkNegativeDiagonal() {
            int startRow = 0;
            int startCol = 0;

            if (positionLatestRow > positionLatestColumn) {
                startRow =  positionLatestRow - positionLatestColumn;
            }

            if (positionLatestRow < positionLatestColumn) {
                startCol =  positionLatestColumn - positionLatestRow;
            }

            return checkFourInSequence(startRow, startCol, 1, 1);
        }

        /**
         *
         * checkPositiveDiagonal
         *
         * @return true if there are for in sequence on a Cartesian
         * positive slope.
         */
        private boolean checkPositiveDiagonal() {
            int startRow = 0;
            int startCol = 5;

            if ((positionLatestRow + positionLatestColumn) < height() - 1) {
                startCol = positionLatestRow + positionLatestColumn;
            }

            if ((positionLatestRow + positionLatestColumn) > height() - 1) {
                startRow = (positionLatestRow + positionLatestColumn) - length();
                startCol = (positionLatestRow + positionLatestColumn) - startRow;
            }

            return checkFourInSequence(startRow, startCol, 1, -1);
        }

        /**
         *
         * checkFourInSequence
         *
         * @param startRow starting row index on model.
         * @param startCol starting column index on model.
         * @param stepRow direction to go vertically, up or down rows.
         * @param stepCol direction to go horizontally, left or right in columns.
         * @return true if there are four of the same characters in sequence.
         */
        private boolean checkFourInSequence(int startRow, int startCol, int stepRow, int stepCol){
            int matchCount = 0;
            for(int row = startRow, col = startCol; rowAndColumnIndexInRange(row, col); row+=stepRow, col+=stepCol){
                if(model[row][col] == ' '){
                    matchCount = 0;
                }

                if(model[row][col] != lastPieceSymbol) {
                    matchCount = 0;
                }

                if(model[row][col] == lastPieceSymbol) {
                    matchCount++;
                }

                if(matchCount == 4) {
                    return true;
                }
            }
            return false;
        }

        /**
         * rowAndColumnIndexInRange
         *
         * @param rowIndex move's row height.
         * @param colIndex move's column.
         * @return
         */
        private boolean rowAndColumnIndexInRange(int rowIndex, int colIndex) {
            return rowIndexIsInRange(rowIndex) && columnIndexIsInRange(colIndex);
        }

        /**
         * rowIndexIsInRange
         *
         * @param rowIndex row placement.
         * @return true if index is good.
         */
        private boolean rowIndexIsInRange(int rowIndex) {
            return rowIndex < ModelRowCount && rowIndex >= 0;
        }

        /**
         * columnIndexIsInRange
         *
         * @param colIndex column placement
         * @return true if column is good.
         */
        private boolean columnIndexIsInRange(int colIndex) {
            return colIndex < ModelColumnCount && colIndex >= 0;
        }

        /**
         * get
         *
         * @param row row index to retrieve.
         * @param column column to retrieve.
         * @return returns the character at the indicated access point.
         */
        public char get(int row, int column) throws IndexOutOfBoundsException {
            if (row < 0 || row >= model.length) {
                throw new IndexOutOfBoundsException("Row index: " + row + " OBE.");
            }

            if (column < 0 || column >= model[0].length) {
                throw new IndexOutOfBoundsException("Column index: " + column + " OBE.");
            }

            return model[row][column];
        }

        /**
         * length
         *
         * @return length of model.
         */
        private int length() { return model.length; }

        /**
         * height
         *
         * @return height of model.
         */
        private int height() { return model[0].length; }
    }

    /**
     * Controller.
     *
     * Passes commands from user to view, and from user to
     * model.
     */
    public static class Controller {
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
        Controller(Model model, Scanner scanner, String input, Connect4TextConsole view) {
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
        private String getValidInput() {
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
        private boolean checkInput(String input) {
            return input.matches("[1-7q]{1}");
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
        private int convertStringDigitToInt(String digit) {
            return Integer.parseInt(digit);
        }

        public boolean checkForGameOver(Model model) {
            return model.checkForFullModel() || model.checkForMatchingFour();
        }
    }

    private static void print(String message) {
        System.out.print(message);
    }

    private static void println(String message) {
        print(message + "\n");
    }
}
