package test;

import core.Connect4ComputerPlayer;
import core.Controller;
import core.Model;
import org.junit.jupiter.api.Test;
import ui.Connect4TextConsole;

import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;

class Connect4ComputerPlayerTest {

    @Test
    void makeMove() {
        Model model = new Model();
        Scanner scanner = new Scanner(System.in);
        String input = "";
        Connect4TextConsole console = new Connect4TextConsole(model);
        Controller cntrl = new Controller(model, scanner, input, console);
        Connect4ComputerPlayer enemy = new Connect4ComputerPlayer(cntrl);
//        assertTrue()
    }
}