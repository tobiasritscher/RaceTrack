package ch.zhaw.pm2.racetrack;

import java.io.File;

public class Config {
    public static final int MAX_CARS = 9;

    // Directory containing the track files
    private File trackDirectory = new File("tracks");

    public enum StrategyType {
        DO_NOT_MOVE(1, "Do not move"),
        USER(2, "User input"),
        MOVE_LIST(3, "Move liste");

        private final int StrategyTypeCode;
        private final String textForUser;

        /**
         * This function returns the chosen option in the enum StrategyType
         * @return The number of the enum
         */
        public int getStrategyTypeCode() {
            return StrategyTypeCode;
        }

        /**
         * This function returns the text for the corresponding text for the user
         * specified in the enum StrategyType
         * @return the text for the user to print
         */
        public String getTextForUser() {
            return textForUser;
        }

        StrategyType(int StrategyTypeCode, String textForUser) {
            this.StrategyTypeCode = StrategyTypeCode;
            this.textForUser = textForUser;
        }

        /**
         * This function returns the value of chosen enum
         * @param label chosen option code
         * @return the enum's value
         */
        public static StrategyType codeOfOption(int label) {
            for (StrategyType value : values()) {
                if (value.StrategyTypeCode == label) {
                    return value;
                }
            }
            return null;
        }
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
        ANY_CAR('n');

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
