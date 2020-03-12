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
    private char name;
    private boolean isCrashed = false;

    private MoveStrategy carMoveStrategy;

    public void setCarMoveStrategy(MoveStrategy carMoveStrategy) {
        this.carMoveStrategy = carMoveStrategy;
    }

    public MoveStrategy getCarMoveStrategy() {
        return carMoveStrategy;
    }

    public Car(PositionVector position, char name) {
        this.position = position;
        this.name = name;
    }


    public void setName(char name) {
        this.name = name;
    }

    public char getName() {
        return name;
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

    public void hasCrashed() {
        crashed = true;
    }
    public void setSpeed(PositionVector speed){
        this.speed = speed;
    }

    public void move(){
        //TODO implement
    }

    /**
     * Crash the car.
     * <p>Note: Cannot be undone.</p>
     */
    public void crash() {
        this.isCrashed = true;
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
        //todo decide if at this place arguments needs to be checked.
        velocity = PositionVector.add(velocity, acceleration.vector);
    }
}
