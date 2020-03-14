package ch.zhaw.pm2.racetrack;

import java.util.*;

import static ch.zhaw.pm2.racetrack.PositionVector.Direction;

/**
 * Game controller class, performing all actions to modify the game state.
 * It contains the logic to move the cars, detect if they are crashed
 * and if we have a winner.
 */
public class Game {

    public static final int NO_WINNER = -1;
    private int winnerIndex = NO_WINNER;

    public static final int NUMBER_OF_LAPS = 1;
    public static final int INITIAL_NUMBER_OF_PENALTY_POINTS = -NUMBER_OF_LAPS;

    private Track raceTrack;

    private int activeCarIndex = 0;

    Map<Character, Integer> penaltyPoints = new HashMap<>();

    /**
     * Constructor of the class Game.
     * Initialises track.
     *
     * @param track race track
     */
    public Game(Track track) {
        raceTrack = track;
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
        return raceTrack.getCarPos(activeCarIndex);
    }

    /**
     * Get the velocity of the specified car.
     *
     * @param carIndex The zero-based carIndex number
     * @return A PositionVector containing the car's current velocity
     */
    public PositionVector getCarVelocity(int carIndex) {
        return raceTrack.getCarVelocity(activeCarIndex);
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
     */
    public void doCarTurn(Direction acceleration) {
        if (!raceTrack.isCarCrashed(activeCarIndex) && !(winnerIndex == NO_WINNER)) {
            //TODO any parameter checks?

            //Accelerate the current car
            raceTrack.accelerateCar(activeCarIndex, acceleration);

            //calculate path between actual and end positions
            List<PositionVector> path = calculatePath(raceTrack.getCarPos(activeCarIndex), raceTrack.getCarNextPosition(activeCarIndex));

        //crashes or passes??
        for (PositionVector transitionPoint : path) {
            if (willCarCrash(activeCarIndex, transitionPoint)) {
                //crashed place car at crash point
                //todo now there are two cars at same point >> the transitionpoint before crash will set as crash point
                //move car to the crash coordinates
                activeCar.move();
                activeCar.crash();
                //there is a car remaining
                if (isLastCarRemaining()) {
                    switchToNextActiveCar();

                    winnerIndex = getCurrentCarIndex();
                }
            } else if (willCarCrosseTheFinishLine(transitionPoint)) {
                winnerIndex = activeCarIndex;
            } else {
                activeCar.move();
            }
        }
        //move to the crash coordinates
        if(activeCar.isCrashed()){
            activeCar.setCarPosition();
        }
        //set correct velocity
        activeCar.accelerate(acceleration);

    }

    private boolean willCarCrosseTheFinishLine(PositionVector position) {
        boolean result = false;
        //crossed?
        getCarVelocity(activeCarIndex);
        PositionVector finishVector;
        switch (raceTrack.getSpaceType(position)) {
            case FINISH_UP:
                finishVector = new PositionVector(1, 1);
                break;
            case FINISH_RIGHT:
                finishVector = new PositionVector(1, 0);
                break;
            case FINISH_DOWN:
                finishVector = new PositionVector(-1, -1);
                break;
            case FINISH_LEFT:
                finishVector = new PositionVector(-1, 0);
                break;
            default:
                finishVector = new PositionVector(0, 0);
        }
        //TODO deal with case: after start, went in the reverse direction, turn around, went in the correct direction.[Lap,Direction]
        //TODO crash on the finish line?
        if (!finishVector.equals(new PositionVector(0, 0)) && PositionVector.scalarProduct(getCarVelocity(activeCarIndex), finishVector) > 0) {
            result = true;
        }
        //correct direction
        return result;
    }

    private boolean isLastCarRemaining() {
        //todo
        return false;
    }

    /**
     * Switches to the next car who is still in the game. Skips
     * crashed cars.
     */
    public void switchToNextActiveCar() {
        //TODO what if all autos crashed
        //TODO test switch next, switch cycle
        //TODO what if all cars are crashed? >> set NO_WINNER?
        int nextCarIndex = (activeCarIndex + 1) % raceTrack.getCarCount();
        if (raceTrack.getCar(nextCarIndex).isCrashed()) {
            switchToNextActiveCar();
        } else {
            if (activeCarIndex != nextCarIndex) {
                activeCarIndex = nextCarIndex;
            } else {
                //STOPGAME
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
        // todo
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
            distanceSlowAxis = distY;
        } else {
            // y axis is the 'fast' direction
            parallelStepX = 0;
            parallelStepY = dirY;
            diagonalStepX = dirX;
            diagonalStepY = dirY;
            distanceFastAxis = distY;
            distanceSlowAxis = distY;
        }
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
        return raceTrack.isTrackBound(position) || raceTrack.someCarIsHere(position);
    }

}
