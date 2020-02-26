package ch.zhaw.pm2.racetrack;

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
    //TODO: define error types via enum which are returned by this Exception class

    public InvalidTrackFormatException(File trackFile){ this.trackFile = trackFile; }

    public File getTrackFile() { return trackFile; }

    public String toString(){
        return("The following track file doesn't match the requirements:\n" + trackFile.toString() +
                "Error Type <to be implemented>");
    }
}


