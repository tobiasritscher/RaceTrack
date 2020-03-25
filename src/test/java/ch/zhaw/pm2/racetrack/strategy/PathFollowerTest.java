package ch.zhaw.pm2.racetrack.strategy;

import ch.zhaw.pm2.racetrack.strategy.PathFollower;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;

public class PathFollowerTest {
    @Test
    void readCoordinatesFromFile_Missformat() throws IOException {
        PathFollower testStrategy = new PathFollower(new File("strategies/teststrategies/missformat.txt"));
        Assertions.assertThrows(RuntimeException.class, () -> testStrategy.readCoordinatesFromFile());
    }
    @Test
    void readCoordinatesFromFile_OkFormat() throws IOException {
        PathFollower testStrategy = new PathFollower(new File("strategies/teststrategies/ok_format.txt"));
        Assertions.assertDoesNotThrow(() -> testStrategy.readCoordinatesFromFile());
    }
    @Test
    void readCoordinatesFromFile_Missformat_MissingCoordinate() throws IOException {
        PathFollower testStrategy = new PathFollower(new File("strategies/teststrategies/missformat_coordinate_missing.txt"));
        Assertions.assertThrows(RuntimeException.class, () -> testStrategy.readCoordinatesFromFile());
    }
}
