package ch.zhaw.pm2.racetrack;

import ch.zhaw.pm2.racetrack.strategy.MoveStrategy;

import java.util.List;

public class TrackStub implements TrackInterface {
    @Override
    public Config.SpaceType[][] getGrid() {
        return new Config.SpaceType[0][];
    }

    @Override
    public int getCarCount() {
        return 0;
    }

    @Override
    public char getCarId(int index) {
        return 0;
    }

    @Override
    public PositionVector getCarPosition(int index) {
        return null;
    }

    @Override
    public PositionVector getCarVelocity(int index) {
        return null;
    }

    @Override
    public Config.SpaceType getSpaceType(PositionVector position) {
        return null;
    }

    @Override
    public List<Car> getCars() {
        return null;
    }

    @Override
    public Car getCar(int carIndex) {
        return null;
    }

    @Override
    public boolean isSomeOtherCarHere(int currentCarIndex, PositionVector position) {
        return false;
    }

    @Override
    public void accelerateCar(int carIndex, PositionVector.Direction acceleration) {

    }

    @Override
    public PositionVector getCarNextPosition(int carIndex) {
        return null;
    }

    @Override
    public void crashCar(int carIndex, PositionVector crashLocation) {

    }

    @Override
    public void moveCar(int carIndex) {

    }

    @Override
    public boolean isCarCrashed(int carIndex) {
        return false;
    }

    @Override
    public int getNumberActiveCarsRemaining() {
        return 0;
    }

    @Override
    public boolean isOnFinishLine(PositionVector position) {
        return false;
    }

    @Override
    public boolean isTrackBound(PositionVector position) {
        return false;
    }

    @Override
    public void setStrategy(MoveStrategy moveStrategy, Car car) {

    }

    @Override
    public void checkCarIndex(int carIndex) {

    }

    @Override
    public void checkPosition(PositionVector position) {

    }

    @Override
    public int getyDimension() {
        return 0;
    }

    @Override
    public int getxDimension() {
        return 0;
    }
}
