package ch.zhaw.pm2.racetrack.strategy;

import ch.zhaw.pm2.racetrack.IO;
import ch.zhaw.pm2.racetrack.PositionVector;

public class UserStrategy implements MoveStrategy {
    IO io = new IO();

    /**
     * Asks the user for the direction he wants to use for the next turn
     *
     * @return the chosen direction
     */
    @Override
    public PositionVector.Direction nextMove() {
        return io.positionVectorInputReader("How would you like to accelerate: ");
    }
}

