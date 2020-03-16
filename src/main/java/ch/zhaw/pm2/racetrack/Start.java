package ch.zhaw.pm2.racetrack;

import ch.zhaw.pm2.racetrack.strategy.DoNotMoveStrategy;
import ch.zhaw.pm2.racetrack.strategy.MoveListeStrategy;
import ch.zhaw.pm2.racetrack.strategy.UserStrategy;

import java.io.File;
import java.io.IOException;
import java.util.Objects;


public class Start {
    static IO io = new IO();
    static Track track;
    static final int INDEX_OFFSET = 1;
    Car car = new Car();

    public Start() {

    }

    public static void main(String[] args) {

        setUpGame();
        strategies(); //set strategies for each player
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
            int i = 1;
            io.print("\n" + car.getName() + " what do you want your strategy to be?\n");

            for (Config.StrategyType strategies: Config.StrategyType.values()) {
                io.print(i++ + ": " + strategies.toString());
                io.print("\n");
            }

            int choice = io.intInputReader(1,2,
                    "\nPlease choose [1, " + Config.StrategyType.values().length + "]: ");
            Config.StrategyType strategy = Config.StrategyType.codeOfOption(choice);
            assert strategy != null;

            switch (strategy) {
                case DO_NOT_MOVE:
                    car.setCarMoveStrategy(new DoNotMoveStrategy());
                    break;
                case USER:
                    car.setCarMoveStrategy(new UserStrategy());
                    break;
                case MOVE_LIST:
                    car.setCarMoveStrategy(new MoveListeStrategy());
                    break;
                default:
                    io.print("ups, something went wrong");
            }
        }
    }
}
