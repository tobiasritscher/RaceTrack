package ch.zhaw.pm2.racetrack;

import ch.zhaw.pm2.racetrack.strategy.MoveStrategy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TrackStub implements TrackInterface {
    private int wishedCarCount;

    private PositionVector wishedCarPosition;
    private PositionVector wishedNextCarPosition;
    private PositionVector wishedCarVelocity;

    private int givenCarIndex;
    private PositionVector.Direction givenAcceleration;

    private Map<PositionVector, Config.SpaceType> fakeGrid = new HashMap<>();

    private Map<Integer, Boolean> isTheCarCrashed = new HashMap<>();
    private int numberActiveCars;
    private boolean isTrackBound;

    public TrackStub() {

    }

    public TrackStub(int carCount) {
        this.wishedCarCount = carCount;
    }

    List<Integer> getActiveCarsList() {
        List<Integer> activeCarsList = new ArrayList<>();
        for (Integer i : isTheCarCrashed.keySet())
            if (!isTheCarCrashed.get(i)) {
                activeCarsList.add(i);
            }
        return activeCarsList;
    }

    @Override
    public int getCarCount() {
        return wishedCarCount;
    }

    public int getGivenCarIndex() {
        return givenCarIndex;
    }

    public PositionVector.Direction getGivenAcceleration() {
        return givenAcceleration;
    }

    @Override
    public Config.SpaceType[][] getGrid() {
        return new Config.SpaceType[0][];
    }

    public void setWishedPositionSpaceType(PositionVector wishedPosition, Config.SpaceType wishedSpaceType) {
        fakeGrid.put(wishedPosition, wishedSpaceType);
    }

    @Override
    public char getCarId(int index) {
        return 0;
    }


    public void setWishedCarPosition(PositionVector wishedCarPosition) {
        this.wishedCarPosition = wishedCarPosition;
    }

    public void setWishedNextCarPosition(PositionVector wishedNextCarPosition) {
        this.wishedNextCarPosition = wishedNextCarPosition;
    }

    @Override
    public PositionVector getCarPosition(int index) {
        return wishedCarPosition;
    }

    @Override
    public PositionVector getCarNextPosition(int carIndex) {
        return wishedNextCarPosition;
    }

    public void setWishedCarVelocity(PositionVector velocity) {
        wishedCarVelocity = velocity;
    }

    @Override
    public PositionVector getCarVelocity(int index) {
        return wishedCarVelocity;
    }

    @Override
    public Config.SpaceType getSpaceType(PositionVector position) {
        return fakeGrid.get(position);
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
        this.givenCarIndex = carIndex;
        this.givenAcceleration = acceleration;
    }

    @Override
    public void crashCar(int carIndex, PositionVector crashLocation) {
        setWishedIsCarCrashed(carIndex, true);
    }

    @Override
    public void moveCar(int carIndex) {

    }

    public void setWishedIsCarCrashed(int carIndex, boolean isCrashed) {
        isTheCarCrashed.put(carIndex, isCrashed);
        if (!isCrashed) {
            numberActiveCars++;
        } else {
            numberActiveCars--;
        }
    }

    @Override
    public boolean isCarCrashed(int carIndex) {
        return isTheCarCrashed.get(carIndex);
    }

    public void setWisheActiveCarNumber(int carNumber) {
        numberActiveCars = carNumber;
    }

    @Override
    public int getNumberActiveCarsRemaining() {
        return numberActiveCars;
    }

    @Override
    public boolean isOnFinishLine(PositionVector position) {
        return fakeGrid.get(position) == Config.SpaceType.FINISH_LEFT || fakeGrid.get(position) == Config.SpaceType.FINISH_RIGHT;
    }


    public void setWishedIsTrackBound(boolean isTrackBound) {
        this.isTrackBound = isTrackBound;
    }

    @Override
    public boolean isTrackBound(PositionVector position) {
        return isTrackBound;
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
