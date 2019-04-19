package core;

/**
 * Model
 *
 * model, aka, the game state information.
 */
public class Model {
    public static int ModelRowCount = 6;
    public static int ModelColumnCount = 8;
    public String xORo;
    public char [][] model;
    public int positionLatestRow;
    public int positionLatestColumn;
    public char lastPieceSymbol;

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
    public void placeCharacterInColumn(int row, int column, char ch) {
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
    public void captureLastMove(int row, int column, char ch) {
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
    public boolean placementAvailable(int row, int col) {
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
    public boolean checkHorizontal() {
        return checkFourInSequence(positionLatestRow, 0, 0, 1);
    }

    /**
     *
     * checkVertical
     *
     * @return true if there are four in sequence vertically.
     */
    public boolean checkVertical() {
        return checkFourInSequence(0, positionLatestColumn, 1, 0);
    }

    /**
     *
     * checkNegativeDiagonal
     *
     * @return true if there are for in sequence on a Cartesian
     * negative slope.
     */
    public boolean checkNegativeDiagonal() {
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
    public boolean checkPositiveDiagonal() {
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
    public boolean checkFourInSequence(int startRow, int startCol, int stepRow, int stepCol){
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
    public boolean rowAndColumnIndexInRange(int rowIndex, int colIndex) {
        return rowIndexIsInRange(rowIndex) && columnIndexIsInRange(colIndex);
    }

    /**
     * rowIndexIsInRange
     *
     * @param rowIndex row placement.
     * @return true if index is good.
     */
    public boolean rowIndexIsInRange(int rowIndex) {
        return rowIndex < ModelRowCount && rowIndex >= 0;
    }

    /**
     * columnIndexIsInRange
     *
     * @param colIndex column placement
     * @return true if column is good.
     */
    public boolean columnIndexIsInRange(int colIndex) {
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
    public int length() { return model.length; }

    /**
     * height
     *
     * @return height of model.
     */
    public int height() { return model[0].length; }
}