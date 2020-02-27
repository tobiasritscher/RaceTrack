package ch.zhaw.pm2.racetrack;

import org.beryx.textio.TextIO;
import org.beryx.textio.TextIoFactory;
import org.beryx.textio.TextTerminal;
import org.beryx.textio.swing.SwingTextTerminal;

import java.io.File;
import java.io.FileNotFoundException;


public class Start {
    private static TextIO textIO = TextIoFactory.getTextIO();
    private static TextTerminal<SwingTextTerminal> textTerminal = (SwingTextTerminal) textIO.getTextTerminal();
    public Start() {

    }

    public static void main(String[] args)  {
        try {
            setUpGame();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        amountOfPlayers();
    }

    private static void setUpGame() throws FileNotFoundException {
        File files = new File("tracks/");
        textIO.newIntInputReader().withMinVal(1).withMaxVal(4).read(files.toString());
    }

    public static void amountOfPlayers(){
        Integer players = textIO.newIntInputReader().withMinVal(2).withMaxVal(9).read("How many players will be playing?");

    }
}
