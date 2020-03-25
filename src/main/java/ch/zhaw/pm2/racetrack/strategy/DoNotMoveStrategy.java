package ch.zhaw.pm2.racetrack.strategy;

import ch.zhaw.pm2.racetrack.PositionVector;

public class DoNotMoveStrategy implements MoveStrategy {

    /**
     * Returns always PositionVector.Direction.NONE as direction
     *
     * @return PositionVector.Direction.NONE
     */
    @Override
    public PositionVector.Direction nextMove() {
        return PositionVector.Direction.NONE;
    }
}
