package ch.zhaw.pm2.racetrack;

import java.io.File;

public class Config {
    public static final int MAX_CARS = 9;

    // Directory containing the track files
    private File trackDirectory = new File("tracks");

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
        FINISH_RIGHT('>');

        private final char value;
        SpaceType(final char c) {
            value = c;
        }
    }

    public File getTrackDirectory() {
        return trackDirectory;
    }

    public void setTrackDirectory(File trackDirectory) {
        this.trackDirectory = trackDirectory;
    }

}
