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
    static File trackFile;
    static String[] tracks;
    static File moveFile;
    static String[] moveLists;

    public Start() {
    }

    public static void main(String[] args) throws IOException {
        trackFile = Config.getTrackDirectory();
        tracks = Objects.requireNonNull(trackFile.list());

        moveFile = Config.getMoveListDirectory();
        moveLists = moveFile.list();

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
        io.print("Welcome to Racetrack!\nSelect Track file:\n");
        for (int i = 0; i < tracks.length; ++i) {
            io.print("  " + (i + INDEX_OFFSET) + ": " + tracks[i] + "\n");
        }
        //choose Track from files
        int trackChosen = io.intInputReader(1, tracks.length, "Choose your map [1-" + tracks.length + "]:") - INDEX_OFFSET;
        trackFile = new File(Config.getTrackDirectory() + "/" + tracks[trackChosen]);
        try {
            track = new Track(trackFile);
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
                    track.setStrategy(new MoveListStrategy(chooseMoveList()), car);
                    break;
                default:
                    io.print("ups, something went wrong");
            }
        }
    }

    public static File chooseMoveList() {
        io.print("\nAll files:\n");
        assert moveLists != null;
        for (int i = 0; i < moveLists.length; ++i) {
            io.print("  " + (i + INDEX_OFFSET) + ": " + moveLists[i] + "\n");
        }
        //choose moveList from files
        int moveListChosen = io.intInputReader(1, moveLists.length, "Choose your file [1-" + moveLists.length + "]:") - INDEX_OFFSET;

        return new File(Config.getMoveListDirectory() + "/" + moveLists[moveListChosen]);
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
