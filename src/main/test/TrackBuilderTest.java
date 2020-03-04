import ch.zhaw.pm2.racetrack.Config;
import ch.zhaw.pm2.racetrack.InvalidTrackFormatException;
import ch.zhaw.pm2.racetrack.TrackBuilder;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;

public class TrackBuilderTest {
    private TrackBuilder trackBuilder;
    private Object IOException;
    private Object InvalidTrackFormatException;

    @BeforeEach
    public void init(){
        // TODO: initialize variables
    }

    @Test
    public void buildTrackTest() throws IOException, InvalidTrackFormatException {
        File testFile = new File("testtracks/wrong_track_width.txt");
        //Assertions.assertThrows(InvalidTrackFormatException,trackBuilder.buildTrack(testFile));
    }
}
