package ch.zhaw.pm2.racetrack;

import ch.zhaw.pm2.racetrack.exceptions.InvalidTrackFormatException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;

public class TrackBuilderTest {
    private TrackBuilder trackBuilder;

    @BeforeEach
    public void init() {
        // TODO: initialize variables
        trackBuilder = new TrackBuilder();
    }

    @Test
    public void fileNotFoundTest() throws IOException, InvalidTrackFormatException {
        File testFile = new File("testtracks/nonsense.txt");
        Assertions.assertThrows(IOException.class, () -> {
            trackBuilder.buildTrack(testFile);
        });
    }

    @Test
    public void wrongTrackWidthTest() throws IOException, InvalidTrackFormatException {
        File testFile = new File("testtracks/wrong_track_width.txt");
        Assertions.assertThrows(InvalidTrackFormatException.class, () -> {
            trackBuilder.buildTrack(testFile);
        });
    }

    @Test
    public void emptyTrackTest() throws IOException, InvalidTrackFormatException {
        File testFile = new File("testtracks/empty_track.txt");
        Assertions.assertThrows(InvalidTrackFormatException.class, () -> {
            trackBuilder.buildTrack(testFile);
        });
    }

    @Test
    public void removeEmptyAndFollowingLinesTest() throws IOException, InvalidTrackFormatException {
        File testFile = new File("testtracks/empty_and_following_lines.txt");
        trackBuilder.buildTrack(testFile);
        Assertions.assertEquals(3, trackBuilder.getTrackHeight());
    }

    @Test
    public void tooManyCarsTest() throws InvalidTrackFormatException {
        File testFile = new File("testtracks/too_many_cars.txt");
        Assertions.assertThrows(InvalidTrackFormatException.class, () -> {
            trackBuilder.buildTrack(testFile);
        });
    }

    @Test
    public void notEnoughCarsTest() throws InvalidTrackFormatException {
        File testFile = new File("testtracks/not_enough_cars.txt");
        Assertions.assertThrows(InvalidTrackFormatException.class, () -> {
            trackBuilder.buildTrack(testFile);
        });
    }

    @Test
    public void amountOfCarsTest() throws InvalidTrackFormatException, IOException {
        File testFile = new File("testtracks/height_and_width_test.txt");
        trackBuilder.buildTrack(testFile);
        Assertions.assertEquals(9, trackBuilder.getCarMap().size());
    }

    @Test
    public void duplicateCarsTest() throws InvalidTrackFormatException {
        File testFile = new File("testtracks/duplicate_cars.txt");
        Assertions.assertThrows(InvalidTrackFormatException.class, () -> {
            trackBuilder.buildTrack(testFile);
        });
    }

    @Test
    public void heightAndWidthTest() throws IOException, InvalidTrackFormatException {
        File testFile = new File("testtracks/height_and_width_test.txt");
        trackBuilder.buildTrack(testFile);
        Assertions.assertEquals(10, trackBuilder.getTrackHeight());
        Assertions.assertEquals(11, trackBuilder.getTrackWidth());
    }

    @Test public void mapFillTest() throws IOException, InvalidTrackFormatException {
    Config.SpaceType[][] testMap = new Config.SpaceType[2][5];
    testMap[0][0] = Config.SpaceType.WALL;
    testMap[0][1] = Config.SpaceType.FINISH_UP;
    testMap[0][2] = Config.SpaceType.FINISH_LEFT;
    testMap[0][3] = Config.SpaceType.FINISH_DOWN;
    testMap[0][4] = Config.SpaceType.WALL;

    testMap[1][0] = Config.SpaceType.WALL;
    testMap[1][1] = Config.SpaceType.ANY_CAR;
    testMap[1][2] = Config.SpaceType.FINISH_RIGHT;
    testMap[1][3] = Config.SpaceType.ANY_CAR;
    testMap[1][4] = Config.SpaceType.WALL;

    File testFile = new File("testtracks/track_fill_test.txt");

    Assertions.assertArrayEquals(testMap, trackBuilder.buildTrack(testFile));
    }
}
