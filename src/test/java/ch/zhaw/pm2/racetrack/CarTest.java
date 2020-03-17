package ch.zhaw.pm2.racetrack;

import ch.zhaw.pm2.racetrack.exceptions.InvalidTrackFormatException;
import ch.zhaw.pm2.racetrack.strategy.MoveListeStrategy;
import ch.zhaw.pm2.racetrack.strategy.MoveStrategy;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;


public class CarTest {
    private Track testTrack;

    public Track initializeTrack(String trackSource) throws IOException, InvalidTrackFormatException {
        Track testTrack = new Track(new File(trackSource));
        return testTrack;
    }

    @Test
    public void moveAndAccelerateTest() throws IOException, InvalidTrackFormatException {
        PositionVector testPositionCar1 = new PositionVector(55, 5);
        PositionVector testPositionCar2 = new PositionVector(55, 3);

        testTrack = initializeTrack("tracks/quarter-mile.txt");

        MoveStrategy testStrategy = new MoveListeStrategy();
        testTrack.getCar(0).setCarMoveStrategy(testStrategy);
        testTrack.getCar(0).accelerate(PositionVector.Direction.LEFT);
        testTrack.getCar(0).move();

        testTrack.getCar(1).setCarMoveStrategy(testStrategy);
        testTrack.getCar(1).accelerate(PositionVector.Direction.LEFT);
        testTrack.getCar(1).move();

        Assertions.assertEquals(testPositionCar1, testTrack.getCar(0).getCarPosition());
        Assertions.assertEquals(testPositionCar2, testTrack.getCar(1).getCarPosition());
    }

    @Test
    public void crashTest() throws IOException, InvalidTrackFormatException {
        // TODO implement
    }
}