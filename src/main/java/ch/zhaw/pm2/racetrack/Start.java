package ch.zhaw.pm2.racetrack;

import java.io.File;
import java.io.IOException;
import java.util.Objects;


public class Start {
    static IO io = new IO();
    static Track track;
    static final int INDEX_OFFSET = 1;

    public Start() {

    }

    public static void main(String[] args) {

        setUpGame();
    }

    private static void setUpGame() {
        File files = new File("tracks");
        String[] tracks = Objects.requireNonNull(files.list());

        io.print("Welcome to Racetrack!\nSelect Track file:\n");
        for (int i = 0; i < tracks.length; ++i) {
            io.print("  " + (i + INDEX_OFFSET) + ": " + tracks[i] + "\n");
        }
        //choose Track from files
        int trackChosen = io.intInputReader(1, tracks.length, "Choose your map [1-" + tracks.length + "]:") - INDEX_OFFSET;

        try {
            track = new Track(new File("tracks/" + tracks[trackChosen]));
        } catch (IOException | InvalidTrackFormatException e) {
            e.printStackTrace();
        }
    }


    /**
     * lets the players decide on their strategies for the game
     */
    public static void strategies() {
        for (Car car: track.getCars()) {
            io.print(car.getName() + " what do you want your strategy to be?");
            io.print("1: DO_NOT_MOVE");
            io.print("2: USER");
            int choice = io.intInputReader(1,2,"Please choose [1,2]: ");
            boolean invalidChoice = false;
            do {
                switch (choice) {
                    case 1:

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
