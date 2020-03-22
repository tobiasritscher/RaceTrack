package ch.zhaw.pm2.racetrack;

import ch.zhaw.pm2.racetrack.exceptions.ErrorType;
import ch.zhaw.pm2.racetrack.exceptions.GameException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static ch.zhaw.pm2.racetrack.PositionVector.Direction;

/**
 * Game controller class, performing all actions to modify the game state.
 * It contains the logic to move the cars, detect if they are crashed
 * and if we have a winner.
 */
public class Game {

    public static final int NO_WINNER = -1;
    public static final int FIRST_TURN_CAR_INDEX = 0;
    public static final int INITIAL_NUMBER_OF_PENALTY_POINTS = -Config.NUMBER_OF_LAPS * 2 + 1;
    private Map<Integer, Integer> penaltyPoints = new HashMap<>();
    private int winnerIndex = NO_WINNER;
    private TrackInterface raceTrack;
    private int activeCarIndex = FIRST_TURN_CAR_INDEX;

    /**
     * Constructor of the class Game.
     * Initialises track.
     *
     * @param track race track
     */
    public Game(TrackInterface track) {
        this.raceTrack = track;
    }

    /**
     * Return the index of the current active car.
     * Car indexes are zero-based, so the first car is 0, and the last car is getCarCount() - 1.
     *
     * @return The zero-based number of the current car
     */
    public int getCurrentCarIndex() {
        return activeCarIndex;
    }

    /**
     * Get the id of the specified car.
     *
     * @param carIndex The zero-based carIndex number
     * @return A char containing the id of the car
     */
    public char getCarId(int carIndex) {
        return raceTrack.getCarId(carIndex);
    }

    /**
     * Get the position of the specified car.
     *
     * @param carIndex The zero-based carIndex number
     * @return A PositionVector containing the car's current position
     */
    public PositionVector getCarPosition(int carIndex) {
        return raceTrack.getCarPosition(carIndex);
    }

    /**
     * Get the velocity of the specified car.
     *
     * @param carIndex The zero-based carIndex number
     * @return A PositionVector containing the car's current velocity
     */
    public PositionVector getCarVelocity(int carIndex) {
        return raceTrack.getCarVelocity(carIndex);
    }

    /**
     * Return the winner of the game. If the game is still in progress, returns NO_WINNER.
     *
     * @return The winning car's index (zero-based, see getCurrentCar()), or NO_WINNER if the game is still in progress
     */
    public int getWinner() {
        return winnerIndex;
    }

    /**
     * Execute the next turn for the current active car.
     * <p>This method changes the current car's velocity and checks on the path to the next position,
     * if it crashes (car state to crashed) or passes the finish line in the right direction (set winner state).</p>
     * <p>The steps are as follows</p>
     * <ol>
     *   <li>Accelerate the current car</li>
     *   <li>Calculate the path from current (start) to next (end) position
     *       (see {@link Game#calculatePath(PositionVector, PositionVector)})</li>
     *   <li>Verify for each step what space type it hits:
     *      <ul>
     *          <li>TRACK: check for collision with other car (crashed &amp; don't continue), otherwise do nothing</li>
     *          <li>WALL: car did collide with the wall - crashed &amp; don't continue</li>
     *          <li>FINISH_*: car hits the finish line - wins only if it crosses the line in the correct direction</li>
     *      </ul>
     *   </li>
     *   <li>If the car crashed or wins, set its position to the crash/win coordinates</li>
     *   <li>If the car crashed, also detect if there is only one car remaining, remaining car is the winner</li>
     *   <li>Otherwise move the car to the end position</li>
     * </ol>
     * <p>The calling method must check the winner state and decide how to go on. If the winner is different
     * than {@link Game#NO_WINNER}, or the current car is already marked as crashed the method returns immediately.</p>
     *
     * @param acceleration A Direction containing the current cars acceleration vector (-1,0,1) in x and y direction
     *                     for this turn
     * @throws IllegalArgumentException No such direction possible.
     */
    public void doCarTurn(Direction acceleration) {
        //todo test
        if (!Arrays.asList(Direction.values()).contains(acceleration)) {
            throw new GameException(ErrorType.INVALID_ACCELERATION);
        }

        if (!raceTrack.isCarCrashed(activeCarIndex)) {

            raceTrack.accelerateCar(activeCarIndex, acceleration);
            List<PositionVector> path = calculatePath(getCarPosition(activeCarIndex), raceTrack.getCarNextPosition(activeCarIndex));

            Iterator<PositionVector> iterator = path.iterator();
            PositionVector currentPathPosition = iterator.next();

            boolean hasCrashedOrFinished = false;
            while (iterator.hasNext() && !hasCrashedOrFinished) {
                PositionVector nextPathPosition = iterator.next();
                if (willCarCrash(activeCarIndex, nextPathPosition)) {
                    hasCrashedOrFinished = true;
                    raceTrack.crashCar(activeCarIndex, nextPathPosition);
                    if (isOneCarRemaining()) {
                        switchToNextActiveCar();
                        raceTrack.crashCar(activeCarIndex, raceTrack.getCarPosition(activeCarIndex));
                        winnerIndex = activeCarIndex;
                    }
                } else if ((raceTrack.isOnFinishLine(currentPathPosition) && !raceTrack.isOnFinishLine(nextPathPosition))
                        || (!raceTrack.isOnFinishLine(currentPathPosition) && raceTrack.isOnFinishLine(nextPathPosition))) {
                    if (raceTrack.isOnFinishLine(currentPathPosition)) {
                        adjustPenaltyPointsForActiveCar(currentPathPosition);
                    } else {
                        adjustPenaltyPointsForActiveCar(nextPathPosition);
                    }
                    if (penaltyPoints.get(activeCarIndex) == 0) {
                        hasCrashedOrFinished = true;
                        //move to the FL
                        raceTrack.crashCar(activeCarIndex, nextPathPosition);
                        winnerIndex = activeCarIndex;
                    }
                }
                currentPathPosition = nextPathPosition;
            }
            if (!hasCrashedOrFinished) {
                raceTrack.moveCar(activeCarIndex);
            }
        }
        switchToNextActiveCar();

    }

    /**
     * Adjust the penalty points of active car.
     * <p>
     * The function first check if there is an penalty entry in the map. If not new entry with INITIAL_NUMBER_OF_PENALTY_POINTS penalty points will be created.
     * A penalty point will be added or subtracted if and only if, car crossed the finish line meaning
     */
    private void adjustPenaltyPointsForActiveCar(PositionVector finishPosition) {
        //todo test
        if (!penaltyPoints.containsKey(activeCarIndex)) {
            penaltyPoints.put(activeCarIndex, INITIAL_NUMBER_OF_PENALTY_POINTS);
        }
        if (isValidDirection(getCarVelocity(activeCarIndex), getFinishDirectionUnitVector(finishPosition))) {
            penaltyPoints.put(activeCarIndex, penaltyPoints.get(activeCarIndex) + 1);
        } else {
            penaltyPoints.put(activeCarIndex, penaltyPoints.get(activeCarIndex) - 1);
        }

    }

    /**
     * Evaluate if the active car is crossing the finish line(FL) in valid direction.
     * <p>
     * Calculation:
     * <ol>
     *  <li>Movement direction vector is calculated: vector which results from difference between next position and current position.</li>
     *  <li>FL???</li>
     *  <li>Scalar product between finish line direction unit vector and the calculated difference vector is taken.</li>
     *  <li>Scalar product gives the sign of the penalty point</li>
     * </ol>
     * <p>
     * Note: The caller function has to make sure the given position is actually the finish line.
     *
     * @return True, if crossed FL in the valid direction. False, if crossed FL in a false direction, or the given position is not a finish line.
     */
    boolean isValidDirection(PositionVector carVelocity, PositionVector finishDirection) {
        //todo test
        boolean isValidDirection = false;
        int angle = (int) PositionVector.calculateAngle(carVelocity, finishDirection);
        if (angle < Math.PI / 2 && angle >= 0) {
            isValidDirection = true;
        }
        return isValidDirection;
    }

    /**
     * Returns a direction unit vector which is in the valid finish direction and is orthogonal to finish line.
     * The coordinate system is assumed to be directed as following: first coordinate to the right and second coordinate downwards.
     * <p>
     * Note:
     * <ol>
     *     <li>Any exception thrown by function call getSpaceType() will be ignored, a (0,0) PositionVector will be returned.</li>
     *     <li>Is package private only for test purposes.</li>
     * </ol>
     *
     * @param positionOnFinishLine A point on finish line.
     * @return (x, y)=(0,0) if the point is not on finish line, otherwise returns finish direction unit vector.
     */
    PositionVector getFinishDirectionUnitVector(PositionVector positionOnFinishLine) {
        PositionVector finishDirectionUnitVector;
        try {
            switch (raceTrack.getSpaceType(positionOnFinishLine)) {
                case FINISH_UP:
                    finishDirectionUnitVector = Direction.UP.vector;
                    break;
                case FINISH_RIGHT:
                    finishDirectionUnitVector = Direction.RIGHT.vector;
                    break;
                case FINISH_DOWN:
                    finishDirectionUnitVector = Direction.DOWN.vector;
                    break;
                case FINISH_LEFT:
                    finishDirectionUnitVector = Direction.LEFT.vector;
                    break;
                default:
                    finishDirectionUnitVector = new PositionVector(0, 0);
            }
        } catch (Exception e) {
            finishDirectionUnitVector = new PositionVector(0, 0);
        }
        return finishDirectionUnitVector;
    }

    /**
     * Tell if there is only one active car left.
     *
     * @return True, if only one car left.
     */
    private boolean isOneCarRemaining() {
        final int ONLY_ONE_CAR = 1;
        return raceTrack.getNumberActiveCarsRemaining() == ONLY_ONE_CAR;
    }

    /**
     * Switches to the next car who is still in the game. Skips
     * crashed cars.
     * <p>
     * Not: If there are no active cars left activeCarIndex = NO_WINNER will be set.
     */
    public void switchToNextActiveCar() {
        //todo use exception
        if (activeCarIndex != NO_WINNER) {
            int nextCarIndex = (activeCarIndex + 1) % raceTrack.getCarCount();
            while (raceTrack.isCarCrashed(nextCarIndex) && activeCarIndex != nextCarIndex) {
                nextCarIndex = (nextCarIndex + 1) % raceTrack.getCarCount();
            }
            //at this point actviteCarIndex==nextCarIndex or nextCarIndex car is not crashed
            if (activeCarIndex == nextCarIndex) {
                //we made a loop and found no cars
                activeCarIndex = NO_WINNER;
            } else {
                activeCarIndex = nextCarIndex;
            }
        }
    }


    /**
     * Returns all of the grid positions in the path between two positions, for use in determining line of sight.
     * Determine the 'pixels/positions' on a raster/grid using Bresenham's line algorithm.
     * (https://de.wikipedia.org/wiki/Bresenham-Algorithmus)
     * Basic steps are
     * - Detect which axis of the distance vector is longer (faster movement)
     * - for each pixel on the 'faster' axis calculate the position on the 'slower' axis.
     * Direction of the movement has to correctly considered
     *
     * @param startPosition Starting position as a PositionVector
     * @param endPosition   Ending position as a PositionVector
     * @return Intervening grid positions as a List of PositionVector's, including the starting and ending positions.
     */
    public List<PositionVector> calculatePath(PositionVector startPosition, PositionVector endPosition) {
        // todo test
        List<PositionVector> path = new ArrayList<>();
        int diffX = endPosition.getX() - startPosition.getX();
        int diffY = endPosition.getY() - startPosition.getY();

        //choose sampling direction
        int distX = Math.abs(diffX);
        int distY = Math.abs(diffY);

        int dirX = Integer.signum(diffX);
        int dirY = Integer.signum(diffY);

        int distanceSlowAxis, distanceFastAxis;
        int parallelStepX, parallelStepY;
        int diagonalStepX, diagonalStepY;

        if (distX > distY) {
            // x axis is the 'fast' direction
            //1,4,5,8 octant
            parallelStepX = dirX;
            parallelStepY = 0;
            diagonalStepX = dirX;
            diagonalStepY = dirY;
            distanceFastAxis = distX;
        } else {
            // y axis is the 'fast' direction
            parallelStepX = 0;
            parallelStepY = dirY;
            diagonalStepX = dirX;
            diagonalStepY = dirY;
            distanceFastAxis = distY;
        }
        distanceSlowAxis = distY;
        int x = startPosition.getX();
        int y = startPosition.getY();
        path.add(new PositionVector(x, y));

        int error = distanceFastAxis / 2;
        for (int step = 0; step < distanceFastAxis; step++) {

            error -= distanceSlowAxis;
            if (error < 0) {
                error += distanceFastAxis;
                x += diagonalStepX;
                y += diagonalStepY;
            } else {
                x += parallelStepX;
                y += parallelStepY;
            }
            path.add(new PositionVector(x, y));
        }
        return path;
    }

    /**
     * Does indicate if a car would have a crash with a WALL space or another car at the given position.
     *
     * @param carIndex The zero-based carIndex number
     * @param position A PositionVector of the possible crash position
     * @return A boolean indicator if the car would crash with a WALL or another car.
     */
    public boolean willCarCrash(int carIndex, PositionVector position) {
        return raceTrack.isTrackBound(position) || raceTrack.isSomeOtherCarHere(carIndex, position);
    }

}
