package ch.zhaw.pm2.racetrack;

import java.awt.*;
import java.io.File;
import java.util.Objects;


public class Start {
    static Car car = new Car();
    private static Car[] carArray;
    static IO io = new IO();

    public Start() {

    }

    public static void main(String[] args) {

        setUpGame();
        amountOfPlayers();

    }

    private static void setUpGame() {
        File files = new File("tracks");
        String[] tracks = Objects.requireNonNull(files.list());

        io.print("Welcome to Racetrack!\nSelect Track file:\n");
        for (int i = 0; i < tracks.length; ++i) {
            io.print("  " + (i + 1) + ": " + tracks[i] + "\n");
        }
        int track = io.intInputReader(1, tracks.length, "Choose your map [1-" + tracks.length + "]:");
    }

    public static void amountOfPlayers() {
        int players = io.intInputReader(2, 9, "How many players will be playing [2-9]: ");
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
            char name = io.charInputReader("Please give your car a character: ");
            carArray[i] = new Car(position, name);
        }
    }

    /**
     * lets the players decide on their strategies for the game
     */
    public static void strategies() {
        for (Car value : carArray) {
            io.print(value.getName() + " what do you want your strategy to be?");
            io.print("1: DO_NOT_MOVE");
            io.print("2: USER");
            int choice = io.intInputReader(1,2,"Please choose [1,2]: ");
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
                        io.print("This isnt a valid choice, please choose again");
                        invalidChoice = true;
                }
            } while (invalidChoice);
        }
    }
}
