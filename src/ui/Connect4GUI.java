/**
 * @author Bill Brubaker
 * @version 0.0.3
 */

package ui;

import core.Model;

import javafx.animation.TranslateTransition;
import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.effect.Light;
import javafx.scene.effect.Lighting;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Displays a graphical user interface which
 * is a game.
 */
public class Connect4GUI extends Application {
    private static final int COLUMNS = 7;
    private static final int ROWS = 6;
    private static final int DISK_SIZE = 80;
    private static final int TILE_SIZE = 80;
    private Model model;
    private Disk[][] grid = new Disk[COLUMNS][ROWS];
    private Color xTextColor = Color.RED;
    private Color oTextColor = Color.BLUE;
    private boolean xMove = true;
    private Pane diskRoot;

    /**
     * Disk
     *
     * The object that is a player's game piece.
     */
    private class Disk extends Circle {
        private String xORo;

        public Disk(String piece) {
            super(DISK_SIZE/2, piece.equals("X") ? xTextColor : oTextColor);
            this.xORo = piece;
            setCenterX(DISK_SIZE/2);
            setCenterY(DISK_SIZE/2);
        }
    }

    /**
     * Creates the entire game window.
     *
     * @return a new pane for use as the root view.
     */
    private Parent createStage() {
        Pane root = new Pane();
        diskRoot = new Pane();
        root.getChildren().add(this.diskRoot);
        Shape gridShape = createGrid();
        root.getChildren().add(gridShape);

        try {
            root.getChildren().addAll(makeColumns());
        } catch (Exception e) {
            println("Could not add columns to root view");
            System.exit(1);
        }

        return root;
    }

    /**
     *
     * @return returns a List of rectangles, which hold pieces of the game.
     * @throws Exception because it should.
     */
    private List<Rectangle> makeColumns() throws Exception {
        List<Rectangle> list = new ArrayList<>();
        for (int x = 0; x < COLUMNS; x++) {
            Rectangle rect = new Rectangle(TILE_SIZE, (ROWS + 1) * TILE_SIZE);
            rect.setTranslateX(x * (TILE_SIZE + 5) + TILE_SIZE / 4);
            rect.setFill(Color.TRANSPARENT);
            rect.setOnMouseEntered(e -> rect.setFill(Color.rgb(236,167,44, 0.3)));
            rect.setOnMouseExited(e -> rect.setFill(Color.TRANSPARENT));
            final int column = x;
            rect.setOnMouseClicked(e -> placeDisk(new Disk(xMove ? "X" : "O"), column));
            list.add(rect);
        }
        return list;
    }

    /**
     * placeDisk
     *
     * Places a disk into the model, and also emplaces it in the view.
     *
     * @param disk which has already been defined as red or blue.
     * @param column where the piece is to be placed.
     *
     */
    private void placeDisk(Disk disk, int column) {
        println("Column " + column);
        model.placePiece(disk.xORo, column);

        int row = ROWS - 1;
        do {
            if (!getDisk(column, row).isPresent())
                break;
            row--;
        } while (row >= 0);

        if (row < 0)
            return;

        grid[column][row] = disk;
        diskRoot.getChildren().add(disk);
        disk.setTranslateX(column * (TILE_SIZE + 5) + TILE_SIZE / 4);

        TranslateTransition animation = new TranslateTransition(Duration.seconds(0.5), disk);
        animation.setToY(row * (TILE_SIZE + 5) + TILE_SIZE / 4);
        animation.setOnFinished(e -> {
            if (model.checkForMatchingFour() || model.checkForFullModel()) {
                gameOver();
            }

            xMove = !xMove;
        });
        animation.play();
    }

    /**
     * gameOver
     *
     * Display result in console.
     *
     * Exit program.
     *
     */
    private void gameOver() {
        println("Winner: " + (xMove ? "RED" : "BLUE"));
        System.exit(0);
    }

    /**
     * getDisk
     *
     * @apiNote gets the disk available at the given indexes, or null if not.
     *
     * @param column
     * @param row
     * @return single disk.
     */
    private Optional<Disk> getDisk(int column, int row) {
        if (column < 0 || column >= COLUMNS || row < 0 || row >= ROWS) {
            return Optional.empty();
        }

        return Optional.ofNullable(grid[column][row]);
    }

    /**
     * createGrid
     *
     * @apiNote Creates the grid for the disks to be placed into.
     * @return returns the drawn playing field to the stage.
     */
    private Shape createGrid() {
        Shape rect = new Rectangle((COLUMNS + 1) * TILE_SIZE, (ROWS + 1) * TILE_SIZE);
        for (int y = 0; y < ROWS; y++) {
            for (int x = 0; x < COLUMNS; x++) {
                Circle circle = new Circle(TILE_SIZE/2);
                circle.setCenterX(TILE_SIZE/2);
                circle.setCenterY(TILE_SIZE/2);
                circle.setTranslateX(x * (TILE_SIZE + 5) + TILE_SIZE / 4);
                circle.setTranslateY(y * (TILE_SIZE + 5) + TILE_SIZE / 4);
                rect = Shape.subtract(rect, circle);
            }
        }

        Light.Distant light = new Light.Distant();
        light.setAzimuth(45.0);
        light.setElevation(30.0);

        Lighting lighting = new Lighting();
        lighting.setLight(light);
        lighting.setSurfaceScale(5.0);

        rect.setFill(Color.BLUE);
        rect.setEffect(lighting);

        return rect;
    }

    /**
     * start
     *
     * Default function called by Application.
     *
     * @param stage a new stage for the window.
     * @throws Exception if start fails.
     */
    @Override
    public void start(Stage stage) throws Exception {
        model = new Model();
        stage.setTitle("Connect 4");
        stage.setScene(new Scene(createStage()));
        stage.show();
    }

    /**
     * play
     *
     * Call Applications launch function.
     * */
    public void play() {
        launch();
    }

    private void print(String message) {
        System.out.print(message);
    }

    private void println(String message) {
        print(message + "\n");
    }
}
