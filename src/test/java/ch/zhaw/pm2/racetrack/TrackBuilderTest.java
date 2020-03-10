package ch.zhaw.pm2.racetrack;

import ch.zhaw.pm2.racetrack.InvalidTrackFormatException;
import ch.zhaw.pm2.racetrack.Track;
import ch.zhaw.pm2.racetrack.TrackBuilder;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;

public class TrackBuilderTest {
    private TrackBuilder trackBuilder;
    private Track track;
    private Object IOException;
    private Object InvalidTrackFormatException;

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
    public void tooManyCarsTest() throws InvalidTrackFormatException {
        File testFile = new File("testtracks/too_many_cars.txt");
        Assertions.assertThrows(InvalidTrackFormatException.class, () -> {
            trackBuilder.buildTrack(testFile);
        });
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

    @Test
    public void mapFillTest() throws IOException, InvalidTrackFormatException {
        Config.SpaceType[][] testMap = new Config.SpaceType[2][5];
        testMap[0][0] = Config.SpaceType.WALL;
        testMap[0][1] = Config.SpaceType.WALL;
        testMap[0][2] = Config.SpaceType.WALL;
        testMap[0][3] = Config.SpaceType.WALL;
        testMap[0][4] = Config.SpaceType.WALL;

        testMap[1][0] = Config.SpaceType.WALL;
        testMap[1][1] = Config.SpaceType.ANY_CAR;
        testMap[1][2] = Config.SpaceType.FINISH_RIGHT;
        testMap[1][3] = Config.SpaceType.TRACK;
        testMap[1][4] = Config.SpaceType.WALL;

        File testFile = new File("testtracks/track_fill_test.txt");

        Assertions.assertEquals(testMap, trackBuilder.buildTrack(testFile));
    }
}
