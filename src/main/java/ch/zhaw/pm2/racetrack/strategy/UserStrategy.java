package ch.zhaw.pm2.racetrack.strategy;

import ch.zhaw.pm2.racetrack.IO;
import ch.zhaw.pm2.racetrack.PositionVector;

public class UserStrategy implements MoveStrategy {
    IO io = new IO();

    @Override
    public PositionVector.Direction nextMove() {
        return io.positionVectorInputReader("How would you like to accelerate: ");
    }
}

