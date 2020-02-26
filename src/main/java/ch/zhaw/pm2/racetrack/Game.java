package ch.zhaw.pm2.racetrack;

import java.util.*;

import static ch.zhaw.pm2.racetrack.PositionVector.*;

/**
 * Game controller class, performing all actions to modify the game state.
 * It contains the logic to move the cars, detect if they are crashed
 * and if we have a winner.
 */
public class Game {
    public static final int NO_WINNER = -1;

    private int activeCarIndex=0;
    private Track raceTrack;
    private static final int MIN_CARS = 2;
    private int winnerIndex = NO_WINNER;
    /**
     * Constructor of the class Game.
     * Initialises track.
     *
     * @param track race track
     */
    public Game(Track track) {
        try{
            raceTrack = track;
        }catch(NullPointerException e){
            System.err.println("Track-Object is null!");
            System.exit(-1);
        }
        if(track.getCarCount() < MIN_CARS || track.getCarCount() > Config.MAX_CARS){
            throw new IllegalArgumentException("Number of car should >"+ (MIN_CARS-1) + " and <" + (Config.MAX_CARS+1));
        }
    }
    /**
     * Return the index of the current active car.
     * Car indexes are zero-based, so the first car is 0, and the last car is getCarCount() - 1.
     * @return The zero-based number of the current car
     */
    public int getCurrentCarIndex() {
        return activeCarIndex;
    }

    /**
     * Get the id of the specified car.
     * @param carIndex The zero-based carIndex number
     * @return A char containing the id of the car
     * @throws IllegalArgumentException
     */
    public char getCarId(int carIndex) {
        if(isValidCarIndex(carIndex)){
            throw new IllegalArgumentException("Is not a legal car index.");
        }
        return raceTrack.getCarId(carIndex);
    }

    /**
     * Check if the given car index is valid.
     * @param carIndex The car index
     * @return true if a valid index given
     */
    private boolean isValidCarIndex(int carIndex){
        return carIndex >= MIN_CARS || carIndex <= Config.MAX_CARS;
    }

    /**
     * Get the position of the specified car.
     * @param carIndex The zero-based carIndex number
     * @return A PositionVector containing the car's current position
     * @throws IllegalArgumentException
     */
    public PositionVector getCarPosition(int carIndex) {
        if(isValidCarIndex(carIndex)){
            throw new IllegalArgumentException("Is not a legal car index.");
        }
        return raceTrack.getCarPos();
    }

    /**
     * Get the velocity of the specified car.
     * @param carIndex The zero-based carIndex number
     * @return A PositionVector containing the car's current velocity
     * @throws IllegalArgumentException
     */
    public PositionVector getCarVelocity(int carIndex) {
        if(isValidCarIndex(carIndex)){
            throw new IllegalArgumentException("Is not a legal car index.");
        }
        return raceTrack.getCarVelocity();
    }

    /**
     * Return the winner of the game. If the game is still in progress, returns NO_WINNER.
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

    }

    /**
     * Switches to the next car who is still in the game. Skips crashed cars.
     */
    public void switchToNextActiveCar() {

    }


    /**
     * Returns all of the grid positions in the path between two positions, for use in determining line of sight.
     * Determine the 'pixels/positions' on a raster/grid using Bresenham's line algorithm.
     * (https://de.wikipedia.org/wiki/Bresenham-Algorithmus)
     * Basic steps are
     * - Detect which axis of the distance vector is longer (faster movement)
     * - for each pixel on the 'faster' axis calculate the position on the 'slower' axis.
     * Direction of the movement has to correctly considered
     * @param startPosition Starting position as a PositionVector
     * @param endPosition Ending position as a PositionVector
     * @return Intervening grid positions as a List of PositionVector's, including the starting and ending positions.
     */
    public List<PositionVector> calculatePath(PositionVector startPosition, PositionVector endPosition) {
        // todo

        return new ArrayList<PositionVector>();
    }

    /**
     * Does indicate if a car would have a crash with a WALL space or another car at the given position.
     * @param carIndex The zero-based carIndex number
     * @param position A PositionVector of the possible crash position
     * @return A boolean indicator if the car would crash with a WALL or another car.
     */
    public boolean willCarCrash(int carIndex, PositionVector position) {
        // todo
        return false;
    }


}
