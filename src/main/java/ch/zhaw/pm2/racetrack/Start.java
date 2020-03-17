package ch.zhaw.pm2.racetrack;

import ch.zhaw.pm2.racetrack.exceptions.InvalidTrackFormatException;
import ch.zhaw.pm2.racetrack.strategy.DoNotMoveStrategy;
import ch.zhaw.pm2.racetrack.strategy.MoveListeStrategy;
import ch.zhaw.pm2.racetrack.strategy.UserStrategy;
import ch.zhaw.pm2.racetrack.Config.StrategyType;

import java.io.File;
import java.io.IOException;
import java.util.Objects;


public class Start {
    static IO io = new IO();
    static Track track;
    static Game game;
    static final int INDEX_OFFSET = 1;
    static File file;

    public Start() {
    }

    public static void main(String[] args) {
        io.setBookmarkBlankScreen();
        setUpGame();
        strategies(); //set strategies for each player
        game = new Game(track);
        io.refresh(file);
        gamingTime();
    }

    private static void setUpGame() {
        file = new File("tracks");
        String[] tracks = Objects.requireNonNull(file.list());

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
    public static void strategies() {
        for (Car car : track.getCars()) {
            StrategyType strategy = io.strategiesInputReader(
                    "\n" + car.getName() + " what do you want your strategy to be?");

            switch (strategy) {
                case DO_NOT_MOVE:
                    track.setStrategy(new DoNotMoveStrategy(), car);
                    break;
                case USER:
                    track.setStrategy(new UserStrategy(), car);
                    break;
                case MOVE_LIST:
                    track.setStrategy(new MoveListeStrategy(), car);
                    break;
                default:
                    io.print("ups, something went wrong");
            }
        }
    }

    public static void gamingTime() {
        do {
            game.doCarTurn(track.getCar(game.getCurrentCarIndex()).getCarMoveStrategy().nextMove());
        } while (game.NO_WINNER == game.getWinner());

}
}
