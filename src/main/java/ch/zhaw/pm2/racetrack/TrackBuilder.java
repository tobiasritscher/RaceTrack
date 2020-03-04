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
     * 3. Creates String array and fills it with data gathered from file
     * 4. Returns the array to Track class.
     *
     * @param file (provided by IO)
     * @return trackArray String array for use with the game
     * @throws IOException if the file couldn't be found
     * @throws InvalidTrackFormatException if file requirements haven't been met //TODO decide where exactly the exception is thrown
     */

    public Config.SpaceType[][] buildTrack(File file) throws IOException, InvalidTrackFormatException {
        Scanner scanner = new Scanner(file);

        // Creating test track ArrayList for width & height tests
        ArrayList<String> trackCheckArray = new ArrayList<>();
        while (scanner.hasNext()) {
            trackCheckArray.add(scanner.next());
        }

        // Checking if no lines are present
        if (trackCheckArray.isEmpty()) {
            throw new InvalidTrackFormatException(file, ErrorType.NO_TRACK_LINES);
        }

        // Checking if lines are same length
        for (int index = 1; index < trackCheckArray.size() + 1; ++index) {
            if (trackCheckArray.get(index - 1).length() != trackCheckArray.get(index).length()) {
                throw new InvalidTrackFormatException(file, ErrorType.NOT_SAME_LENGTH);
            }
        }

        // Setting track width
        String[] firstLine = scanner.nextLine().trim().split("\\s+");
        for (int index = 0; index < firstLine.length; index++) {
            trackWidth++;
        }

        // Setting track height
        trackHeight = trackCheckArray.size();

        // Initializing track array with width + height data gathered before
        Config.SpaceType[][] trackArray = new Config.SpaceType[trackHeight][trackWidth];

        // Filling the array with track data, checking if car is already taken & creating them

        while (scanner.hasNext()) {
            for (int indexY = 0; indexY < trackHeight; indexY++) {
                String[] fillArray = scanner.next().split("(?!^)");
                for (int indexX = 0; indexX < fillArray.length; indexX++) {
                    switch (fillArray[indexX]) {
                        case "#":
                            trackArray[indexX][indexY] = Config.SpaceType.WALL;
                            indexX++;
                            break;
                        case " ":
                            trackArray[indexX][indexY] = Config.SpaceType.TRACK;
                            indexX++;
                            break;
                        case "^":
                            trackArray[indexX][indexY] = Config.SpaceType.FINISH_UP;
                            indexX++;
                            break;
                        case "v":
                            trackArray[indexX][indexY] = Config.SpaceType.FINISH_DOWN;
                            indexX++;
                            break;
                        case "<":
                            trackArray[indexX][indexY] = Config.SpaceType.FINISH_LEFT;
                            indexX++;
                            break;
                        case ">":
                            trackArray[indexX][indexY] = Config.SpaceType.FINISH_RIGHT;
                            indexX++;
                            break;
                        default:
                            if (numberOfCars > Config.MAX_CARS){
                                throw new InvalidTrackFormatException(file, ErrorType.TOO_MANY_CARS);
                            } else {
                                numberOfCars++;
                                carMap.put(fillArray[indexX].charAt(0), new PositionVector(indexX, indexY));
                            }
                            trackArray[indexX][indexY] = Config.SpaceType.ANY_CAR;
                            indexX++;
                    }
                }
            }
            scanner.close();
        }
        return trackArray;
    }
    public int getTrackWidth(){
        return trackWidth;
    }

    public int getTrackHeight(){
        return trackHeight;
    }

    public int getNumberOfCars(){
        return numberOfCars;
    }

    public Map<Character, PositionVector> getCarMap() {
        return carMap;
    }

}

