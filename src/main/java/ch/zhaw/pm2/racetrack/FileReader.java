package ch.zhaw.pm2.racetrack;

import java.io.File;
import java.io.IOException;
import java.io.LineNumberReader;
import java.util.Scanner;

/**
 * Reads files and converts the data into a String array for use as track data.
 *
 * @author corrooli
 * @version 200226
 */

public class FileReader {

    /**
     * Main method for file reading. Does the following things:
     * 1. Checks + sets track width
     * 2. Checks + sets track height
     * 3. Creates String array and fills it with data gathered from file
     * 4. Returns the array to Track class.
     *
     * @param filepath Path to requested file (provided by IO)
     * @return trackArray String array for use with the game
     * @throws IOException if the file couldn't be found
     * NOT YET DECIDED: @throws InvalidTrackFormatException if the requirements haven't been met //TODO decide where exactly the exception is thrown
     */

    public String[][] readFile(String filepath) throws IOException {
        Scanner scanner = new Scanner(new File(filepath));
        LineNumberReader lineNumberReader = new LineNumberReader(new java.io.FileReader(filepath));

        int trackWidth = 0;
        int trackHeight = 10; // TODO remove test value!

        // Checking width of track
        String[] firstLine = scanner.nextLine().trim().split("\\s+");
        for (int index = 0; index < firstLine.length; index++) {
            trackWidth++;
        } // TODO: check if all lines are the same width

        // find out how many lines are in the file and fill in trackHeight variable
        // TODO: replace LineNumberReader with something simpler + more elegant
        while (lineNumberReader.readLine() != null) {
            trackHeight++;
        }

        // Initializing track array with width + height data gathered before
        String[][] trackArray = new String[trackHeight][trackWidth];

        // Filling the array
        while (scanner.hasNext()) {
            for (int indexY = 0; indexY < trackHeight; indexY++) {
                for (int indexX = 0; indexX < trackWidth; indexX++) {
                    trackArray[indexX][indexY] = (scanner.next());
                }
            }
        }
        scanner.close();
        return trackArray;
    }
}
