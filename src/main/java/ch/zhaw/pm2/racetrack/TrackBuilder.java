package ch.zhaw.pm2.racetrack;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

/**
 * Reads files and converts the data into a Config.SpaceType array for use as track data.
 *
 * @author corrooli
 * @version 200226
 */

public class TrackBuilder {
    int trackWidth = 0;
    int trackHeight = 0;
    int numberOfCars = 0;

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
        ArrayList<Boolean> carTaken = new ArrayList<>(Arrays.asList(false,false,false,false,false,false,false,false,false));

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
                        case "a|b|c|d|e|f|g|h|i":
                            // TODO create new cars
                            switch (fillArray[indexX]) {
                                case "a":
                                    if (carTaken.get(0)) {
                                        throw new InvalidTrackFormatException(file, ErrorType.TOO_MANY_CARS);
                                    } else {
                                        carTaken.set(0, true);
                                        // create new car 'a'
                                    }
                                    break;
                                case "b":
                                    if (carTaken.get(1)) {
                                        throw new InvalidTrackFormatException(file, ErrorType.TOO_MANY_CARS);
                                    } else {
                                        carTaken.set(1, true);
                                        // create new car 'b'
                                    }
                                    break;
                                case "c":
                                    if (carTaken.get(2)) {
                                        throw new InvalidTrackFormatException(file, ErrorType.TOO_MANY_CARS);
                                    } else {
                                        carTaken.set(2, true);
                                        // create new car 'c'
                                    }
                                    break;
                                case "d":
                                    if (carTaken.get(3)) {
                                        throw new InvalidTrackFormatException(file, ErrorType.TOO_MANY_CARS);
                                    } else {
                                        carTaken.set(3, true);
                                        // create new car 'd'
                                    }
                                    break;
                                case "e":
                                    if (carTaken.get(4)) {
                                        throw new InvalidTrackFormatException(file, ErrorType.TOO_MANY_CARS);
                                    } else {
                                        carTaken.set(4, true);
                                        // create new car 'e'
                                    }
                                    break;
                                case "f":
                                    if (carTaken.get(5)) {
                                        throw new InvalidTrackFormatException(file, ErrorType.TOO_MANY_CARS);
                                    } else {
                                        carTaken.set(5, true);
                                        // create new car 'b'
                                    }
                                    break;
                                case "g":
                                    if (carTaken.get(6)) {
                                        throw new InvalidTrackFormatException(file, ErrorType.TOO_MANY_CARS);
                                    } else {
                                        carTaken.set(6, true);
                                        // create new car 'b'
                                    }
                                    break;
                                case "h":
                                    if (carTaken.get(7)) {
                                        throw new InvalidTrackFormatException(file, ErrorType.TOO_MANY_CARS);
                                    } else {
                                        carTaken.set(7, true);
                                        // create new car 'b'
                                    }
                                    break;
                                case "i":
                                    if (carTaken.get(8)) {
                                        throw new InvalidTrackFormatException(file, ErrorType.TOO_MANY_CARS);
                                    } else {
                                        carTaken.set(8, true);
                                        // create new car 'b'
                                    }
                                    break;
                                case "j|k|l|m|n|o|p|q|r|s|t|u|v|w|x|y|z":
                                    throw new InvalidTrackFormatException(file, ErrorType.TOO_MANY_CARS);
                                default:
                                    System.err.println("Character not recognized!");
                                    indexX++;
                                    break;
                            }
                            break;
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
}

