package ch.zhaw.pm2.racetrack.strategy;

import ch.zhaw.pm2.racetrack.PositionVector;

public class DoNotMoveStrategy implements MoveStrategy {

    /**
     * returns always null as direction
     *
     * @return null as direction
     */
    @Override
    public PositionVector.Direction nextMove() {
        return PositionVector.Direction.NONE;
    }
}
