package ch.zhaw.pm2.racetrack;

import ch.zhaw.pm2.racetrack.strategy.MoveStrategy;

import java.util.List;

public class TrackStub implements TrackInterface {
    private PositionVector.Direction acceleration;

    public TrackStub(){

    }

    public PositionVector.Direction getAcceleration() {
        return acceleration;
    }

    @Override
    public Config.SpaceType[][] getGrid() {
        return new Config.SpaceType[0][];
    }

    @Override
    public int getCarCount() {
        return Config.MIN_CARS;
    }

    @Override
    public char getCarId(int index) {
        return 0;
    }

    @Override
    public PositionVector getCarPosition(int index) {
        return new PositionVector(0,0);
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
        this.acceleration = acceleration;
    }

    @Override
    public PositionVector getCarNextPosition(int carIndex) {
        return new PositionVector(0,0);
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
