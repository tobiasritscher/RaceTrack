package ch.zhaw.pm2.racetrack.strategy;

import ch.zhaw.pm2.racetrack.Game;
import ch.zhaw.pm2.racetrack.PositionVector;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Scanner;
import java.util.Stack;

public class PathFollowerStrategy implements MoveStrategy {

    private Queue<PositionVector> coordinatesToFollow = new LinkedList<>();
    private Stack<PositionVector.Direction> moves = new Stack<>();
    private int moveCounter = 0;
    private Scanner scanner;

    /**
     * Assumption the first coordinates is the start coordinates of a car
     *
     * @return
     */
    public PathFollowerStrategy(File pathFollowerFile) throws IOException {
        this.scanner = new Scanner(pathFollowerFile, StandardCharsets.UTF_8);
        readCoordinatesFromFile();
    }

    @Override
    public PositionVector.Direction nextMove() {
        if (moveCounter == 0) {
            calculateMoves();
            moveCounter = moves.size();
        }
        moveCounter++;
        if (moves.isEmpty()) {

        }
        return null;
    }

    private void calculateMoves() {
        List<PositionVector> path = Game.calculatePath(coordinatesToFollow.poll(), coordinatesToFollow.peek());
        final int CAR_START_VELOCITY = 0;

    }

    void readCoordinatesFromFile() {
        int numberEntries = Integer.parseInt(scanner.nextLine());
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine().strip();
            if (!line.matches("[+,-]?\\d+\\s[+,-]?\\d+")) {
                throw new RuntimeException("Invalid file format! Wrong line format: " + line);
            }

            String[] arguments = line.strip().split(" ");
            coordinatesToFollow.add(new PositionVector(Integer.parseInt(arguments[0]), Integer.parseInt(arguments[1])));
        }
        if (coordinatesToFollow.size() != numberEntries) {
            throw new RuntimeException("Invalid file format! Number of entries mismatch. Should be: " + numberEntries);
        }
    }
}
