package ch.zhaw.pm2.racetrack.strategy;

import ch.zhaw.pm2.racetrack.PositionVector;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * this class handels the nextMove method if the user has chosen the MoveList strategy
 */
public class MoveListStrategy implements MoveStrategy {
    int currentLine;
    Scanner scanner;
    List<PositionVector> lines;

    /**
     * Constructor of the class
     *
     * @param file the file chosen from the user for the moves
     * @throws IOException if the scanner can't be initalised
     */
    public MoveListStrategy(File file) throws IOException {
        fileToPositionVectors(file);
    }

    private void fileToPositionVectors(File file) throws IOException {
        currentLine = -1; //offset
        lines = new ArrayList<>();

        scanner = new Scanner(file, StandardCharsets.UTF_8);
        while (scanner.hasNextLine()) {
            String[] line;
            line = scanner.nextLine().trim().split(" ");
            PositionVector vector = new PositionVector(Integer.parseInt(line[0]), Integer.parseInt(line[1]));
            lines.add(vector);
        }
    }

    /**
     * gets the next move of the user from the given file
     *
     * @return the direction from the current line in the file
     */
    @Override
    public PositionVector.Direction nextMove() {
        ++currentLine;
        if (currentLine < lines.size()) {
            for (PositionVector.Direction direction : PositionVector.Direction.values()) {
                if (direction.vector.equals(lines.get(currentLine))) {
                    return direction;
                }
            }
        }
        return PositionVector.Direction.NONE;
    }
}
