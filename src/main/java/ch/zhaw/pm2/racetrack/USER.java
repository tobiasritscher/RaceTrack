package ch.zhaw.pm2.racetrack;

import ch.zhaw.pm2.racetrack.strategy.MoveStrategy;
import org.beryx.textio.TextIO;
import org.beryx.textio.TextIoFactory;
import org.beryx.textio.TextTerminal;
import org.beryx.textio.swing.SwingTextTerminal;

public class USER implements MoveStrategy {
    private static TextIO textIO = TextIoFactory.getTextIO();
    private static TextTerminal<SwingTextTerminal> textTerminal = (SwingTextTerminal) textIO.getTextTerminal();
    Car car = new Car();

    @Override
    public PositionVector.Direction nextMove() {
        PositionVector.Direction acceleration = textIO.newEnumInputReader(PositionVector.Direction.class).read("How would you like to accelerate: ");
        car.setSpeed(PositionVector.add(car.getSpeed(), acceleration.vector));
        car.setPosition(PositionVector.add(car.getPosition(), car.getSpeed()));
        return acceleration;
    }
}

