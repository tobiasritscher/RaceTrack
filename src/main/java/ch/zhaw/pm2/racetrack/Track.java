package ch.zhaw.pm2.racetrack;


import ch.zhaw.pm2.racetrack.exceptions.InvalidTrackFormatException;
import ch.zhaw.pm2.racetrack.strategy.MoveStrategy;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * This class represents the racetrack board.
 *
 * <p>The racetrack board consists of a rectangular grid of 'width' columns and 'height' rows.
 * The zero point of he grid is at the top left. The x-axis points to the right and the y-axis points downwards.</p>
 * <p>Positions on the track grid are specified using {@link PositionVector} objects. These are vectors containing an
 * x/y coordinate pair, pointing from the zero-point (top-left) to the addressed space in the grid.</p>
 *
 * <p>Each position in the grid represents a space which can hold an enum object of type {@link Config.SpaceType}.<br>
 * Possible Space types are:
 * <ul>
 *  <li>WALL : road boundary or off track space</li>
 *  <li>TRACK: road or open track space</li>
 *  <li>FINISH_LEFT, FINISH_RIGHT, FINISH_UP, FINISH_DOWN :  finish line spaces which have to be crossed
 *      in the indicated direction to winn the race.</li>
 * </ul>
 * <p>Beside the board the track contains the list of cars, with their current state (position, velocity, crashed,...)</p>
 *
 * <p>At initialization the track grid data is read from the given track file. The track data must be a
 * rectangular block of text. Empty lines at the start are ignored. Processing stops at the first empty line
 * following a non-empty line, or at the end of the file.</p>
 * <p>Characters in the line represent SpaceTypes. The mapping of the Characters is as follows:
 * <ul>
 *   <li>WALL : '#'</li>
 *   <li>TRACK: ' '</li>
 *   <li>FINISH_LEFT : '&lt;'</li>
 *   <li>FINISH_RIGHT: '&gt;'</li>
 *   <li>FINISH_UP   : '^;'</li>
 *   <li>FINISH_DOWN: 'v'</li>
 *   <li>Any other character indicates the starting position of a car.<br>
 *       The character acts as the id for the car and must be unique.<br>
 *       There are 1 to {@link Config#MAX_CARS} allowed. </li>
 * </ul>
 * </p>
 * <p>All lines must have the same length, used to initialize the grid width).
 * Beginning empty lines are skipped.
 * The the tracks ends with the first empty line or the file end.<br>
 * An {@link InvalidTrackFormatException} is thrown, if
 * <ul>
 *   <li>not all track lines have the same length</li>
 *   <li>the file contains no track lines (grid height is 0)</li>
 *   <li>the file contains more than {@link Config#MAX_CARS} cars</li>
 * </ul>
 *
 * <p>The Track can return a String representing the current state of the race (including car positons)</p>
 */
public class Track {
    private List<Car> cars = new ArrayList<>();
    //(Y,X)
    private Config.SpaceType[][] grid;

    /**
     * Initialize a Track from the given track file.
     *
     * @param trackFile Reference to a file containing the track data
     * @throws FileNotFoundException       if the given track file could not be found
     * @throws InvalidTrackFormatException if the track file contains invalid data (no tracklines, no
     */
    public Track(File trackFile) throws IOException, InvalidTrackFormatException {
        TrackBuilder builder = new TrackBuilder();
        grid = builder.buildTrack(trackFile);
        for (Map.Entry<Character, PositionVector> entry : builder.getCarMap().entrySet()) {
            cars.add(new Car(entry.getValue(), entry.getKey()));
        }
    }

    public Config.SpaceType[][] getGrid() {
        return grid;
    }

    public int getCarCount() {
        return cars.size();
    }

    public char getCarId(int index) {
        return cars.get(index).getName();
    }

    public PositionVector getCarPos(int index) {
        return cars.get(index).getCarPosition();
    }

    public PositionVector getCarVelocity(int index) {
        return cars.get(index).getVelocity();
    }

    public Config.SpaceType getSpaceType(PositionVector position) {
        checkPosition(position);
        int x = position.getX();
        int y = position.getY();
        return grid[y][x];
    }

    public List<Car> getCars() {
        return cars;
    }

    public Car getCar(int carIndex) {
        checkCarIndex(carIndex);
        return cars.get(carIndex);
    }

    /**
     * Tells if some different from given car at the given position.
     *
     * @param position The position to be tested.
     * @return True, if car with different index is at given position.
     */
    public boolean isSomeOtherCarHere(int currentCarIndex, PositionVector position) {
        //todo test
        boolean isOtherCarHere = false;
        for (Car car : cars) {
            if (!car.equals(cars.get(currentCarIndex)) && car.getCarPosition().equals(position)) {
                isOtherCarHere = true;
            }
        }
        return isOtherCarHere;
    }

    /**
     * Accelerates a car with the given index.
     *
     * @param carIndex     The index of a car.
     * @param acceleration Acceleration vector of the car.
     */
    public void accelerateCar(int carIndex, PositionVector.Direction acceleration) {
        checkCarIndex(carIndex);
        cars.get(carIndex).accelerate(acceleration);
    }

    /**
     * Returns the next position of the given car.
     *
     * @param carIndex The zero-based car index number.
     * @return Car next position.
     */
    public PositionVector getCarNextPosition(int carIndex) {
        checkCarIndex(carIndex);
        return cars.get(carIndex).nextPosition();
    }

    /**
     * Crashes car.
     * Moves car to crash location and makes it inactive.
     *
     * @param carIndex      The zero-based car index number.
     * @param crashLocation A location of the crash.
     */
    public void crashCar(int carIndex, PositionVector crashLocation) {
        checkCarIndex(carIndex);
        checkPosition(crashLocation);
        cars.get(carIndex).crash(crashLocation);
    }

    /**
     * Moves car.
     *
     * @param carIndex The zero-based car index number
     */
    public void moveCar(int carIndex) {
        checkCarIndex(carIndex);
        cars.get(carIndex).move();
    }

    /**
     * Tells if the car is crashed.
     *
     * @param carIndex The zero-based car index number.
     * @return True, if the car is crashed
     */
    public boolean isCarCrashed(int carIndex) {
        checkCarIndex(carIndex);
        return cars.get(carIndex).isCrashed();
    }


    /**
     * Return a number of car which are still active(can be moved).
     *
     * @return integer number of active cars.
     */
    public int getNumberActiveCarsRemaining() {
        int counter = 0;
        for (Car car : cars) {
            if (!car.isCrashed()) {
                counter++;
            }
        }
        return counter;
    }

    /**
     * Tell if the given position is the finish line.
     *
     * @param position
     * @return True, if the position finish line.
     */
    public boolean isOnFinishLine(PositionVector position) {
        checkPosition(position);
        Config.SpaceType spaceType = getSpaceType(position);
        return spaceType == Config.SpaceType.FINISH_DOWN
                || spaceType == Config.SpaceType.FINISH_LEFT
                || spaceType == Config.SpaceType.FINISH_UP
                || spaceType == Config.SpaceType.FINISH_RIGHT;
    }

    /**
     * Tell if there is a wall at given position.
     *
     * @param position The position to be checked if there is a wall.
     * @return True if there is a wall at given position.
     */
    public boolean isTrackBound(PositionVector position) {
        checkPosition(position);
        return getSpaceType(position).equals(Config.SpaceType.WALL);
    }

    public void setStrategy(MoveStrategy moveStrategy, Car car) {
        car.setCarMoveStrategy(moveStrategy);
    }

    public void checkCarIndex(int carIndex) {
        // TODO IF SOMETHING GOES WRONG, COMMENT OUT THIS METHOD BODY
        if (carIndex > cars.size() - 1 || carIndex < 0) {
            throw new IllegalArgumentException();
        }
    }

    public void checkPosition(PositionVector position) {
        // TODO IF SOMETHING GOES WRONG, COMMENT OUT THIS METHOD BODY
        if (position.getX() > grid[0].length - 1 || position.getX() < 0) {
            throw new IllegalArgumentException();
        }
        if (position.getY() > grid.length - 1 || position.getY() < 0) {
            throw new IllegalArgumentException();
        }
    }

    @Override
    public String toString() {
        String gridString = "";
        //todo width and high
        char[][] charGrid = new char[grid.length][grid[0].length];
        //build track
        for (int y = 0; y < grid.length; y++) {
            for (int x = 0; x < grid[y].length; x++) {
                charGrid[y][x] = grid[y][x].getValue();
            }
        }
        //todo if a car is crashed how to know if it is? new symbol?? if (car.isCrashed){use other symbol}
        //map cars
        for (Car car : cars) {
            int x = car.getCarPosition().getX();
            int y = car.getCarPosition().getY();
            charGrid[y][x] = car.getName();
        }
        //build string
        for (int y = 0; y < charGrid.length; y++) {
            String charGridString = new String(charGrid[y]);
            gridString += charGridString + "\n";
        }
        //todo if "wall" > therefore "crashed", do we need new symbol??? or
        return gridString;
    }
}
