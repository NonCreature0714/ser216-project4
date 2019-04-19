package test;

import core.Controller;
import core.Model;
import org.junit.jupiter.api.Test;
import ui.Connect4TextConsole;

import java.io.ByteArrayInputStream;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;

class ControllerTest {

    @Test
    void checkInput() {
        Model model = new Model();
        Scanner scanner = new Scanner(System.in);
        String input = "";
        Controller controller = new Controller(model, scanner, input, new Connect4TextConsole(model));

        assertTrue(controller.checkInput("1"));
        assertFalse(controller.checkInput("0"));
        assertTrue(controller.checkInput("7"));
        assertFalse(controller.checkInput("8"));
        assertTrue(controller.checkInput("Q"));
        assertTrue(controller.checkInput("q"));
        assertFalse(controller.checkInput("w"));

    }

    @Test
    void convertStringDigitToInt() {
        Model model = new Model();
        Scanner scanner = new Scanner(System.in);
        String input = "";
        Controller controller = new Controller(model, scanner, input, new Connect4TextConsole(model));

        assertEquals(1, controller.convertStringDigitToInt("1"));
    }
}