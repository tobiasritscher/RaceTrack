package ch.zhaw.pm2.racetrack.exceptions;

import ch.zhaw.pm2.racetrack.PositionVector;

import java.util.Arrays;

public class GameException extends RuntimeException {
    ErrorType errorType;

    public GameException(ErrorType errorType) {
        this.errorType = errorType;
    }

    private String getErrorMessage() {
        String errorMessage = "";
        switch (errorType) {
            case INVALID_ACCELERATION:
                errorMessage += "Invalid Acceleration! Only " + Arrays.toString(PositionVector.Direction.values()) + " are allowed!";
                break;
            case NO_ACTIVE_CARS:
                errorMessage += "There are no active cars left.";
                break;
            case NOT_ON_FINISH_LINE:
                errorMessage += "Given position is not on finish line.";
                break;
            default:
        }
        return errorMessage;
    }

    @Override
    public String toString() {
        return getErrorMessage();
    }
}
