package ch.zhaw.pm2.racetrack;

import ch.zhaw.pm2.racetrack.exceptions.InvalidTrackFormatException;
import ch.zhaw.pm2.racetrack.strategy.MoveListStrategy;
import ch.zhaw.pm2.racetrack.strategy.MoveStrategy;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;


public class CarTest {
    private Track testTrack;
    private MoveStrategy testStrategy = new MoveListStrategy(new File("moveLists/quarter_mile_moves.txt"));

    public CarTest() throws IOException {
    }

    public Track initializeTrack(String trackSource) throws IOException, InvalidTrackFormatException {
        return new Track(new File(trackSource));
    }

    @Test
    public void moveAndAccelerateTest() throws IOException, InvalidTrackFormatException {
        PositionVector testPositionCar1 = new PositionVector(55, 5);
        PositionVector testPositionCar2 = new PositionVector(55, 3);

        testTrack = initializeTrack("tracks/quarter-mile.txt");

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
    public void crashTest() {
        PositionVector crashLocation = new PositionVector(53, 3);
        PositionVector startLocationTestCarCrash = new PositionVector(0, 0);
        PositionVector startLocationTestCarNoCrash = new PositionVector(0, 1);

        Car testCarCrash = new Car(startLocationTestCarCrash, 'a');
        Car testCarNoCrash = new Car(startLocationTestCarNoCrash, 'b');

        testCarCrash.crash(crashLocation);

        Assertions.assertTrue(testCarCrash.isCrashed());
        Assertions.assertFalse(testCarNoCrash.isCrashed());
    }
}
