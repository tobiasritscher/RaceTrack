package ch.zhaw.pm2.racetrack;
import org.beryx.textio.*;
import org.beryx.textio.swing.SwingTextTerminal;

public class IO {
    private static TextIO textIO = TextIoFactory.getTextIO();
    private static TextTerminal<SwingTextTerminal> textTerminal = (SwingTextTerminal) textIO.getTextTerminal();

    public IO() {

    }

    public void print(String output){
        textTerminal.print(output);
    }

    public int intInputReader(int min, int max, String output) {
        return textIO.newIntInputReader().withMinVal(min).withMaxVal(max).read(output);
    }

    public char charInputReader(String output) {
        return textIO.newCharInputReader().read(output);
    }

    public boolean booleanInputReader(String output) {
        return textIO.newBooleanInputReader().read(output);
    }

    public PositionVector.Direction positionVectorInputReader(String output){
        return textIO.newEnumInputReader(PositionVector.Direction.class).read(output);
    }

}
