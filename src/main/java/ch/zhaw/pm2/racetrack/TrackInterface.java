package ch.zhaw.pm2.racetrack;

import java.util.List;

public interface TrackInterface {
    Config.SpaceType[][] getGrid();

    char getCarId(int index) throws IllegalArgumentException;

    PositionVector getCarPosition(int index) throws IllegalArgumentException;

    PositionVector getCarNextPosition(int carIndex) throws IllegalArgumentException;

    PositionVector getCarVelocity(int index) throws IllegalArgumentException;

    void accelerateCar(int carIndex, PositionVector.Direction acceleration) throws IllegalArgumentException;

    void crashCar(int carIndex, PositionVector crashLocation) throws IllegalArgumentException;

    void moveCar(int carIndex) throws IllegalArgumentException;

    int getCarCount();

    int getNumberActiveCarsRemaining();

    List<Car> getCars();

    Car getCar(int carIndex) throws IllegalArgumentException;

    Config.SpaceType getSpaceType(PositionVector position) throws IllegalArgumentException;

    boolean isSomeOtherCarHere(int currentCarIndex, PositionVector position) throws IllegalArgumentException;

    boolean isCarCrashed(int carIndex) throws IllegalArgumentException;

    boolean isOnFinishLine(PositionVector position) throws IllegalArgumentException;

    boolean isTrackBound(PositionVector position) throws IllegalArgumentException;

    @Override
    String toString();
}
