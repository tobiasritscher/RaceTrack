package ch.zhaw.pm2.racetrack;

import java.io.File;
import java.io.IOException;
import java.io.LineNumberReader;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Reads files and converts the data into a String array for use as track data.
 *
 * @author corrooli
 * @version 200226
 */

public class TrackBuilder {

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
     * NOT YET DECIDED: @throws InvalidTrackFormatException if the requirements haven't been met //TODO decide where exactly the exception is thrown
     */

    public String[][] readFile(File file) throws IOException, InvalidTrackFormatException {
        Scanner scanner = new Scanner(file);
        LineNumberReader lineNumberReader = new LineNumberReader(new java.io.FileReader(file));

        int trackWidth = 0;
        int trackHeight = 0;

        // Checking + setting trackHeight value
        // TODO: replace LineNumberReader with something simpler + more elegant
        while (lineNumberReader.readLine() != null) {
            trackHeight++;
        }

        // Checking + setting trackWidth value
        ArrayList<String> trackCheckArray = new ArrayList<>();
        while (scanner.hasNext()) {
            trackCheckArray.add(scanner.next());
        }
        // Checking if no lines are present
        if (trackCheckArray.size()==0){
            throw new InvalidTrackFormatException(file, ErrorType.NO_TRACK_LINES);
        }
        for (int i = 1; i < trackCheckArray.size()+1; ++i){
            if (trackCheckArray.get(i-1).length() != trackCheckArray.get(i).length()) {
                throw new InvalidTrackFormatException(file, ErrorType.NOT_SAME_LENGTH);
            }
        }

        String[] firstLine = scanner.nextLine().trim().split("\\s+");
        for (int index = 0; index < firstLine.length; index++) {
            trackWidth++;
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
