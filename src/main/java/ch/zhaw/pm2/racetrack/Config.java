package ch.zhaw.pm2.racetrack;

import java.io.File;

public class Config {
    public static final int MIN_CARS = 2;
    public static final int MAX_CARS = 9;
    public static final int NUMBER_OF_LAPS = 1;
    private static final String TRACK_DIRECTORY = "tracks";
    private static final String MOVE_LIST_DIRECTORY = "moveLists";

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

    public enum StrategyType {
        DO_NOT_MOVE, USER, MOVE_LIST
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
