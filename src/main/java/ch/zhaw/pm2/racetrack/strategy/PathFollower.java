package ch.zhaw.pm2.racetrack.strategy;

import ch.zhaw.pm2.racetrack.PositionVector;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;

public class PathFollower implements MoveStrategy {

    private Queue<PositionVector> coordinatesToFollow = new LinkedList<>();
    private Stack<PositionVector.Direction> moves = new Stack<>();
    private int moveCounter = 0;

    @Override
    public PositionVector.Direction nextMove() {
        if (moveCounter == Integer.MAX_VALUE) {
            throw new RuntimeException("Overflow occurred.The number of turns got its limit.");
        }
        moveCounter++;
        if(moves.isEmpty()){

        }
        return null;
    }

    public void readCoordinates() {

    }

    private void calculateMoves() {
        PositionVector
        final int CAR_START_VELOCITY = 0;
    }
}
