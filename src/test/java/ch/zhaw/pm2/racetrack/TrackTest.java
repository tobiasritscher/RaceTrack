package ch.zhaw.pm2.racetrack;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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
