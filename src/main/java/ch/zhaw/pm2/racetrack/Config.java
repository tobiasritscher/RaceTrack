package ch.zhaw.pm2.racetrack;

import java.io.File;

public class Config {
    public static final int MIN_CARS = 2;
    public static final int MAX_CARS = 9;

    // Directory containing the track files
    private static File trackDirectory = new File("tracks");
    // Directory containing the strategy files
    private static File strategyDirectory;

    public static File getTrackDirectory() {
        return trackDirectory;
    }

    public static void setTrackDirectory(File trackDirectory) {
        Config.trackDirectory = trackDirectory;
    }

    public static File getStrategyDirectory() {
        return strategyDirectory;
    }

    public static void setStrategyDirectory(File strategyDirectory) {
        Config.strategyDirectory = strategyDirectory;
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
