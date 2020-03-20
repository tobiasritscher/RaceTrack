package ch.zhaw.pm2.racetrack.exceptions;

import ch.zhaw.pm2.racetrack.Config;

import java.io.File;

/**
 * Checked exception which is thrown then the track format doesn't match the requirements
 * of the game.
 *
 * Error Types:
 *   1: not all track lines have the same length</li>
 *   2: the file contains no track lines (grid height is 0)</li>
 *   3: the file contains more than {@link Config#MAX_CARS} cars</li>
 *
 * @author corrooli
 * @version 200226
 */

public class InvalidTrackFormatException extends Exception {
    private File trackFile;
    private ErrorType errorType;

    public InvalidTrackFormatException(File trackFile, ErrorType errorType){
        this.trackFile = trackFile;
        this.errorType = errorType;
    }

    public String printErrorType() {
        String errorMessage;
        switch(errorType){
            case NOT_SAME_LENGTH:
                errorMessage = "Width is not consistent over entire track (some lines are shorter/longer)";
                break;
            case NO_TRACK_LINES:
                errorMessage = "Track contains no track lines";
                break;
            case TOO_MANY_CARS:
                errorMessage = "Too many cars on track file. Maximum amount allowed is " +Config.MAX_CARS;
                break;
            case NOT_ENOUGH_CARS:
                errorMessage = "Not enough cars on track file. Minimum amount allowed is " +Config.MIN_CARS;
                break;
            case DUPLICATE_CARS:
                errorMessage = "There are duplicate cars";
                break;
            default:
                errorMessage = "Error not specified.";
                break;
        }
        return errorMessage;
    }

    public String toString(){
        return("The following track file doesn't match the requirements: " + trackFile.toString() +
                "\n" + printErrorType());
    }
}

