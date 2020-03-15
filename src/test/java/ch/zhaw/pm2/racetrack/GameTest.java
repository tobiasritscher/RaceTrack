package ch.zhaw.pm2.racetrack;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class GameTest {

    public static final boolean THIS_CAR_IS_CRASHED = true;
    public static final boolean THIS_CAR_IS_NOT_CRASHED = false;
    public static final PositionVector ARBITRARY_VALID_FINISH_POSITION = new PositionVector(Integer.MIN_VALUE, Integer.MIN_VALUE);
    public static final PositionVector ARBITRARY_INVALID_FINISH_POSITION = new PositionVector(Integer.MIN_VALUE, Integer.MIN_VALUE);
    public static final PositionVector ZERO_POSITION_VECTOR = new PositionVector(0, 0);
    public static final PositionVector ARBITRARY_VALID_CAR_POSITION = new PositionVector(Integer.MIN_VALUE, Integer.MIN_VALUE);

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
}
