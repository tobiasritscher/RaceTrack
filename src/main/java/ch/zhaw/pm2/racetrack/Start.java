package ch.zhaw.pm2.racetrack;

import ch.zhaw.pm2.racetrack.Config.StrategyType;
import ch.zhaw.pm2.racetrack.exceptions.InvalidTrackFormatException;
import ch.zhaw.pm2.racetrack.strategy.DoNotMoveStrategy;
import ch.zhaw.pm2.racetrack.strategy.MoveListStrategy;
import ch.zhaw.pm2.racetrack.strategy.UserStrategy;

import java.io.File;
import java.io.IOException;

/**
 * Class starts the game with the needed setup and strategy initialization
 */
public class Start {
    static final int INDEX_OFFSET = 1;
    static IO io = new IO();
    static Track track;
    static Game game;
    static File trackFile;
    static String[] tracks;
    static File moveFile;
    static String[] moveLists;

    /**
     * initalises the start and the game, saves the predifend directories (Config.java) and the files in there
     */
    public Start() {
        trackFile = Config.getTrackDirectory();
        tracks = trackFile.list();

        moveFile = Config.getMoveListDirectory();
        moveLists = moveFile.list();
    }

    /**
     * main function of the game
     *
     * @param args from the terminal (not used in this program)
     * @throws IOException if scanner from strategies() can't be initalised
     */
    public static void main(String[] args) throws IOException {
        io.setBookmarkBlankScreen();
        setUpGame();
        strategies();

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
     *
     * @throws IOException if scanner in MoveListStrategy() can't be initalised
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

    /**
     * Let the player choose a Movleist from the defined directory
     *
     * @return the chosen file
     */
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
