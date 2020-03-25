package ch.zhaw.pm2.racetrack;

import ch.zhaw.pm2.racetrack.Config.StrategyType;
import ch.zhaw.pm2.racetrack.exceptions.InvalidTrackFormatException;
import ch.zhaw.pm2.racetrack.strategy.DoNotMoveStrategy;
import ch.zhaw.pm2.racetrack.strategy.MoveListStrategy;
import ch.zhaw.pm2.racetrack.strategy.PathFollowerStrategy;
import ch.zhaw.pm2.racetrack.strategy.UserStrategy;

import java.io.File;
import java.io.IOException;

import static ch.zhaw.pm2.racetrack.Config.StrategyType.PATH_FOLLOWER;

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
     * main function of the game
     *
     * @param args from the terminal (not used in this program)
     * @throws IOException if scanner from strategies() can't be initalised
     */
    public static void main(String[] args) throws IOException {
        trackFile = Config.getTrackDirectory();
        tracks = trackFile.list();

        moveFile = Config.getMoveListDirectory();
        moveLists = moveFile.list();

        io.setBookmarkBlankScreen();
        setUpGame();
        strategies();
        io.promptEnter("Hit 'Enter' to start the game: ");

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
                case PATH_FOLLOWER:
                    track.setStrategy(new PathFollowerStrategy(chooseFile(PATH_FOLLOWER)), car);
                    break;
                default:
                    io.print("ups, something went wrong");
            }
        }
    }

    public static File chooseFile(StrategyType strategyType) {
        String filePath;
        switch (strategyType) {
            case PATH_FOLLOWER:
                filePath = Config.getPathFollowerDirectoryPath();
                break;
            default:
                throw new RuntimeException("There are no lists for given strategy.");
        }
        assert filePath != null && new File(filePath).isDirectory() : "Directory was not set properly. Please check Config.java.";
        String[] files = (new File(filePath)).list();
        io.print("\nAll files:\n");
        for (int i = 0; i < files.length; ++i) {
            io.print("  " + (i + INDEX_OFFSET) + ": " + files[i] + "\n");
        }
        //choose moveList from files
        int fileChoosen = io.intInputReader(1, files.length, "Choose your file [1-" + files.length + "]:") - INDEX_OFFSET;
        return new File(filePath + "\\" + files[fileChoosen]);
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
            if (currentCar.isCrashed() && game.getWinner() == Game.NO_WINNER) {
                io.print(currentCar.getId() + " is crashed!\n");
            }
        } while (Game.NO_WINNER == game.getWinner());
        io.print(game.getCarId(game.getWinner()) + " is the winner");
        io.promptEnter("Hit 'Enter' to quit the game");
        io.closeTerminal();
    }
}
