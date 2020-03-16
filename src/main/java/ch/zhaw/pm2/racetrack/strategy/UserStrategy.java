package ch.zhaw.pm2.racetrack.strategy;

import ch.zhaw.pm2.racetrack.Car;
import ch.zhaw.pm2.racetrack.IO;
import ch.zhaw.pm2.racetrack.PositionVector;
import ch.zhaw.pm2.racetrack.strategy.MoveStrategy;
import org.beryx.textio.TextIO;
import org.beryx.textio.TextIoFactory;
import org.beryx.textio.TextTerminal;
import org.beryx.textio.swing.SwingTextTerminal;

public class UserStrategy implements MoveStrategy {
    IO io = new IO();

    //todo rename according to clean code rules(find paragraph)
    private static TextIO textIO = TextIoFactory.getTextIO();
    private static TextTerminal<SwingTextTerminal> textTerminal = (SwingTextTerminal) textIO.getTextTerminal();

    @Override
    public PositionVector.Direction nextMove() {
        PositionVector.Direction acceleration = textIO.newEnumInputReader(PositionVector.Direction.class).read("How would you like to accelerate: ");
        return acceleration;
    }
}

