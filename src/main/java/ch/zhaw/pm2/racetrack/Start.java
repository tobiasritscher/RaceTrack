package ch.zhaw.pm2.racetrack;

import ch.zhaw.pm2.racetrack.exceptions.InvalidTrackFormatException;
import ch.zhaw.pm2.racetrack.strategy.DoNotMoveStrategy;
import ch.zhaw.pm2.racetrack.strategy.MoveListStrategy;
import ch.zhaw.pm2.racetrack.strategy.UserStrategy;
import ch.zhaw.pm2.racetrack.Config.StrategyType;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

/**
 * Class starts the game with the needed setup and strategy initialization
 */

public class Start {
    static IO io = new IO();
    static Track track;
    static Game game;
    static final int INDEX_OFFSET = 1;
    static File file;

    public Start() {
    }

    public static void main(String[] args) throws IOException {
        io.setBookmarkBlankScreen();
        setUpGame();
        strategies(); //set strategies for each player
        game = new Game(track);
        io.refresh(track);
        gamingTime();
    }

    /**
     * loads the chosen track from the players
     */
        private static void setUpGame() {
        file = Config.getTrackDirectory();
        String[] tracks = Objects.requireNonNull(file.list());
        Config.setTrackDirectory(file);

        io.print("Welcome to Racetrack!\nSelect Track file:\n");
        for (int i = 0; i < tracks.length; ++i) {
            io.print("  " + (i + INDEX_OFFSET) + ": " + tracks[i] + "\n");
        }
        //choose Track from files
        int trackChosen = io.intInputReader(1, tracks.length, "Choose your map [1-" + tracks.length + "]:") - INDEX_OFFSET;
        file = new File("tracks/" + tracks[trackChosen]);
        try {
            track = new Track(file);
        } catch (IOException | InvalidTrackFormatException e) {
            e.printStackTrace();
        }
    }


    /**
     * lets the players decide on their strategies for the game
     */
    public static void strategies() throws IOException {
        for (Car car : track.getCars()) {
            StrategyType strategy = io.strategiesInputReader(
                    "\n" + car.getId() + " what do you want your strategy to be?");

            switch (strategy) {
                case DO_NOT_MOVE:
                    track.setStrategy(new DoNotMoveStrategy(), car);
                    break;
                case USER:
                    track.setStrategy(new UserStrategy(), car);
                    break;
                case MOVE_LIST:
                    track.setStrategy(new MoveListStrategy(new File("strategies/quarter_mile_moves.txt" )), car);
                    break;
                default:
                    io.print("ups, something went wrong");
            }
        }
    }

    /**
     * lets the players play the actual game until a winner gets chosen
     */
    public static void gamingTime() {
        do {
            Car currentCar = track.getCar(game.getCurrentCarIndex());
            io.print(currentCar.getId() + ": ");
            game.doCarTurn(currentCar.getCarMoveStrategy().nextMove());
            io.refresh(track);
        } while (Game.NO_WINNER == game.getWinner());
        io.print(game.getCarId(game.getWinner()) + " is the winner");
        io.promptEnter("Hit 'Enter' to quit the game");
        io.closeTerminal();
    }
}
