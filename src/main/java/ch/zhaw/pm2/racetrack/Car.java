package ch.zhaw.pm2.racetrack;

import ch.zhaw.pm2.racetrack.strategy.MoveStrategy;

/**
 * Class representing a car on the racetrack.
 * Uses {@link PositionVector} to store current position on the track grid and current velocity vector.
 * Each car has an identifier character which represents the car on the race track board.
 * Also keeps the state, if the car is crashed (not active anymore). The state can not be changed back to uncrashed.
 * The velocity is changed by providing an acceleration vector.
 * The car is able to calculate the endpoint of its next position and on request moves to it.
 */
public class Car {
    private PositionVector position;
    private PositionVector velocity = new PositionVector(0, 0);
    private char id;
    private boolean isCrashed = false;
    private MoveStrategy carMoveStrategy;

    /**
     * creates a car object with the needed parameters
     *
     * @param position takes the initial position from the map
     * @param id       takes its name (char) from the map
     */
    public Car(PositionVector position, char id) {
        this.position = position;
        this.id = id;
    }

    public MoveStrategy getCarMoveStrategy() {
        return carMoveStrategy;
    }

    public void setCarMoveStrategy(MoveStrategy carMoveStrategy) {
        this.carMoveStrategy = carMoveStrategy;
    }

    public char getId() {
        return id;
    }

    public PositionVector getCarPosition() {
        return position;
    }

    public PositionVector getVelocity() {
        return velocity;
    }

    public boolean isCrashed() {
        return isCrashed;
    }

    /**
     * Calculate the next position from to current position and calculated velocity at this turn.
     * <p>The formula used for calculation is: p<sub>n</sub>=p<sub>n-1</sub>+v<sub>n</sub></p>
     * <p>Where: </p>
     * <ul>
     *  <li>p<sub>n</sub> - new position after this turn</li>
     *  <li>v<sub>n</sub> - calculated velocity at this turn </li>
     *   <li>p<sub>n-1</sub> - position at the beginning of this turn.</li>
     * </ul>
     */
    public PositionVector nextPosition() {
        return PositionVector.add(velocity, position);
    }

    /**
     * Move the car to the end position.
     */
    public void move() {
        position = nextPosition();
    }

    /**
     * Crash the car.
     * <p>Move the car to crash location and set the crash status.</p>
     * <p>Note: Is used both for finish and crash. </p>
     * <p>Note: Cannot be undone.</p>
     */
    public void crash(PositionVector crashLocation) {
        this.isCrashed = true;
        position = crashLocation;
    }

    /**
     * Accelerate the car.
     * <p>Set velocity for the current turn by changing the old car velocity by given acceleration.</p>
     * <p>The formula used for calculation is: v<sub>n</sub>=v<sub>n-1</sub>+a<sub>n</sub></p>
     * <p>Where: </p>
     * <ul>
     *  <li>v<sub>n</sub> - the new velocity at this turn</li>
     *  <li>v<sub>n-1</sub> - velocity at turn before</li>
     *   <li>a<sub>n</sub> - acceleration at this turn.</li>
     * </ul>
     *
     * @param acceleration The acceleration at this turn.
     */
    public void accelerate(PositionVector.Direction acceleration) {
        velocity = PositionVector.add(velocity, acceleration.vector);
    }

    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof Car)){
            throw new ClassCastException();
        }
        final Car otherCar = (Car) obj;
        return this.id == otherCar.id;
    }
}
