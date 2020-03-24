package ch.zhaw.pm2.racetrack;

import java.io.File;

public class Config {
    public static final int MIN_CARS = 2;
    public static final int MAX_CARS = 9;
    private static final String TRACK_DIRECTORY = "tracks";
    private static final String MOVE_LIST_DIRECTORY = "moveLists";
    public static int firstTurnCarIndex = 0;
    static int numberLaps = 1;
    // Directory containing the track files
    private static File trackDirectory = new File(TRACK_DIRECTORY);
    // Directory containing the strategy files
    private static File moveListDirectory = new File(MOVE_LIST_DIRECTORY);

    public static File getTrackDirectory() {
        return trackDirectory;
    }

    public static File getMoveListDirectory() {
        return moveListDirectory;
    }

    /**
     * Set the index of car which goes first.
     *
     * @param firstTurnCarIndex Zero based index of the car which should be first to take turn.
     * @throws IllegalArgumentException firstTurnCarIndex > MAX_CARS or firstTurnCarIndex < MIN_CARS
     */
    public static void setFirstTurnCarIndex(int firstTurnCarIndex) {
        if (firstTurnCarIndex >= MAX_CARS || firstTurnCarIndex < 0) {
            throw new IllegalArgumentException(firstTurnCarIndex + " is illegal car index! Must be in [Min_CARS,MAX_CARS]!");
        }
        Config.firstTurnCarIndex = firstTurnCarIndex;
    }

    /**
     * Set the number of laps.
     *
     * @param numberLaps The number of laps.
     * @throws IllegalArgumentException numberLaps < 1
     */
    public static void setNumberLaps(int numberLaps) throws IllegalArgumentException {
        if (numberLaps < 1) {
            throw new IllegalArgumentException("Number must be greater than zero.");
        }
        Config.numberLaps = numberLaps;
    }

    public enum StrategyType {
        DO_NOT_MOVE, USER, MOVE_LIST, PATH_FOLLOWER
    }

    /**
     * Enum representing possible space types of the grid.
     * The char value is used to parse from the track file and represents
     * the space type in the text representation created by toString().
     */
    public enum SpaceType {
        WALL('#'),
        TRACK(' '),
        FINISH_UP('^'),
        FINISH_DOWN('v'),
        FINISH_LEFT('<'),
        FINISH_RIGHT('>'),
        ANY_CAR(' ');

        private final char value;

        SpaceType(final char c) {
            value = c;
        }

        public char getValue() {
            return value;
        }

        @Override
        public String toString() {
            return String.valueOf(value);
        }
    }

}
