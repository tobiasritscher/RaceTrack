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

    public Car(Point position, char name) {
        this.position = position;
        this.name = name;
    }
    public char getName(){
        return name;
    }
    public Point getPosition(){
        return position;
    }
    public Point getSpeed(){
        return speed;
    }
    public boolean isCrashed(){
        return crashed;
    }
    public void hasCrashed(){
        crashed = true;
    }
}
