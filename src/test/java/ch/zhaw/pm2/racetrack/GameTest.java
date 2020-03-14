package ch.zhaw.pm2.racetrack;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class GameTest {

    @Test
    public void getFinishDirectionUnitVector_FinishUp() {
        PositionVector ARBITRARY_FINISH_POSITION = new PositionVector(1, 1);
        Track mockedTrack = mock(Track.class);
        Game testedGame = new Game(mockedTrack);
        when(mockedTrack.getSpaceType(ARBITRARY_FINISH_POSITION)).thenReturn(Config.SpaceType.FINISH_UP);
        Assertions.assertEquals(PositionVector.Direction.UP.vector, testedGame.getFinishDirectionUnitVector(ARBITRARY_FINISH_POSITION));
    }

    @Test
    public void getFinishDirectionUnitVector_FinishDown() {
        PositionVector ARBITRARY_FINISH_POSITION = new PositionVector(1, 1);
        Track mockedTrack = mock(Track.class);
        Game testedGame = new Game(mockedTrack);
        when(mockedTrack.getSpaceType(ARBITRARY_FINISH_POSITION)).thenReturn(Config.SpaceType.FINISH_DOWN);
        Assertions.assertEquals(PositionVector.Direction.DOWN.vector, testedGame.getFinishDirectionUnitVector(ARBITRARY_FINISH_POSITION));
    }

    @Test
    public void getFinishDirectionUnitVector_FinishLeft() {
        PositionVector ARBITRARY_FINISH_POSITION = new PositionVector(1, 1);
        Track mockedTrack = mock(Track.class);
        Game testedGame = new Game(mockedTrack);
        when(mockedTrack.getSpaceType(ARBITRARY_FINISH_POSITION)).thenReturn(Config.SpaceType.FINISH_LEFT);
        Assertions.assertEquals(PositionVector.Direction.LEFT.vector, testedGame.getFinishDirectionUnitVector(ARBITRARY_FINISH_POSITION));
    }

    @Test
    public void getFinishDirectionUnitVector_FinishRight() {
        PositionVector ARBITRARY_FINISH_POSITION = new PositionVector(1, 1);
        Track mockedTrack = mock(Track.class);
        Game testedGame = new Game(mockedTrack);
        when(mockedTrack.getSpaceType(ARBITRARY_FINISH_POSITION)).thenReturn(Config.SpaceType.FINISH_RIGHT);
        Assertions.assertEquals(PositionVector.Direction.RIGHT.vector, testedGame.getFinishDirectionUnitVector(ARBITRARY_FINISH_POSITION));
    }

    @Test
    public void getFinishDirectionUnitVector_PositionIsTrack() {
        PositionVector ARBITRARY_FINISH_POSITION = new PositionVector(1, 1);
        Track mockedTrack = mock(Track.class);
        Game testedGame = new Game(mockedTrack);
        when(mockedTrack.getSpaceType(ARBITRARY_FINISH_POSITION)).thenReturn(Config.SpaceType.TRACK);
        Assertions.assertEquals(new PositionVector(0, 0), testedGame.getFinishDirectionUnitVector(ARBITRARY_FINISH_POSITION));
    }

    @Test
    public void getFinishDirectionUnitVector_PositionIsWall() {
        PositionVector ARBITRARY_FINISH_POSITION = new PositionVector(1, 1);
        Track mockedTrack = mock(Track.class);
        Game testedGame = new Game(mockedTrack);
        when(mockedTrack.getSpaceType(ARBITRARY_FINISH_POSITION)).thenReturn(Config.SpaceType.WALL);
        Assertions.assertEquals(new PositionVector(0, 0), testedGame.getFinishDirectionUnitVector(ARBITRARY_FINISH_POSITION));
    }

    @Test
    public void getFinishDirectionUnitVector_NoSuchPosition() {
        PositionVector ARBITRARY_INVALID_FINISH_POSITION = new PositionVector(Integer.MIN_VALUE, Integer.MIN_VALUE);
        Track mockedTrack = mock(Track.class);
        Game testedGame = new Game(mockedTrack);
        Assertions.assertEquals(new PositionVector(0, 0), testedGame.getFinishDirectionUnitVector(ARBITRARY_INVALID_FINISH_POSITION));
    }

    //todo initial cat is zero?
    @Test
    public void switchToNextActiveCar_FIVE_CARS() {
        final int NUMBER_ACTIVE_CARS = 5;
        final int NUMBER_LOOPS = 3;
        PositionVector ARBITRARY_CAR_POSITION = new PositionVector(Integer.MIN_VALUE, Integer.MIN_VALUE);

        Track mockedTrack = mock(Track.class);
        when(mockedTrack.getCarCount()).thenReturn(NUMBER_ACTIVE_CARS);

        List<Car> cars = new ArrayList<>();
        for (int i = 0; i < NUMBER_ACTIVE_CARS; i++) {
            cars.add(new Car(ARBITRARY_CAR_POSITION, (char) ('a' + i)));
        }

        for (int i = 0; i < cars.size(); i++) {
            when(mockedTrack.getCar(i)).thenReturn(cars.get(i));
        }

        Game testedGame = new Game(mockedTrack);
        for (int i = 0; i < cars.size() * NUMBER_LOOPS; i++) {
            int expectedIndex = i % cars.size();
            Assertions.assertEquals(expectedIndex, testedGame.getCurrentCarIndex());
            testedGame.switchToNextActiveCar();
        }
    }

    @Test
    public void switchToNextActiveCar_TenCars_OnlyOddActive() {
        final int NUMBER_CARS = 10;
        final int NUMBER_LOOPS = 3;
        PositionVector ARBITRARY_CAR_POSITION = new PositionVector(Integer.MIN_VALUE, Integer.MIN_VALUE);

        Track mockedTrack = mock(Track.class);
        when(mockedTrack.getCarCount()).thenReturn(NUMBER_CARS);

        List<Car> cars = new ArrayList<>();
        for (int i = 0; i < NUMBER_CARS; i++) {
            cars.add(new Car(ARBITRARY_CAR_POSITION, (char) ('a' + i)));
        }

        for (int i = 0; i < cars.size(); i++) {
            when(mockedTrack.getCar(i)).thenReturn(cars.get(i));
        }

        for (int i = 0; i < NUMBER_CARS; i++) {
            if (i % 2 == 0) {
                final boolean THIS_CAR_IS_CRASHED = true;
                when(mockedTrack.isCarCrashed(i)).thenReturn(THIS_CAR_IS_CRASHED);
            }
        }

        Game testedGame = new Game(mockedTrack);
        for (int i = 0; i < cars.size() * NUMBER_LOOPS; i++) {
            int expectedIndex = i % cars.size();
            Assertions.assertEquals(expectedIndex, testedGame.getCurrentCarIndex());
            testedGame.switchToNextActiveCar();
        }
    }

    @Test
    public void switchToNextActiveCar_TwentyCars_RandomActive() {
        //todo
        final int NUMBER_CARS = 20;
        PositionVector ARBITRARY_CAR_POSITION = new PositionVector(Integer.MIN_VALUE, Integer.MIN_VALUE);

        Track mockedTrack = mock(Track.class);
        when(mockedTrack.getCarCount()).thenReturn(NUMBER_CARS);

        List<Car> cars = new ArrayList<>();
        for (int i = 0; i < NUMBER_CARS; i++) {
            cars.add(new Car(ARBITRARY_CAR_POSITION, (char) ('a' + i)));
        }

        for (int i = 0; i < cars.size(); i++) {
            when(mockedTrack.getCar(i)).thenReturn(cars.get(i));
        }

        for (int i = 0; i < NUMBER_CARS; i++) {
            //if (i % 2 == 0) {
            final boolean THIS_CAR_IS_CRASHED = true;
            when(mockedTrack.isCarCrashed(i)).thenReturn(THIS_CAR_IS_CRASHED);
            // }
        }

        Game testedGame = new Game(mockedTrack);
        for (int i = 0; i < cars.size(); i++) {
            Assertions.assertEquals(i, testedGame.getCurrentCarIndex());
            testedGame.switchToNextActiveCar();
        }
    }

    //todo no active cars
    @Test
    public void switchToNextActiveCar_TwentyCars_NoActiveCars() {
        //todo
        final int NUMBER_CARS = 20;
        PositionVector ARBITRARY_CAR_POSITION = new PositionVector(Integer.MIN_VALUE, Integer.MIN_VALUE);

        Track mockedTrack = mock(Track.class);
        when(mockedTrack.getCarCount()).thenReturn(NUMBER_CARS);

        List<Car> cars = new ArrayList<>();
        for (int i = 0; i < NUMBER_CARS; i++) {
            cars.add(new Car(ARBITRARY_CAR_POSITION, (char) ('a' + i)));
        }

        for (int i = 0; i < cars.size(); i++) {
            when(mockedTrack.getCar(i)).thenReturn(cars.get(i));
        }

        for (int i = 0; i < NUMBER_CARS; i++) {
            //if (i % 2 == 0) {
            final boolean THIS_CAR_IS_CRASHED = true;
            when(mockedTrack.isCarCrashed(i)).thenReturn(THIS_CAR_IS_CRASHED);
            // }
        }
        final int NO_ACTIVE_CARS = -1;
        Game testedGame = new Game(mockedTrack);
        for (int i = 0; i < cars.size(); i++) {
            testedGame.switchToNextActiveCar();
            Assertions.assertEquals(NO_ACTIVE_CARS, testedGame.getCurrentCarIndex());
        }
    }
}
