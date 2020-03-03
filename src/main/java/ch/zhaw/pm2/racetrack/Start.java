package ch.zhaw.pm2.racetrack;

import org.beryx.textio.TextIO;
import org.beryx.textio.TextIoFactory;
import org.beryx.textio.TextTerminal;
import org.beryx.textio.swing.SwingTextTerminal;

import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Objects;


public class Start {
    private static TextIO textIO = TextIoFactory.getTextIO();
    private static TextTerminal<SwingTextTerminal> textTerminal = (SwingTextTerminal) textIO.getTextTerminal();
    static Car car = new Car();
    private static Car[] carArray;

    public Start() {

    }

    public static void main(String[] args) {
        try {
            setUpGame();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        amountOfPlayers();

    }

    private static void setUpGame() throws FileNotFoundException {
        File files = new File("tracks");
        String[] tracks = Objects.requireNonNull(files.list());
        for (int i = 0; i < tracks.length; ++i){
            textTerminal.print(i + 1 + ": " + tracks[i] + "\n");
        }
        textIO.newIntInputReader().withMinVal(1).withMaxVal(tracks.length)
                .read("Choose your map [1-" + tracks.length + "]:");
    }

    public static void amountOfPlayers() {
        Integer players = textIO.newIntInputReader().withMinVal(2).withMaxVal(9).read("How many players will be playing?");
        carArray = new Car[players];
        for (int i = 0; i < players; ++i) {
            carArray[i] = new Car();
        }
    }

    /**
     * Gives each car its name and depending on the track its position
     *
     * @param position
     */
    public static void nameOfPlayers(Point position) {
        for (int i = 0; i < carArray.length; ++i) {
            char name = textIO.newCharInputReader().read("Please give your car a character");
            carArray[i] = new Car(position, name);
        }
    }

    /**
     * lets the players decide on their strategies for the game
     */
    public static void strategies() {
        for (Car value : carArray) {
            textTerminal.print(value.getName() + " what do you want your strategy to be?");
            textTerminal.print("1: DO_NOT_MOVE");
            textTerminal.print("2:USER");
            Integer choice = textIO.newIntInputReader().read("Please choose");
            boolean invalidChoice = false;
            do {
                switch (choice) {
                    case 1:
                        value.byMachine();
                        break;
                    case 2:
                        // TODO implement user strategy
                        break;
                    default:
                        textTerminal.print("This isnt a valid choice, please choose again");
                        invalidChoice = true;
                }
            } while (invalidChoice);
        }
    }
}
