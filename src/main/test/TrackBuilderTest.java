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
        Assertions.assertEquals(10, trackBuilder.getTrackWidth());
    }
}
