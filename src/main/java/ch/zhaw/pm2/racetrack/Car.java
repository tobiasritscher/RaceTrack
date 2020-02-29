package ch.zhaw.pm2.racetrack;

import java.awt.Point;

/**
 * Class representing a car on the racetrack.
 * Uses {@link PositionVector} to store current position on the track grid and current velocity vector.
 * Each car has an identifier character which represents the car on the race track board.
 * Also keeps the state, if the car is crashed (not active anymore). The state can not be changed back to uncrashed.
 * The velocity is changed by providing an acelleration vector.
 * The car is able to calculate the endpoint of its next position and on request moves to it.
 */
public class Car {
    private Point position;
    private Point speed = new Point(0, 0);
    private char name;
    private boolean crashed = false;
    private boolean byUser = true;

    public Car(Point position, char name) {
        this.position = position;
        this.name = name;
    }

    public Car() {

    }

    /**
     * calculates the car's new speed and position
     *
     * @param accelaration takes in the user input for desired acceleration
     * @return position     returns the new position of the car
     * @throws IllegalArgumentException if given acceleration is not [-1,0,1]
     */
    public Point newSpeedandPosition(Point accelaration) throws IllegalArgumentException {
        if (accelaration.getX() < -1 || accelaration.getX() > 1 || accelaration.getY() < -1 || accelaration.getY() > 1) {
            throw new IllegalArgumentException("cant have such fast acceleration");
        } else if(byUser) {
            speed.x = (int) (speed.getX() + accelaration.getX());
            speed.y = (int) (speed.getY() + accelaration.getY());
            position.x = (int) (position.getX() + speed.getX());
            position.y = (int) (position.getY() + speed.getY());
        }else {
            return position;
        }
        return position;
    }

    public void byMachine(){
        byUser = false;
    }
    public void setPosition(Point position) {
        this.position = position;
    }

    public void setName(char name) {
        this.name = name;
    }

    public char getName() {
        return name;
    }

    public Point getPosition() {
        return position;
    }

    public Point getSpeed() {
        return speed;
    }

    public boolean isCrashed() {
        return crashed;
    }

    public void hasCrashed() {
        crashed = true;
    }

}
