package ch.zhaw.pm2.racetrack;

import ch.zhaw.pm2.racetrack.exceptions.InvalidTrackFormatException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class GameTest {

    public static final boolean THIS_CAR_IS_CRASHED = true;
    public static final boolean THIS_CAR_IS_NOT_CRASHED = false;
    public static final PositionVector ARBITRARY_VALID_FINISH_POSITION = new PositionVector(1, 0);
    public static final PositionVector ARBITRARY_INVALID_FINISH_POSITION = new PositionVector(Integer.MIN_VALUE, Integer.MIN_VALUE);
    public static final PositionVector ZERO_POSITION_VECTOR = new PositionVector(0, 0);
    public static final PositionVector ARBITRARY_VALID_CAR_POSITION = new PositionVector(1, 0);

    Random random = new Random();

    @Mock
    Track mockedTrack;

    @Test
    public void getFinishDirectionUnitVector_FinishUp() {
        mockedTrack = mock(Track.class);
        Game sampleGame = new Game(mockedTrack);
        when(mockedTrack.getSpaceType(ARBITRARY_VALID_FINISH_POSITION)).thenReturn(Config.SpaceType.FINISH_UP);
        Assertions.assertEquals(PositionVector.Direction.UP.vector, sampleGame.getFinishDirectionUnitVector(ARBITRARY_VALID_FINISH_POSITION));
    }

    @Test
    public void getFinishDirectionUnitVector_FinishDown() {
        mockedTrack = mock(Track.class);
        Game sampleGame = new Game(mockedTrack);
        when(mockedTrack.getSpaceType(ARBITRARY_VALID_FINISH_POSITION)).thenReturn(Config.SpaceType.FINISH_DOWN);
        Assertions.assertEquals(PositionVector.Direction.DOWN.vector, sampleGame.getFinishDirectionUnitVector(ARBITRARY_VALID_FINISH_POSITION));
    }

    @Test
    public void getFinishDirectionUnitVector_FinishLeft() {
        mockedTrack = mock(Track.class);
        Game sampleGame = new Game(mockedTrack);
        when(mockedTrack.getSpaceType(ARBITRARY_VALID_FINISH_POSITION)).thenReturn(Config.SpaceType.FINISH_LEFT);
        Assertions.assertEquals(PositionVector.Direction.LEFT.vector, sampleGame.getFinishDirectionUnitVector(ARBITRARY_VALID_FINISH_POSITION));
    }

    @Test
    public void getFinishDirectionUnitVector_FinishRight() {
        mockedTrack = mock(Track.class);
        Game sampleGame = new Game(mockedTrack);
        when(mockedTrack.getSpaceType(ARBITRARY_VALID_FINISH_POSITION)).thenReturn(Config.SpaceType.FINISH_RIGHT);
        Assertions.assertEquals(PositionVector.Direction.RIGHT.vector, sampleGame.getFinishDirectionUnitVector(ARBITRARY_VALID_FINISH_POSITION));
    }

    @Test
    public void getFinishDirectionUnitVector_PositionIsTrack() {
        mockedTrack = mock(Track.class);
        Game sampleGame = new Game(mockedTrack);
        when(mockedTrack.getSpaceType(ARBITRARY_VALID_FINISH_POSITION)).thenReturn(Config.SpaceType.TRACK);
        Assertions.assertEquals(ZERO_POSITION_VECTOR, sampleGame.getFinishDirectionUnitVector(ARBITRARY_VALID_FINISH_POSITION));
    }

    @Test
    public void getFinishDirectionUnitVector_PositionIsWall() {
        mockedTrack = mock(Track.class);
        Game sampleGame = new Game(mockedTrack);
        when(mockedTrack.getSpaceType(ARBITRARY_VALID_FINISH_POSITION)).thenReturn(Config.SpaceType.WALL);
        Assertions.assertEquals(ZERO_POSITION_VECTOR, sampleGame.getFinishDirectionUnitVector(ARBITRARY_VALID_FINISH_POSITION));
    }

    @Test
    public void getFinishDirectionUnitVector_NoSuchPosition() {
        mockedTrack = mock(Track.class);
        Game sampleGame = new Game(mockedTrack);
        Assertions.assertEquals(ZERO_POSITION_VECTOR, sampleGame.getFinishDirectionUnitVector(ARBITRARY_INVALID_FINISH_POSITION));
    }

    /**
     * Makes mockedTrack.getCarCount() return number.
     *
     * @param number Th positive integer number of cars.
     */
    private void initializeMockedTrackWithGivenNumberCars(int number) {
        if (number < 0) {
            throw new IllegalArgumentException();
        }
        mockedTrack = mock(Track.class);
        when(mockedTrack.getCarCount()).thenReturn(number);
    }

    /**
     * Returns the list with given number cars.
     * The caller must make sure valid parameter is passed.
     *
     * @param numberCars The integer number of cars > 0.
     * @return a non null list of cars, which position is initialized with ARBITRARY_VALID_CAR_POSITION and carId is set to ASCII symbols starting by 'a'
     * @throws IllegalArgumentException numberCars < 1
     */
    private List<Car> getCarsList(int numberCars) {
        if (numberCars < 1) {
            throw new IllegalArgumentException();
        }
        List<Car> cars = new ArrayList<>();
        for (int i = 0; i < numberCars; i++) {
            final int ASCII_MAX = 128;
            cars.add(new Car(ARBITRARY_VALID_CAR_POSITION, (char) (('a' + i) % ASCII_MAX)));
        }
        return cars;
    }

    @Test
    public void switchToNextActiveCar_Five_Cars() {
        final int NUMBER_ACTIVE_CARS = 5;
        final int NUMBER_LOOPS = 3;

        List<Car> cars = getCarsList(NUMBER_ACTIVE_CARS);

        initializeMockedTrackWithGivenNumberCars(NUMBER_ACTIVE_CARS);
        for (int i = 0; i < cars.size(); i++) {
            when(mockedTrack.getCar(i)).thenReturn(cars.get(i));
        }

        Game sampleGame = new Game(mockedTrack);
        for (int i = 0; i < cars.size() * NUMBER_LOOPS; i++) {
            int expectedCarIndex = (i + Game.FIRST_TURN_CAR_INDEX) % cars.size();
            Assertions.assertEquals(expectedCarIndex, sampleGame.getCurrentCarIndex());
            sampleGame.switchToNextActiveCar();
        }
    }

    @Test
    public void switchToNextActiveCar_TenCars_OnlyOddsAndInitialAreActive() {
        final int NUMBER_CARS = 10;
        final int NUMBER_LOOPS = 3;

        List<Car> cars = getCarsList(NUMBER_CARS);

        initializeMockedTrackWithGivenNumberCars(NUMBER_CARS);
        for (int i = 0; i < cars.size(); i++) {
            when(mockedTrack.getCar(i)).thenReturn(cars.get(i));
        }
        List<Integer> activeCarIndexes = new ArrayList<>();
        for (int i = 0; i < NUMBER_CARS; i++) {
            if (i % 2 != 0) {
                activeCarIndexes.add(i);
            }
        }
        if (!activeCarIndexes.contains(Game.FIRST_TURN_CAR_INDEX)) {
            activeCarIndexes.add(Game.FIRST_TURN_CAR_INDEX);
            Collections.sort(activeCarIndexes);
        }
        for (Integer activeCarIndex : activeCarIndexes) {
            when(mockedTrack.isCarCrashed(activeCarIndex)).thenReturn(THIS_CAR_IS_NOT_CRASHED);
        }
        for (int i = 0; i < NUMBER_CARS; i++) {
            if (!activeCarIndexes.contains(i)) {
                when(mockedTrack.isCarCrashed(i)).thenReturn(THIS_CAR_IS_CRASHED);
            }
        }

        Game sampleGame = new Game(mockedTrack);
        for (int i = 0; i < (cars.size() * NUMBER_LOOPS); i++) {
            int expectedCarIndex = activeCarIndexes.get((activeCarIndexes.indexOf(Game.FIRST_TURN_CAR_INDEX) + i) % activeCarIndexes.size());
            Assertions.assertEquals(expectedCarIndex, sampleGame.getCurrentCarIndex());
            sampleGame.switchToNextActiveCar();
        }
    }


    @Test
    public void switchToNextActiveCar_TwentyCars_InitialAndTwoRandomCarsAreActive() {
        final int NUMBER_CARS = 20;
        final int NUMBER_ACTIVE_CARS = 3;

        initializeMockedTrackWithGivenNumberCars(NUMBER_CARS);

        List<Car> cars = getCarsList(NUMBER_CARS);
        for (int i = 0; i < cars.size(); i++) {
            when(mockedTrack.getCar(i)).thenReturn(cars.get(i));
        }


        List<Integer> activeCarsIndexes = new ArrayList<>();
        activeCarsIndexes.add(Game.FIRST_TURN_CAR_INDEX);
        for (int i = 0; i < NUMBER_ACTIVE_CARS - 1; i++) {
            int randomlyPickedCarIndex;
            do {
                randomlyPickedCarIndex = random.nextInt(NUMBER_CARS);
            } while (activeCarsIndexes.contains(randomlyPickedCarIndex));
            activeCarsIndexes.add(randomlyPickedCarIndex);
        }
        for (int i = 0; i < activeCarsIndexes.size(); i++) {
            when(mockedTrack.isCarCrashed(activeCarsIndexes.indexOf(i))).thenReturn(THIS_CAR_IS_NOT_CRASHED);
        }
        Collections.sort(activeCarsIndexes);
        for (int i = 0; i < NUMBER_CARS; i++) {
            if (!activeCarsIndexes.contains(i)) {
                when(mockedTrack.isCarCrashed(i)).thenReturn(THIS_CAR_IS_CRASHED);
            }
        }

        Game sampleGame = new Game(mockedTrack);
        for (int i = 0; i < cars.size(); i++) {
            int index = (i + activeCarsIndexes.indexOf(Game.FIRST_TURN_CAR_INDEX)) % activeCarsIndexes.size();
            int expectedCarIndex = activeCarsIndexes.get(index);
            Assertions.assertEquals(expectedCarIndex, sampleGame.getCurrentCarIndex());
            sampleGame.switchToNextActiveCar();
        }
    }


    @Test
    public void switchToNextActiveCar_TwentyCars_NoActiveCars() {
        final int NUMBER_CARS = 20;


        List<Car> cars = getCarsList(NUMBER_CARS);

        initializeMockedTrackWithGivenNumberCars(NUMBER_CARS);
        for (int i = 0; i < cars.size(); i++) {
            when(mockedTrack.getCar(i)).thenReturn(cars.get(i));
        }
        for (int i = 0; i < NUMBER_CARS; i++) {
            final boolean THIS_CAR_IS_CRASHED = true;
            when(mockedTrack.isCarCrashed(i)).thenReturn(THIS_CAR_IS_CRASHED);
        }

        final int NO_ACTIVE_CARS = -1;
        Game sampleGame = new Game(mockedTrack);
        for (int i = 0; i < cars.size(); i++) {
            sampleGame.switchToNextActiveCar();
            Assertions.assertEquals(NO_ACTIVE_CARS, sampleGame.getCurrentCarIndex());
        }
    }

    @Test
    public void calculatePath_SamePoint() {
        final PositionVector START_POINT = ZERO_POSITION_VECTOR;
        final PositionVector END_POINT = START_POINT;
        mockedTrack = mock(Track.class);
        Game sampleGame = new Game(mockedTrack);
        List<PositionVector> expectedPath = new ArrayList<>();
        expectedPath.add(ZERO_POSITION_VECTOR);
        Assertions.assertEquals(expectedPath, sampleGame.calculatePath(START_POINT, END_POINT));
    }

    @Test
    public void calculatePath_inX_LengthOne() {
        final int PATH_LENGTH = 1;
        final PositionVector START_POINT = ZERO_POSITION_VECTOR;
        final PositionVector END_POINT = new PositionVector(PATH_LENGTH, 0);
        mockedTrack = mock(Track.class);
        Game sampleGame = new Game(mockedTrack);
        List<PositionVector> expectedPath = new ArrayList<>();
        expectedPath.add(START_POINT);
        expectedPath.add(END_POINT);
        Assertions.assertEquals(expectedPath, sampleGame.calculatePath(START_POINT, END_POINT));
    }

    @Test
    public void calculatePath_inX_StraightLine() {
        final int PATH_LENGTH = 20;
        final PositionVector START_POINT = ZERO_POSITION_VECTOR;
        final PositionVector END_POINT = new PositionVector(PATH_LENGTH, 0);
        mockedTrack = mock(Track.class);
        Game sampleGame = new Game(mockedTrack);
        List<PositionVector> expectedPath = new ArrayList<>();
        for (int x = 0; x <= PATH_LENGTH; x++) {
            expectedPath.add(new PositionVector(x, 0));
        }
        Assertions.assertEquals(expectedPath, sampleGame.calculatePath(START_POINT, END_POINT));
    }

    @Test
    public void calculatePath_inY_LengthOne() {
        final int PATH_LENGTH = 1;
        final PositionVector START_POINT = ZERO_POSITION_VECTOR;
        final PositionVector END_POINT = new PositionVector(0, PATH_LENGTH);
        mockedTrack = mock(Track.class);
        Game sampleGame = new Game(mockedTrack);
        List<PositionVector> expectedPath = new ArrayList<>();
        expectedPath.add(START_POINT);
        expectedPath.add(END_POINT);
        Assertions.assertEquals(expectedPath, sampleGame.calculatePath(START_POINT, END_POINT));
    }

    @Test
    public void calculatePath_inY_StraightLine() {
        final int PATH_LENGTH = 20;
        final PositionVector START_POINT = ZERO_POSITION_VECTOR;
        final PositionVector END_POINT = new PositionVector(0, PATH_LENGTH);
        mockedTrack = mock(Track.class);
        Game sampleGame = new Game(mockedTrack);
        List<PositionVector> expectedPath = new ArrayList<>();
        for (int y = 0; y <= PATH_LENGTH; y++) {
            expectedPath.add(new PositionVector(0, y));
        }
        Assertions.assertEquals(expectedPath, sampleGame.calculatePath(START_POINT, END_POINT));
    }

    /**
     * Check if an expected car was accelerated how it was intended.
     */
    //do carTurn()
    @Test
    public void doCarTurn_AccelerateCar() {

        final int NUMBER_CARS = Config.MIN_CARS;
        TrackStub trackStub = new TrackStub(NUMBER_CARS);

        Game sampleGame = new Game(trackStub);
        for (int i = 0; i < NUMBER_CARS; i++) {
            trackStub.setWishedIsCarCrashed(i, false);
        }

        trackStub.setWishedCarPosition(ZERO_POSITION_VECTOR);
        trackStub.setWishedNextCarPosition(ZERO_POSITION_VECTOR);

        sampleGame.doCarTurn(PositionVector.Direction.UP);
        Assertions.assertEquals(PositionVector.Direction.UP, trackStub.getGivenAcceleration());
        Assertions.assertEquals(Game.FIRST_TURN_CAR_INDEX, trackStub.getGivenCarIndex());
    }

    /**
     * StubTrack with Config.MAX_CARS will be generated.
     * At the begin a random car will be picked as an expected winner.
     * Each turn will be randomly decided if current car(excluded the expected one) will crash or not.
     * In the end wll be evaluated if the winner car index was set correctly.
     */
    @Test
    public void doCarTurn_LastRemainingCarIsTheWinner() {
        final int NUMBER_CARS = Config.MAX_CARS;
        final int WINNER_CAR_INDEX = random.nextInt(NUMBER_CARS);

        TrackStub trackStub = new TrackStub(NUMBER_CARS);

        for (int i = 0; i < NUMBER_CARS; i++) {
            trackStub.setWishedIsCarCrashed(i, false);
        }

        trackStub.setWishedCarPosition(ZERO_POSITION_VECTOR);
        trackStub.setWishedNextCarPosition(ARBITRARY_VALID_CAR_POSITION);

        Game sampleGame = new Game(trackStub);

        //do some turns without crashes
        final int NUMBER_TURNS_WITHOUT_CRASHES = 5;
        for (int i = 0; i < NUMBER_TURNS_WITHOUT_CRASHES; i++) {
            sampleGame.doCarTurn(PositionVector.Direction.UP);
        }

        while (sampleGame.getWinner() == Game.NO_WINNER) {
            int currentCarIndex = sampleGame.getCurrentCarIndex();
            //decide whether to crash the car or not
            if (random.nextBoolean() && currentCarIndex != WINNER_CAR_INDEX) {
                trackStub.setWishedIsTrackBound(true);
            }
            sampleGame.doCarTurn(PositionVector.Direction.UP);
            trackStub.setWishedIsTrackBound(false);
        }

        Assertions.assertEquals(sampleGame.getWinner(), WINNER_CAR_INDEX);

    }

    /**
     * <ol>
     * <li>The trackStub with Config.MAX_CARS will be generated.</li>
     * <li>Two random cars will be left active.</li>
     * <li>The index of expected car to win will be saved(is not the current one).</li>
     * <li>Current car will be crashed.</li>
     * <li>The winner index should be set to the expected car index.</li>
     * </ol>
     */
    @Test
    public void doCarTurn_OneOfTwoRemainingActiveCarsCrashesTheRemainingCarIsTheWinner() {
        final int NUMBER_CARS = Config.MAX_CARS;
        TrackStub trackStub = new TrackStub(NUMBER_CARS);
        Game sampleGame = new Game(trackStub);
        for (int i = 0; i < NUMBER_CARS; i++) {
            trackStub.setWishedIsCarCrashed(i, false);
        }

        //crash all cars except two
        final int NUMBER_CARS_LEFT_ACTIVE = 2;
        for (int i = 0; i < NUMBER_CARS - NUMBER_CARS_LEFT_ACTIVE; i++) {
            int carIndex = random.nextInt(NUMBER_CARS);
            while (trackStub.isCarCrashed(carIndex)) {
                carIndex = random.nextInt(NUMBER_CARS);
            }
            trackStub.setWishedIsCarCrashed(carIndex, true);
        }

        sampleGame.switchToNextActiveCar();
        // the expected car to win is not the current one
        int expectedIndex = trackStub.getActiveCarsList().get(0);
        if (expectedIndex == sampleGame.getCurrentCarIndex()) {
            expectedIndex = trackStub.getActiveCarsList().get(1);
        }

        trackStub.setWishedCarPosition(ZERO_POSITION_VECTOR);
        trackStub.setWishedNextCarPosition(ARBITRARY_VALID_CAR_POSITION);

        //crash current car
        trackStub.setWishedIsTrackBound(true);
        sampleGame.doCarTurn(PositionVector.Direction.UP);

        Assertions.assertEquals(expectedIndex, sampleGame.getWinner());
    }

    /**
     * <ol>
     *     <li>TestStub with Config.MIN_CARS will be generated.</li>
     *     <li>Test path the length 3 will be generated: (1,0),(2,0)(3,0)</li>
     *     <li>For each path point SpaceType will be ordered: (1,0)->SpaceType.TRACK,(2,0)->SpaceType.FINISH_LEFT,(3,0)->SpaceType.TRACK</li>
     *     <li>First car will cross the line in reversed direction an then in the right direction, going back to it start position.(Second car turn is just skipped.)</li>
     *     <li>The winner should be Game.NO_WINNER</li>
     * </ol>
     */
    @Test
    public void doCarTurn_CrossLineInFalseDirectionGoReverseTheWinnerIsNoWinner() {

        final int NUMBER_CARS = Config.MIN_CARS;
        TrackStub trackStub = new TrackStub(NUMBER_CARS);
        Game sampleGame = new Game(trackStub);
        trackStub.setWishedIsCarCrashed(0, false);
        trackStub.setWishedIsCarCrashed(1, false);

        //set up path
        final int TEST_PATH_LENGTH = 3;
        final PositionVector CAR_START_POSITION = new PositionVector(1, 0);
        final PositionVector CAR_END_POSITION = new PositionVector(TEST_PATH_LENGTH, 0);
        List<PositionVector> testPath = sampleGame.calculatePath(CAR_START_POSITION, CAR_END_POSITION);
        trackStub.setWishedPositionSpaceType(testPath.get(0), Config.SpaceType.TRACK);
        trackStub.setWishedPositionSpaceType(testPath.get(1), Config.SpaceType.FINISH_LEFT);
        trackStub.setWishedPositionSpaceType(testPath.get(2), Config.SpaceType.TRACK);

        //first car: cross fl in reversed direction.
        trackStub.setWishedCarVelocity(PositionVector.subtract(testPath.get(2), testPath.get(0)));
        trackStub.setWishedCarPosition(CAR_START_POSITION);
        trackStub.setWishedNextCarPosition(CAR_END_POSITION);
        sampleGame.doCarTurn(PositionVector.Direction.RIGHT);
        Assertions.assertEquals(Game.NO_WINNER, sampleGame.getWinner());

        //skip second car
        sampleGame.switchToNextActiveCar();

        //fist car: cross fl in right direction
        trackStub.setWishedCarVelocity(PositionVector.subtract(testPath.get(0), testPath.get(2)));
        trackStub.setWishedCarPosition(CAR_END_POSITION);
        trackStub.setWishedNextCarPosition(CAR_START_POSITION);
        sampleGame.doCarTurn(PositionVector.Direction.LEFT);
        Assertions.assertEquals(Game.NO_WINNER, sampleGame.getWinner());
    }


    /**
     * Car "a" crashes into car "b".
     * After the crash car "a" stays crashed und don't move.
     */
    @Test
    public void doCarTurn_CrashedCarStaysCrashed() throws IOException, InvalidTrackFormatException {
        Track sampleTrack = new Track(new File("testtracks/game-testtracks/stay_crashed.txt"));
        Game sampleGame = new Game(sampleTrack);
        //crash a
        final int staysCrashedCarIndex = sampleGame.getCurrentCarIndex();
        Assertions.assertFalse(sampleTrack.isCarCrashed(staysCrashedCarIndex));
        sampleGame.doCarTurn(PositionVector.Direction.RIGHT);
        Assertions.assertTrue(sampleTrack.isCarCrashed(staysCrashedCarIndex));

        final PositionVector staysCrashedCarCrashLocation = sampleGame.getCarPosition(staysCrashedCarIndex);
        while (sampleGame.getWinner() == Game.NO_WINNER) {
            sampleGame.doCarTurn(PositionVector.Direction.RIGHT);
            Assertions.assertTrue(sampleTrack.isCarCrashed(staysCrashedCarIndex));
            Assertions.assertEquals(staysCrashedCarCrashLocation, sampleTrack.getCarPosition(staysCrashedCarIndex));
        }
    }

    /**
     * Car "a" crashes in car "b".
     * The test checks if car "b" can still move.
     */
    @Test
    public void doCarTurn_CrashVictimIsAbleToMove() throws IOException, InvalidTrackFormatException {
        Track sampleTrack = new Track(new File("testtracks/game-testtracks/stay_crashed.txt"));
        Game sampleGame = new Game(sampleTrack);

        final int criminalCarIndex = sampleGame.getCurrentCarIndex();
        Assertions.assertFalse(sampleTrack.isCarCrashed(criminalCarIndex));
        sampleGame.doCarTurn(PositionVector.Direction.RIGHT);
        Assertions.assertTrue(sampleTrack.isCarCrashed(criminalCarIndex));

        final int victimCarIndex = sampleGame.getCurrentCarIndex();
        Assertions.assertFalse(sampleTrack.isCarCrashed(victimCarIndex));
        while (sampleGame.getWinner() == Game.NO_WINNER) {
            sampleGame.doCarTurn(PositionVector.Direction.RIGHT);
        }
        Assertions.assertEquals(sampleGame.getWinner(), victimCarIndex);
    }

    /**
     * Cars "a" is able to cross the finish line, when cars "d" to "g" are crashed at one point.
     */
    @Test
    public void doCarTurn_MultipleCarsCrashAtOneLocationCarAisAbleToFinish() throws IOException, InvalidTrackFormatException {
        Track sampleTrack = new Track(new File("testtracks/game-testtracks/crash_line.txt"));
        Game sampleGame = new Game(sampleTrack);
        //move a,b
        sampleGame.doCarTurn(PositionVector.Direction.RIGHT);
        sampleGame.doCarTurn(PositionVector.Direction.RIGHT);
        //c halt
        sampleGame.doCarTurn(PositionVector.Direction.NONE);
        //crash d,e,f,g
        while (sampleGame.getWinner() == Game.NO_WINNER) {
            sampleGame.doCarTurn(PositionVector.Direction.RIGHT);
        }
        Assertions.assertTrue(sampleTrack.isCarCrashed(3));
        Assertions.assertTrue(sampleTrack.isCarCrashed(4));
        Assertions.assertTrue(sampleTrack.isCarCrashed(5));
        Assertions.assertTrue(sampleTrack.isCarCrashed(6));
        Assertions.assertEquals(sampleTrack.getCarPosition(3), sampleTrack.getCarPosition(4));
        Assertions.assertEquals(sampleTrack.getCarPosition(4), sampleTrack.getCarPosition(5));
        Assertions.assertEquals(sampleTrack.getCarPosition(5), sampleTrack.getCarPosition(6));
        Assertions.assertEquals(0, sampleGame.getWinner());
    }

    /**
     * Track with test set up will be loaded, which expects,if move to right and car "b" doesn't move:
     * <ol>
     *     <li>car a - crash with car be.</li>
     *     <li>car c- crash with wall.</li>
     *     <li>car d- will cross the finish line.</li>
     * </ol>
     */
    @Test
    public void doCarTurn_CollisionWithObstacles() throws IOException, InvalidTrackFormatException {
        Track sampleTrack = new Track(new File("testtracks/game-testtracks/crashes_into_onstacles.txt"));
        Game sampleGame = new Game(sampleTrack);
        //crash a with car b
        sampleGame.doCarTurn(PositionVector.Direction.RIGHT);
        Assertions.assertTrue(sampleTrack.isCarCrashed(0));

        //car b stand still
        sampleGame.doCarTurn(PositionVector.Direction.NONE);

        //crash c with wall
        sampleGame.doCarTurn(PositionVector.Direction.RIGHT);
        Assertions.assertTrue(sampleTrack.isCarCrashed(2));

        sampleGame.doCarTurn(PositionVector.Direction.RIGHT);

        Assertions.assertTrue(sampleGame.getWinner() == 3);
    }
}
