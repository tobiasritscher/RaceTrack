package ch.zhaw.pm2.racetrack;

import ch.zhaw.pm2.racetrack.exceptions.InvalidTrackFormatException;
import ch.zhaw.pm2.racetrack.strategy.MoveListStrategy;
import ch.zhaw.pm2.racetrack.strategy.MoveStrategy;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;

public class TrackTest {
    private Track testTrack;

    public Track initializeTrack(String trackSource) throws IOException, InvalidTrackFormatException {
        Track testTrack = new Track(new File(trackSource));
        return testTrack;
    }

    @Test
    public void getSpaceTypeTest() throws IOException, InvalidTrackFormatException {
        testTrack = initializeTrack("testtracks/track_fill_test.txt");

        Assertions.assertEquals(Config.SpaceType.WALL, testTrack.getSpaceType(new PositionVector(0,0)));
        Assertions.assertEquals(Config.SpaceType.FINISH_UP, testTrack.getSpaceType(new PositionVector(1,0)));
        Assertions.assertEquals(Config.SpaceType.FINISH_LEFT, testTrack.getSpaceType(new PositionVector(2,0)));
        Assertions.assertEquals(Config.SpaceType.FINISH_DOWN, testTrack.getSpaceType(new PositionVector(3,0)));
        Assertions.assertEquals(Config.SpaceType.WALL, testTrack.getSpaceType(new PositionVector(4,0)));

        Assertions.assertEquals(Config.SpaceType.WALL, testTrack.getSpaceType(new PositionVector(0,1)));
        Assertions.assertEquals(Config.SpaceType.ANY_CAR, testTrack.getSpaceType(new PositionVector(1,1)));
        Assertions.assertEquals(Config.SpaceType.FINISH_RIGHT, testTrack.getSpaceType(new PositionVector(2,1)));
        Assertions.assertEquals(Config.SpaceType.ANY_CAR, testTrack.getSpaceType(new PositionVector(3,1)));
        Assertions.assertEquals(Config.SpaceType.WALL, testTrack.getSpaceType(new PositionVector(4,1)));
    }

    @Test
    public void getCarTest() throws IOException, InvalidTrackFormatException {
        testTrack = initializeTrack("testtracks/track_fill_test.txt");
        Assertions.assertEquals('a', testTrack.getCar(0).getId());
    }

    @Test
    public void someCarIsHereTest() throws IOException, InvalidTrackFormatException {
        testTrack = initializeTrack("testtracks/track_fill_test.txt");
        PositionVector testPositionPositive = new PositionVector(1,1);
        PositionVector testPositionNegative = new PositionVector(0,0);
        //todo fix
        Assertions.assertTrue(testTrack.isSomeOtherCarHere(0,testPositionPositive));
        Assertions.assertFalse(testTrack.isSomeOtherCarHere(0,testPositionNegative));
    }

    @Test
    public void accelerateCarTest() throws IOException, InvalidTrackFormatException {
        PositionVector testPositionCar1 = new PositionVector(55, 5);
        PositionVector testPositionCar2 = new PositionVector(55, 3);

        testTrack = initializeTrack("tracks/quarter-mile.txt");

        MoveStrategy testStrategy = new MoveListStrategy();
        testTrack.getCar(0).setCarMoveStrategy(testStrategy);
        testTrack.accelerateCar(0,PositionVector.Direction.LEFT);
        testTrack.getCar(0).move();

        testTrack.getCar(1).setCarMoveStrategy(testStrategy);
        testTrack.accelerateCar(1,PositionVector.Direction.LEFT);
        testTrack.getCar(1).move();

        Assertions.assertEquals(testPositionCar1, testTrack.getCar(0).getCarPosition());
        Assertions.assertEquals(testPositionCar2, testTrack.getCar(1).getCarPosition());
    }

    @Test
    public void getCarNextPositionTest(){
        // TODO implement
    }

    @Test
    public void checkCarIndexTest() throws IOException, InvalidTrackFormatException {
        testTrack = initializeTrack("testtracks/track_fill_test.txt");
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            testTrack.checkCarIndex(2);
        });
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            testTrack.checkCarIndex(-1);
        });
    }

    @Test
    public void checkPositionTest() throws IOException, InvalidTrackFormatException {
        testTrack = initializeTrack("testtracks/track_fill_test.txt");
        PositionVector validPosition = new PositionVector(0,0);
        PositionVector invalidXPosition = new PositionVector(0,2);
        PositionVector invalidYPosition = new PositionVector(6,0);
        testTrack.checkPosition(validPosition);
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            testTrack.checkPosition(invalidXPosition);
        });
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            testTrack.checkPosition(invalidYPosition);
        });

    }
}
