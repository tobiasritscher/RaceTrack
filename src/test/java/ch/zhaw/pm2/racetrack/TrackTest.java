package ch.zhaw.pm2.racetrack;

import ch.zhaw.pm2.racetrack.exceptions.InvalidTrackFormatException;
import org.junit.jupiter.api.BeforeEach;

import java.io.File;
import java.io.IOException;

public class TrackTest {
    private Track testTrack;
    private File trackSource;

    @BeforeEach
    public void init() throws IOException, InvalidTrackFormatException {
        // TODO: initialize variables
        testTrack = new Track(trackSource);
    }

    // TODO: make some radical, tubular, outrageous, wicked-cool track tests

}
