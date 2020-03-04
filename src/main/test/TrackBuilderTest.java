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
    private File testTrack;
    private Object IOException;

    @BeforeEach
    public void init(){
        // TODO: initialize variables
    }

    @Test
    public Config.SpaceType[][] buildTrackTest(File file) throws IOException, InvalidTrackFormatException {
        // test if file is found
        //Assertions.assertThrows(IOException, ...);

        Config.SpaceType[][] track = trackBuilder.buildTrack(testTrack);
        return track;
    }
}
