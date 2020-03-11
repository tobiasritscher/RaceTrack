package ch.zhaw.pm2.racetrack.strategy;

import ch.zhaw.pm2.racetrack.PositionVector;
import ch.zhaw.pm2.racetrack.strategy.MoveStrategy;

public class DO_NOT_MOVE_STRATEGY implements MoveStrategy {

    @Override
    public PositionVector.Direction nextMove() {
        return PositionVector.Direction.NONE;
    }

}
