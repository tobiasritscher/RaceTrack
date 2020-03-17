package ch.zhaw.pm2.racetrack.strategy;

import ch.zhaw.pm2.racetrack.IO;
import ch.zhaw.pm2.racetrack.PositionVector;

public class DoNotMoveStrategy implements MoveStrategy {

    @Override
    public PositionVector.Direction nextMove() {
        return PositionVector.Direction.NONE;
    }

}
