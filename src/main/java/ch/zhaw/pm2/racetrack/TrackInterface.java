package ch.zhaw.pm2.racetrack;

import ch.zhaw.pm2.racetrack.strategy.MoveStrategy;

import java.util.List;

public interface TrackInterface {
    Config.SpaceType[][] getGrid();

    int getCarCount();

    char getCarId(int index);

    PositionVector getCarPosition(int index);

    PositionVector getCarVelocity(int index);

    Config.SpaceType getSpaceType(PositionVector position);

    List<Car> getCars();

    Car getCar(int carIndex);

    boolean isSomeOtherCarHere(int currentCarIndex, PositionVector position);

    void accelerateCar(int carIndex, PositionVector.Direction acceleration);

    PositionVector getCarNextPosition(int carIndex);

    void crashCar(int carIndex, PositionVector crashLocation);

    void moveCar(int carIndex);

    boolean isCarCrashed(int carIndex);

    int getNumberActiveCarsRemaining();

    boolean isOnFinishLine(PositionVector position);

    boolean isTrackBound(PositionVector position);

    void setStrategy(MoveStrategy moveStrategy, Car car);

    void checkCarIndex(int carIndex);

    void checkPosition(PositionVector position);

    int getyDimension();

    int getxDimension();

    @Override
    String toString();
}
