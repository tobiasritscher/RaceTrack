package ch.zhaw.pm2.racetrack;

import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * Reads files and converts the data into a Config.SpaceType array for use as track data.
 *
 * @author corrooli
 * @version 200226
 */

public class TrackBuilder {
    private int trackWidth = 0;
    private int trackHeight = 0;
    private int numberOfCars = 0;
    private Map<Character, PositionVector> carMap = new HashMap<>();

    /**
     * Main method for track building. Does the following things:
     * 1. Checks + sets track width
     * 2. Checks + sets track height
     * 3. Creates Config.SpaceType array and fills it with data gathered from file
     * 4. Prepares + checks list of cars, including their position and character
     * 5. Returns the array to Track class.
     *
     * @param file (provided by IO)
     * @return trackArray Config.SpaceType array for use with the game
     * @throws IOException                 if the file couldn't be found
     * @throws InvalidTrackFormatException if file requirements haven't been met //TODO decide where exactly the exception is thrown
     */

    public Config.SpaceType[][] buildTrack(File file) throws IOException, InvalidTrackFormatException {
        Scanner scanner = new Scanner(file);

        // Creating test track ArrayList for width & height tests
        ArrayList<String> trackCheckArray = new ArrayList<>();
        while (scanner.hasNextLine()) {
            trackCheckArray.add(scanner.nextLine());
        }

        // Checking if no lines are present
        if (trackCheckArray.isEmpty()) {
            throw new InvalidTrackFormatException(file, ErrorType.NO_TRACK_LINES);
        }

        // Checking if lines are same length
        for (int index = 1; index <= trackCheckArray.size() - 1; ++index) {
            if (trackCheckArray.get(index - 1).length() != trackCheckArray.get(index).length()) {
                throw new InvalidTrackFormatException(file, ErrorType.NOT_SAME_LENGTH);
            }
        }

        // Re-initializing scanner
        scanner = new Scanner(file);

        // Setting track width
        trackWidth = trackCheckArray.get(0).length();

        // Setting track height
        trackHeight = trackCheckArray.size();

        // Initializing track array with width + height data gathered before
        Config.SpaceType[][] trackArray = new Config.SpaceType[trackHeight][trackWidth];

        // Filling the array with track data
        while (scanner.hasNext()) {
            for (int indexY = 0; indexY < trackHeight; indexY++) {
                // TODO: break loop if empty line is detected
                String[] fillArray = scanner.nextLine().split("(?!^)");
                for (int indexX = 0; indexX < trackWidth; indexX++) {
                    switch (fillArray[indexX]) {
                        case "#":
                            trackArray[indexY][indexX] = Config.SpaceType.WALL;
                            break;
                        case " ":
                            trackArray[indexY][indexX] = Config.SpaceType.TRACK;
                            break;
                        case "^":
                            trackArray[indexY][indexX]  = Config.SpaceType.FINISH_UP;
                            break;
                        case "v":
                            trackArray[indexY][indexX] = Config.SpaceType.FINISH_DOWN;
                            break;
                        case "<":
                            trackArray[indexY][indexX] = Config.SpaceType.FINISH_LEFT;
                            break;
                        case ">":
                            trackArray[indexY][indexX] = Config.SpaceType.FINISH_RIGHT;
                            break;
                        default:
                            // checking if number of cars exceed the allowed amount
                            if (numberOfCars > Config.MAX_CARS) {
                                throw new InvalidTrackFormatException(file, ErrorType.TOO_MANY_CARS);
                            } else {
                                // check if car character is already taken
                                if (carMap.containsKey(fillArray[indexX].charAt(0))) {
                                    throw new InvalidTrackFormatException(file, ErrorType.TOO_MANY_CARS);
                                } else {
                                    numberOfCars++;
                                    carMap.put(fillArray[indexX].charAt(0), new PositionVector(indexX, indexY));
                                }
                            }
                            trackArray[indexX][indexY] = Config.SpaceType.ANY_CAR;
                    }
                }
            }
        }
        return trackArray;
    }

    public int getTrackWidth() {
        return trackWidth;
    }

    public int getTrackHeight() {
        return trackHeight;
    }

    public int getNumberOfCars() {
        return numberOfCars;
    }

    public Map<Character, PositionVector> getCarMap() {
        return carMap;
    }

}

