package test;

import core.Model;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ModelTest {

    @Test
    void placePiece() {
        Model model = new Model();
        assertTrue(model.placePiece("X", 1));
        assertThrows(java.lang.ArrayIndexOutOfBoundsException.class, () -> {
            model.placePiece("X", -1);
        });
        assertTrue(model.placePiece("X", 0));
    }

    @Test
    void placeCharacterInColumn() {
        Model model = new Model();
        model.placeCharacterInColumn(0, 0, 'X');
        assertEquals('X', model.get(0,0));

    }


    @Test
    void placementAvailable() {
        Model model = new Model();
        assertTrue(model.placementAvailable(0, 0));
        model.placePiece("X", 0);
        assertTrue(model.placementAvailable(0, 0));
    }
}