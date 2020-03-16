package ch.zhaw.pm2.racetrack;

import org.beryx.textio.TextIO;
import org.beryx.textio.TextIoFactory;
import org.beryx.textio.TextTerminal;
import org.beryx.textio.swing.SwingTextTerminal;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

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

    public void printGrid(File file){
       // try (BufferedReader br = new BufferedReader(new FileReader(file))) {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file),"UTF-8"))) {
            String line;
            while ((line = br.readLine()) != null) {
                print(line + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * This function closes the terminal window
     */
    public void closeTerminal() {
        textIO.dispose();
    }

    /**
     * This function prints a prompt in the terminal window that asks the user to press enter
     */
    public void promptEnter() {
        textIO.newStringInputReader()
                .withMinLength(0)
                .read("\nPress enter to continue");
    }

    /**
     * This function sets a bookmark in the terminal window
     *
     */
    public void setBookmarkBlankScreen() {
        textTerminal.setBookmark("BLANK_SCREEN");
    }

    /**
     * This function sets a bookmark in the terminal window
     *
     */
    public void setBookmark(String bookmark) {
        textTerminal.setBookmark(bookmark);
    }

    /**
     * This function allows the terminal window to jump to a bookmark that has been set before
     *
     * @param bookmark Name of the bookmark that the window will be reset to
     */
    public void resetBookmark(String bookmark) {
        textTerminal.resetToBookmark(bookmark);
    }

    /**
     * This function refreshes the gameboard, when e.g. a new settlement has been built.
     * It does so by jumping back to a "blank screen" that has been set in the beginning
     * of the game and prints an instance of the gameboard.
     */
    public void refresh(File file) {
        resetBookmark("BLANK_SCREEN");
        printGrid(file);
    }

}
