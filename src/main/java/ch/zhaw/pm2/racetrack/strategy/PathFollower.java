package ch.zhaw.pm2.racetrack.strategy;

import ch.zhaw.pm2.racetrack.PositionVector;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;
import java.util.Stack;

public class PathFollower implements MoveStrategy {

    private Queue<PositionVector> coordinatesToFollow = new LinkedList<>();
    private Stack<PositionVector.Direction> moves = new Stack<>();
    private int moveCounter = 0;
    private Scanner scanner;

    public PathFollower(File pathFollowerFile) throws IOException {
        this.scanner = new Scanner(pathFollowerFile, StandardCharsets.UTF_8);
    }

    @Override
    public PositionVector.Direction nextMove() {
        if (moveCounter == Integer.MAX_VALUE) {
            throw new RuntimeException("Overflow occurred.The number of turns got its limit.");
        }
        moveCounter++;
        if (moves.isEmpty()) {

        }
        return null;
    }

    void readCoordinatesFromFile() {
        int numberEntries = Integer.parseInt(scanner.nextLine());
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine().strip();
            if (!line.matches("[+,-]?\\d+\\s[+,-]?\\d+")) {
                throw new RuntimeException("Invalid format.!Wrong line format");
            }

            String[] arguments = line.strip().split(" ");
            coordinatesToFollow.add(new PositionVector(Integer.parseInt(arguments[0]), Integer.parseInt(arguments[1])));
        }
        if (coordinatesToFollow.size() != numberEntries) {
            throw new RuntimeException("Invalid format.! Numbers of entries missmatch.");
        }
    }

    private void calculateMoves() {
        final int CAR_START_VELOCITY = 0;

    }
}
