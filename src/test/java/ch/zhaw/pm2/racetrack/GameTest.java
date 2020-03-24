package ch.zhaw.pm2.racetrack;

import ch.zhaw.pm2.racetrack.exceptions.GameException;
import ch.zhaw.pm2.racetrack.exceptions.InvalidTrackFormatException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GameTest {

    public static final PositionVector ARBITRARY_POSITION = new PositionVector(1, 0);
    public static final PositionVector ZERO_POSITION_VECTOR = new PositionVector(0, 0);

    Random random = new Random();

    private TrackStub trackStub;
    private Game sampleGame;
    private Track sampleTrack;

    @AfterEach
    public void teardown() {
        trackStub = null;
        sampleGame = null;
        sampleTrack = null;
        Config.setNumberLaps(1);
        Config.setFirstTurnCarIndex(0);
    }

    /**
     * Game with default TackStub will be generated.
     */
    private void setUpGameWithDefaultTrackStub() {
        trackStub = new TrackStub();
        sampleGame = new Game(trackStub);
    }

    /**
     * Does the following:
     * <ol>
     *     <li>Place wished finish type at wished position.</li>
     *     <li>Check if expected unit vector was ordered.</li>
     * </ol>
     */
    @Test
    public void getFinishDirectionUnitVector_PositionIsValidFinishType() {
        setUpGameWithDefaultTrackStub();
        trackStub.setWishedPositionSpaceType(ARBITRARY_POSITION, Config.SpaceType.FINISH_UP);
        Assertions.assertEquals(PositionVector.Direction.UP.vector, sampleGame.getFinishDirectionUnitVector(ARBITRARY_POSITION));
        trackStub.setWishedPositionSpaceType(ARBITRARY_POSITION, Config.SpaceType.FINISH_DOWN);
        Assertions.assertEquals(PositionVector.Direction.DOWN.vector, sampleGame.getFinishDirectionUnitVector(ARBITRARY_POSITION));
        trackStub.setWishedPositionSpaceType(ARBITRARY_POSITION, Config.SpaceType.FINISH_LEFT);
        Assertions.assertEquals(PositionVector.Direction.LEFT.vector, sampleGame.getFinishDirectionUnitVector(ARBITRARY_POSITION));
        trackStub.setWishedPositionSpaceType(ARBITRARY_POSITION, Config.SpaceType.FINISH_RIGHT);
        Assertions.assertEquals(PositionVector.Direction.RIGHT.vector, sampleGame.getFinishDirectionUnitVector(ARBITRARY_POSITION));
    }

    @Test
    public void getFinishDirectionUnitVector_PositionIsInvalidFinishType() {
        setUpGameWithDefaultTrackStub();
        trackStub.setWishedPositionSpaceType(ARBITRARY_POSITION, Config.SpaceType.TRACK);
        Assertions.assertThrows(GameException.class, () -> sampleGame.getFinishDirectionUnitVector(ARBITRARY_POSITION));
        trackStub.setWishedPositionSpaceType(ARBITRARY_POSITION, Config.SpaceType.WALL);
        Assertions.assertThrows(GameException.class, () -> sampleGame.getFinishDirectionUnitVector(ARBITRARY_POSITION));
    }

    //isValidDirection()
    @Test
    public void isValidDirection_Valid() {
        setUpGameWithDefaultTrackStub();
        Assertions.assertTrue(sampleGame.isValidDirection(PositionVector.Direction.RIGHT.vector, PositionVector.Direction.UP_RIGHT.vector));
        Assertions.assertTrue(sampleGame.isValidDirection(PositionVector.Direction.RIGHT.vector, PositionVector.Direction.DOWN_RIGHT.vector));
        Assertions.assertTrue(sampleGame.isValidDirection(PositionVector.Direction.UP.vector, PositionVector.Direction.UP_LEFT.vector));
        Assertions.assertTrue(sampleGame.isValidDirection(PositionVector.Direction.UP.vector, PositionVector.Direction.UP_RIGHT.vector));
        Assertions.assertTrue(sampleGame.isValidDirection(PositionVector.Direction.LEFT.vector, PositionVector.Direction.UP_LEFT.vector));
        Assertions.assertTrue(sampleGame.isValidDirection(PositionVector.Direction.LEFT.vector, PositionVector.Direction.DOWN_LEFT.vector));
        Assertions.assertTrue(sampleGame.isValidDirection(PositionVector.Direction.DOWN.vector, PositionVector.Direction.DOWN_RIGHT.vector));
        Assertions.assertTrue(sampleGame.isValidDirection(PositionVector.Direction.DOWN.vector, PositionVector.Direction.DOWN_LEFT.vector));
    }

    @Test
    public void isValidDirection_Invalid() {
        setUpGameWithDefaultTrackStub();
        Assertions.assertFalse(sampleGame.isValidDirection(PositionVector.Direction.RIGHT.vector, PositionVector.Direction.UP.vector));
        Assertions.assertFalse(sampleGame.isValidDirection(PositionVector.Direction.RIGHT.vector, PositionVector.Direction.UP_LEFT.vector));
        Assertions.assertFalse(sampleGame.isValidDirection(PositionVector.Direction.RIGHT.vector, PositionVector.Direction.LEFT.vector));
        Assertions.assertFalse(sampleGame.isValidDirection(PositionVector.Direction.RIGHT.vector, PositionVector.Direction.DOWN_LEFT.vector));
        Assertions.assertFalse(sampleGame.isValidDirection(PositionVector.Direction.RIGHT.vector, PositionVector.Direction.DOWN.vector));
        Assertions.assertFalse(sampleGame.isValidDirection(PositionVector.Direction.LEFT.vector, PositionVector.Direction.UP.vector));
        Assertions.assertFalse(sampleGame.isValidDirection(PositionVector.Direction.LEFT.vector, PositionVector.Direction.UP_RIGHT.vector));
        Assertions.assertFalse(sampleGame.isValidDirection(PositionVector.Direction.LEFT.vector, PositionVector.Direction.RIGHT.vector));
        Assertions.assertFalse(sampleGame.isValidDirection(PositionVector.Direction.LEFT.vector, PositionVector.Direction.DOWN_RIGHT.vector));
        Assertions.assertFalse(sampleGame.isValidDirection(PositionVector.Direction.LEFT.vector, PositionVector.Direction.DOWN.vector));
    }

    @Test
    public void switchToNextActiveCar_EightCarsSequenceAllActive() throws IOException, InvalidTrackFormatException {
        final int NUMBER_TIMES = 5;
        setUpTrackAndGame("testtracks/game-testtracks/switch_cars.txt");
        sampleGame.switchToNextActiveCar();
        int expectedIndex = sampleGame.getCurrentCarIndex();
        for (int i = 0; i < sampleTrack.getCarCount() * NUMBER_TIMES; i++) {
            Assertions.assertEquals(expectedIndex, sampleGame.getCurrentCarIndex());
            sampleGame.switchToNextActiveCar();
            expectedIndex = (expectedIndex + 1) % sampleTrack.getCarCount();
        }
    }

    @Test
    public void switchToNextActiveCar_TwoAndSevenActive() throws IOException, InvalidTrackFormatException {
        final int NUMBER_TIMES = 50;
        List<Integer> activeCars = new ArrayList<>();
        activeCars.add(2);
        activeCars.add(7);
        int listIndex = 1;
        int expectedCarIndex = activeCars.get(listIndex);
        Config.setFirstTurnCarIndex(expectedCarIndex);
        setUpTrackAndGame("testtracks/game-testtracks/switch_cars.txt");
        //crash rest
        for (int i = 0; i < sampleTrack.getCarCount(); i++) {
            if (!activeCars.contains(i)) {
                sampleTrack.crashCar(i, ZERO_POSITION_VECTOR);
            }
        }
        for (int i = 0; i < NUMBER_TIMES; i++) {
            Assertions.assertEquals(expectedCarIndex, sampleGame.getCurrentCarIndex());
            sampleGame.switchToNextActiveCar();
            listIndex = (listIndex + 1) % activeCars.size();
            expectedCarIndex = activeCars.get(listIndex);
        }
    }

    /**
     * <ol>
     *     <li>Load test track.</li>
     *     <li>Crash cars wih even index number.</li>
     *     <li>Validate if switching goes as expected.</li></l>
     * </ol>
     * @throws IOException                 Invalid file path.
     * @throws InvalidTrackFormatException Invalid track format.
     */
    @Test
    public void switchToNextActiveCar_EightCarsSequenceOddsActive() throws IOException, InvalidTrackFormatException {
        final int NUMBER_TIMES = 50;
        setUpTrackAndGame("testtracks/game-testtracks/switch_cars.txt");
        //crash each second car
        for (int i = 0; i < sampleTrack.getCarCount(); i++) {
            if (i % 2 == 0) {
                sampleTrack.crashCar(i, new PositionVector(0, 0));
            }
        }
        int expectedIndex = sampleGame.getCurrentCarIndex();
        if (sampleGame.getCurrentCarIndex() % 2 == 0) {
            expectedIndex = (expectedIndex + 1) % sampleTrack.getCarCount();
            sampleGame.switchToNextActiveCar();
        }
        for (int i = 0; i < NUMBER_TIMES; i++) {
            Assertions.assertEquals(expectedIndex, sampleGame.getCurrentCarIndex());
            expectedIndex = (expectedIndex + 1) % sampleTrack.getCarCount();
            while (expectedIndex % 2 == 0) {
                expectedIndex = (expectedIndex + 1) % sampleTrack.getCarCount();
            }
            sampleGame.switchToNextActiveCar();
        }
    }

    @Test
    public void switchToNextActiveCar_NoActiveCarsLeft() {
        setUpGameWithDefaultTrackStub();
        trackStub.setWishedActiveCarNumber(0);
        Assertions.assertThrows(GameException.class, () -> sampleGame.switchToNextActiveCar());
    }

    // calculatePath()

    /**
     * The list (0,0) is expected.
     */
    @Test
    public void calculatePath_SamePoint() {
        setUpGameWithDefaultTrackStub();
        final PositionVector START_POINT = ZERO_POSITION_VECTOR;

        List<PositionVector> expectedPath = new ArrayList<>();
        expectedPath.add(ZERO_POSITION_VECTOR);
        Assertions.assertEquals(expectedPath, sampleGame.calculatePath(START_POINT, START_POINT));
    }

    /**
     * The list (0,0),(0,1),(0,2) is expected.
     */
    @Test
    public void calculatePath_xLine() {
        setUpGameWithDefaultTrackStub();
        //build line
        List<PositionVector> expectedPath = new ArrayList<>();
        final PositionVector START_POINT = ZERO_POSITION_VECTOR;
        expectedPath.add(START_POINT);
        expectedPath.add(new PositionVector(1, 0));
        final PositionVector END_POINT = new PositionVector(2, 0);
        expectedPath.add(END_POINT);
        Assertions.assertEquals(expectedPath, sampleGame.calculatePath(START_POINT, END_POINT));
    }

    /**
     * The list (2,0),(1,0),(0,0) is expected.
     */
    @Test
    public void calculatePath_xReversedLine() {
        setUpGameWithDefaultTrackStub();
        //build line
        List<PositionVector> expectedPath = new ArrayList<>();
        final PositionVector START_POINT = new PositionVector(2, 0);
        expectedPath.add(START_POINT);
        expectedPath.add(new PositionVector(1, 0));
        final PositionVector END_POINT = ZERO_POSITION_VECTOR;
        expectedPath.add(END_POINT);
        Assertions.assertEquals(expectedPath, sampleGame.calculatePath(START_POINT, END_POINT));
    }

    /**
     * The list (0,0),(0,1),(0,2) is expected.
     */
    @Test
    public void calculatePath_YLine() {
        setUpGameWithDefaultTrackStub();
        //build the line
        List<PositionVector> expectedPath = new ArrayList<>();
        final PositionVector START_POINT = ZERO_POSITION_VECTOR;
        expectedPath.add(START_POINT);
        expectedPath.add(new PositionVector(0, 1));
        final PositionVector END_POINT = new PositionVector(0, 2);
        expectedPath.add(END_POINT);
        Assertions.assertEquals(expectedPath, sampleGame.calculatePath(START_POINT, END_POINT));
    }

    /**
     * The list (0,2),(0,1),(0,0) is expected.
     */
    @Test
    public void calculatePath_YReversedLine() {
        setUpGameWithDefaultTrackStub();
        //build the line
        List<PositionVector> expectedPath = new ArrayList<>();
        final PositionVector START_POINT = new PositionVector(0, 2);
        expectedPath.add(START_POINT);
        expectedPath.add(new PositionVector(0, 1));
        final PositionVector END_POINT = ZERO_POSITION_VECTOR;
        expectedPath.add(END_POINT);
        Assertions.assertEquals(expectedPath, sampleGame.calculatePath(START_POINT, END_POINT));
    }

    /**
     * The list (0,0),(1,1),(2,2),(3,3) is expected.
     */
    @Test
    public void calculatePath_DiagonalLine() {
        setUpGameWithDefaultTrackStub();
        List<PositionVector> expectedPath = new ArrayList<>();
        final PositionVector START_POINT = ZERO_POSITION_VECTOR;
        expectedPath.add(START_POINT);
        expectedPath.add(new PositionVector(1, 1));
        expectedPath.add(new PositionVector(2, 2));
        final PositionVector END_POINT = new PositionVector(3, 3);
        expectedPath.add(END_POINT);
        Assertions.assertEquals(expectedPath, sampleGame.calculatePath(START_POINT, END_POINT));
    }

    /**
     * The list (3,3),(2,2),(1,1),(0,0) is expected.
     */
    @Test
    public void calculatePath_DiagonalLineReversed() {
        setUpGameWithDefaultTrackStub();
        List<PositionVector> expectedPath = new ArrayList<>();
        final PositionVector START_POINT = new PositionVector(3, 3);
        expectedPath.add(START_POINT);
        expectedPath.add(new PositionVector(2, 2));
        expectedPath.add(new PositionVector(1, 1));
        final PositionVector END_POINT = ZERO_POSITION_VECTOR;
        expectedPath.add(END_POINT);
        Assertions.assertEquals(expectedPath, sampleGame.calculatePath(START_POINT, END_POINT));
    }

    /**
     * The list (0,0),(1,0),(2,1),(3,1) is expected.
     */
    @Test
    public void calculatePath_xyLine() {
        setUpGameWithDefaultTrackStub();
        List<PositionVector> expectedPath = new ArrayList<>();
        final PositionVector START_POINT = ZERO_POSITION_VECTOR;
        expectedPath.add(START_POINT);
        expectedPath.add(new PositionVector(1, 0));
        expectedPath.add(new PositionVector(2, 1));
        final PositionVector END_POINT = new PositionVector(3, 1);
        expectedPath.add(END_POINT);
        Assertions.assertEquals(expectedPath, sampleGame.calculatePath(START_POINT, END_POINT));
    }
    //do carTurn()

    /**
     * Set up doCarTurn():
     * <ol>
     *     <li>Generate trackStub with "numberCars" cars.</li>
     *     <li>Generate sampleGame</li>
     *     <li>Set up trackStub.isCarCrashed(int) response for each car index to "false".</li></l>
     * </ol>
     *
     * @param numberCars wished return value for trackStub.getCarCount();
     */
    private void setUpDoCarTurn(int numberCars) {
        setUpGameWithDefaultTrackStub();
        trackStub.setWishedCarCount(numberCars);
        for (int i = 0; i < numberCars; i++) {
            trackStub.setWishedIsCarCrashed(i, false);
        }
    }

    /**
     * Set up wished behavior of trackStub with respect to car position.
     *
     * @param carCurrentPosition wished return value for trackStub.getCarPosition(int)
     * @param carNextPosition    wished return value for trackStub.getCarNextPosition(int)
     */
    private void setUpTrackStubCarPositionResponse(PositionVector carCurrentPosition, PositionVector carNextPosition) {
        trackStub.setWishedCarPosition(carCurrentPosition);
        trackStub.setWishedNextCarPosition(carNextPosition);
    }

    /**
     * Check if an expected car was accelerated how it was intended.
     */
    @Test
    public void doCarTurn_AccelerateCar() {
        final int NUMBER_CARS = Config.MIN_CARS;
        setUpDoCarTurn(NUMBER_CARS);

        setUpTrackStubCarPositionResponse(ZERO_POSITION_VECTOR, ZERO_POSITION_VECTOR);

        sampleGame.doCarTurn(PositionVector.Direction.UP);
        Assertions.assertEquals(PositionVector.Direction.UP, trackStub.getGivenAcceleration());
        Assertions.assertEquals(Config.firstTurnCarIndex, trackStub.getGivenCarIndex());
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
        setUpDoCarTurn(NUMBER_CARS);

        final int WINNER_CAR_INDEX = random.nextInt(NUMBER_CARS);

        setUpTrackStubCarPositionResponse(ZERO_POSITION_VECTOR, ARBITRARY_POSITION);

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
        setUpDoCarTurn(NUMBER_CARS);

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

        setUpTrackStubCarPositionResponse(ZERO_POSITION_VECTOR, ARBITRARY_POSITION);

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
        setUpDoCarTurn(NUMBER_CARS);

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
        setUpTrackStubCarPositionResponse(CAR_START_POSITION, CAR_END_POSITION);
        sampleGame.doCarTurn(PositionVector.Direction.RIGHT);
        Assertions.assertEquals(Game.NO_WINNER, sampleGame.getWinner());

        //skip second car
        sampleGame.switchToNextActiveCar();

        //fist car: cross fl in right direction
        trackStub.setWishedCarVelocity(PositionVector.subtract(testPath.get(0), testPath.get(2)));
        setUpTrackStubCarPositionResponse(CAR_END_POSITION, CAR_START_POSITION);
        sampleGame.doCarTurn(PositionVector.Direction.LEFT);
        Assertions.assertEquals(Game.NO_WINNER, sampleGame.getWinner());
    }

    /**
     * Do set up as follow:
     * <ol>
     *     <li>Generate sampleTrack from the given file path.</li>
     *     <li>Generate sampleGame with sampleTrack as parameter.</li>
     * </ol>
     *
     * @param filePath path to the test track.
     * @throws IOException                 Invalid file path.
     * @throws InvalidTrackFormatException Invalid track format.
     */
    private void setUpTrackAndGame(String filePath) throws IOException, InvalidTrackFormatException {
        sampleTrack = new Track(new File(filePath));
        sampleGame = new Game(sampleTrack);
    }

    /**
     * Track with test track set up will be loaded, which expects,if cars are moved to the right and car "b" is not moved for one turn:
     * <ol>
     *     <li>Car "a" will crash into car "b".</li>
     * </ol>
     * After the crash car "a" stays crashed und don't move until there is a winner.
     */
    @Test
    public void doCarTurn_CrashedCarStaysCrashed() throws IOException, InvalidTrackFormatException {
        setUpTrackAndGame("testtracks/game-testtracks/stay_crashed.txt");
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
     * Track with test track set up will be loaded, which expects,if cars are moved to the right and car "b" is not moved for one turn:
     * <ol>
     *     <li>Car "a" will crash into car "b".</li>
     * </ol>
     * The test checks if car "b" can still move.
     */
    @Test
    public void doCarTurn_CrashVictimIsAbleToMove() throws IOException, InvalidTrackFormatException {
        setUpTrackAndGame("testtracks/game-testtracks/stay_crashed.txt");

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
     * Track with test track set up will be loaded, which expects,if cars are moved to the right and car "c" is not moved for one turn:
     * <ol>
     *     <li>Cars "a" will finish the line first.</li>
     *     <li>Cars "d","e","f","g" will crash at the location where car "c" stood.</li>
     * </ol>
     * Cars "a" is able to cross the finish line, when cars "d" to "g" are crashed at one point.
     */
    @Test
    public void doCarTurn_MultipleCarsCrashAtOneLocationCarAisTheWinner() throws IOException, InvalidTrackFormatException {
        setUpTrackAndGame("testtracks/game-testtracks/crash_line.txt");
        //move a,b
        sampleGame.doCarTurn(PositionVector.Direction.RIGHT);
        sampleGame.doCarTurn(PositionVector.Direction.RIGHT);
        //c halt
        sampleGame.doCarTurn(PositionVector.Direction.NONE);
        //crash d,e,f,g and move a,b,c to finish direction.
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
     * Track with test track set up will be loaded, which expects,if cars are moved to the right and car "b" is not moved:
     * <ol>
     *     <li>car a - crash with car be.</li>
     *     <li>car c- crash with wall.</li>
     *     <li>car d- will cross the finish line.</li>
     * </ol>
     */
    @Test
    public void doCarTurn_CollisionWithObstacles() throws IOException, InvalidTrackFormatException {
        setUpTrackAndGame("testtracks/game-testtracks/crashes_into_obstacles.txt");
        //crash a with car b
        sampleGame.doCarTurn(PositionVector.Direction.RIGHT);
        Assertions.assertTrue(sampleTrack.isCarCrashed(0));
        //car b stand still
        sampleGame.doCarTurn(PositionVector.Direction.NONE);
        //crash c with wall
        sampleGame.doCarTurn(PositionVector.Direction.RIGHT);
        Assertions.assertTrue(sampleTrack.isCarCrashed(2));

        sampleGame.doCarTurn(PositionVector.Direction.RIGHT);
        Assertions.assertEquals(3, sampleGame.getWinner());
    }
    //isOneCarRemaining

    /**
     * Set up trackStub and sampleGame.
     *
     * @param numberActiveCars wished return value for trackStub.getNumberActiveCarsRemaining()
     */
    private void setUpIsOneCarRemaining(int numberActiveCars) {
        trackStub = new TrackStub();
        sampleGame = new Game(trackStub);
        trackStub.setWishedActiveCarNumber(numberActiveCars);
    }

    /**
     * The test does following:
     * <ol>
     *     <li>TrackStub is generated.</li>
     *     <li>TrackStub set up: number of active cars is set to be one.</li>
     * </ol>
     * True is expected.
     */
    @Test
    public void isOneCarRemaining_OneCar_True() {
        final int NUMBER_ACTIVE_CARS = 1;
        setUpIsOneCarRemaining(NUMBER_ACTIVE_CARS);
        Assertions.assertTrue(sampleGame.isOneCarRemaining());
    }

    /**
     * The test does following:
     * <ol>
     *     <li>TrackStub is generated.</li>
     *     <li>TrackStub set up: number of active cars is set to be two.</li>
     * </ol>
     * False is expected.
     */
    @Test
    public void isOneCarRemaining_TwoCars_False() {
        final int NUMBER_ACTIVE_CARS = 2;
        setUpIsOneCarRemaining(NUMBER_ACTIVE_CARS);
        Assertions.assertFalse(sampleGame.isOneCarRemaining());
    }

    /**
     * The test does following:
     * <ol>
     *     <li>TrackStub is generated.</li>
     *     <li>TrackStub set up: number of active cars is set to be -1.</li>
     * </ol>
     * False is expected.
     */
    @Test
    public void isOneCarRemaining_ImpossibleNumberOfCars_False() {
        final int NUMBER_ACTIVE_CARS = -1;
        setUpIsOneCarRemaining(NUMBER_ACTIVE_CARS);
        Assertions.assertFalse(sampleGame.isOneCarRemaining());
    }

    //willCarCrash()

    /**
     * Set up trackStub and sampleGame.
     *
     * @param isTrackBound       wished return value for trackStub.isTrackBound(PositionVector)
     * @param isSomeOtherCarHere wished return value for trackStub.isSomeOtherCarHere(PositionVector)
     */
    private void setUpWillCarCrash(boolean isTrackBound, boolean isSomeOtherCarHere) {
        setUpGameWithDefaultTrackStub();
        setUpWillCarCrashTrackStubResponse(isTrackBound, isSomeOtherCarHere);
    }

    /**
     * @param isTrackBound       wished return value for trackStub.isTrackBound(PositionVector)
     * @param isSomeOtherCarHere wished return value for trackStub.isSomeOtherCarHere(PositionVector)
     */
    private void setUpWillCarCrashTrackStubResponse(boolean isTrackBound, boolean isSomeOtherCarHere) {
        trackStub.setWishedIsTrackBound(isTrackBound);
        trackStub.setWishedIsSomeOtherCarHere(isSomeOtherCarHere);
    }

    @Test
    public void willCarCrash_OnlyWallHere_True() {
        setUpWillCarCrash(true, false);
        final int ARBITRARY_CAR_INDEX = 0;
        Assertions.assertTrue(sampleGame.willCarCrash(ARBITRARY_CAR_INDEX, ARBITRARY_POSITION));
    }

    @Test
    public void willCarCrash_OnlySomeOtherCarHere_True() {
        setUpWillCarCrash(false, true);
        final int ARBITRARY_CAR_INDEX = 0;
        Assertions.assertTrue(sampleGame.willCarCrash(ARBITRARY_CAR_INDEX, ARBITRARY_POSITION));
    }

    @Test
    public void willCarCrash_SomeOtherCarAndWallHere_True() {
        setUpWillCarCrash(true, true);
        final int ARBITRARY_CAR_INDEX = 0;
        Assertions.assertTrue(sampleGame.willCarCrash(ARBITRARY_CAR_INDEX, ARBITRARY_POSITION));
    }

    @Test
    public void willCarCrash_NoCarNoWallHere_False() {
        setUpWillCarCrash(false, false);
        final int ARBITRARY_CAR_INDEX = 0;
        Assertions.assertFalse(sampleGame.willCarCrash(ARBITRARY_CAR_INDEX, ARBITRARY_POSITION));
    }

    // getCarId(), getCarVelocity(), getCarPosition(),
    @Test
    public void carGetters_IndexPassedCorrectly() {
        setUpGameWithDefaultTrackStub();
        final int ARBITRARY_INDEX_1 = 331;
        sampleGame.getCarId(ARBITRARY_INDEX_1);
        Assertions.assertEquals(ARBITRARY_INDEX_1, trackStub.getGivenCarIndex());
        final int ARBITRARY_INDEX_2 = 332;
        sampleGame.getCarPosition(ARBITRARY_INDEX_2);
        Assertions.assertEquals(ARBITRARY_INDEX_2, trackStub.getGivenCarIndex());
        final int ARBITRARY_INDEX_3 = 333;
        sampleGame.getCarVelocity(ARBITRARY_INDEX_3);
        Assertions.assertEquals(ARBITRARY_INDEX_3, trackStub.getGivenCarIndex());

    }

    //getCurrentCarIndex()

    /**
     * CurrentCarIndex should be initialized to Game.FIRST_TURN_CAR_INDEX.
     */
    @Test
    public void getCurrentCarIndex_CorrectlyInitialized() {
        setUpGameWithDefaultTrackStub();
        Assertions.assertEquals(Config.firstTurnCarIndex, sampleGame.getCurrentCarIndex());
    }

    //adjustPenaltyPointsForActiveCar()
    @Test
    public void adjustPenaltyPointsForActiveCar_NotOnFinishLine() {
        setUpGameWithDefaultTrackStub();
        trackStub.setWishedPositionSpaceType(ARBITRARY_POSITION, Config.SpaceType.TRACK);
        Assertions.assertThrows(GameException.class, () -> sampleGame.adjustPenaltyPointsForActiveCar(ARBITRARY_POSITION));
    }

    /**
     * The test was set up as follow:
     * <ol>
     *     <li>Number of laps will be set to two, in order to be able continue the test if a car arrives on the finish line.</li>
     *     <li>Car "a" will be moved to the right, it arrives on the finish line: ">". Therefore should become +1 point.</li>
     *     <li>Car "b" will be skipped(Is there because of Config.MIN_CARS).</li>
     *     <li>Car "a" will be moved to the right, it leaves the finish line: ">". Therefore should become +1 point.</li>
     * </ol>
     *
     * @throws IOException                 Invalid file path.
     * @throws InvalidTrackFormatException Invalid track format.
     */
    @Test
    public void adjustPenaltyPointsForActiveCar_ProperDirectionMovementPointsAddition() throws IOException, InvalidTrackFormatException {
        Config.setNumberLaps(2);
        setUpTrackAndGame("testtracks/game-testtracks/penalty_points.txt");
        //car "a" goes on finish line
        int indexCarA = sampleGame.getCurrentCarIndex();
        sampleGame.doCarTurn(PositionVector.Direction.RIGHT);
        Assertions.assertEquals(sampleGame.getInitialNumberPenaltyPoints() + 1, sampleGame.getNumberPenaltyPoints(indexCarA));
        //car "b" is standing still
        sampleGame.doCarTurn(PositionVector.Direction.NONE);
        //car "a" leaves the finish line
        sampleGame.doCarTurn(PositionVector.Direction.NONE);
        Assertions.assertEquals(sampleGame.getInitialNumberPenaltyPoints() + 2, sampleGame.getNumberPenaltyPoints(indexCarA));
    }

    /**
     * The test was set up as follow:
     * <ol>
     *     <li>Car "a" will be skipped(Is there because of Config.MIN_CARS).</li>
     *     <li>Car "b" will be moved to the right, it arrives on the finish line: "<". Therefore should become -1 point.</li>
     *     <li>Car "a" will be skipped.</li>
     *     <li>Car "b" will be moved to the right, it leaves the finish line: ">". Therefore should become -1 point.</li>
     * </ol>
     *
     * @throws IOException                 Invalid file path.
     * @throws InvalidTrackFormatException Invalid track format.
     */
    @Test
    public void adjustPenaltyPointsForActiveCar_WrongDirectionMovementPointsSubtraction() throws IOException, InvalidTrackFormatException {
        setUpTrackAndGame("testtracks/game-testtracks/penalty_points.txt");
        //car "a" is standing still
        sampleGame.doCarTurn(PositionVector.Direction.NONE);
        //car "b" goes on finish line
        int indexCarB = sampleGame.getCurrentCarIndex();
        sampleGame.doCarTurn(PositionVector.Direction.RIGHT);
        Assertions.assertEquals(sampleGame.getInitialNumberPenaltyPoints() - 1, sampleGame.getNumberPenaltyPoints(indexCarB));
        //car "a" is standing still
        sampleGame.doCarTurn(PositionVector.Direction.NONE);
        //car "b" leaves the finish line
        sampleGame.doCarTurn(PositionVector.Direction.NONE);
        Assertions.assertEquals(sampleGame.getInitialNumberPenaltyPoints() - 2, sampleGame.getNumberPenaltyPoints(indexCarB));
    }
}
