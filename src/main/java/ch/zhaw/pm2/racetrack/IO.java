package ch.zhaw.pm2.racetrack;

import org.beryx.textio.TextIO;
import org.beryx.textio.TextIoFactory;
import org.beryx.textio.TextTerminal;
import org.beryx.textio.swing.SwingTextTerminal;

public class IO {
    private static TextIO textIO = TextIoFactory.getTextIO();
    private static TextTerminal<SwingTextTerminal> textTerminal = (SwingTextTerminal) textIO.getTextTerminal();

    public IO() {

    }

    /**
     * prints the output on the terminal
     *
     * @param output the output as a String
     */
    public void print(String output) {
        textTerminal.print(output);
    }

    /**
     * prints the output on the terminal and asks the user to choose an option by tiping the
     * according number. Checks fro the right number and asks again if the number is out of bounds
     *
     * @param min    lowest possible option
     * @param max    highst possible option
     * @param output the ouput as a String
     * @return the chosen int
     */
    public int intInputReader(int min, int max, String output) {
        return textIO.newIntInputReader().withMinVal(min).withMaxVal(max).read(output);
    }

    /**
     * prints the ouput and asks wich Vektor the user wants to choose
     *
     * @param output the output as a String
     * @return the chosen PositionVektor
     */
    public PositionVector.Direction positionVectorInputReader(String output) {
        return textIO.newEnumInputReader(PositionVector.Direction.class).read(output);
    }

    /**
     * prints the output and asks wich strategy the player wants to use
     *
     * @param output the output as a String
     * @return the chosen Trategy
     */
    public Config.StrategyType strategiesInputReader(String output) {
        return textIO.newEnumInputReader(Config.StrategyType.class).read(output);
    }

    /**
     * prints the current grid on the terminal
     *
     * @param track the track to print
     */
    public void printGrid(Track track) {
        print(track.toString());
    }

    /**
     * This function closes the terminal window
     */
    public void closeTerminal() {
        textIO.dispose();
    }

    /**
     * This function prints a output in the terminal window and continiue if the user presses enter
     *
     * @param output the ouput to print
     */
    public void promptEnter(String output) {
        textIO.newStringInputReader()
                .withMinLength(0)
                .read("\n" + output);
    }

    /**
     * This function sets a bookmark in the terminal window for a blank terminal screen
     */
    public void setBookmarkBlankScreen() {
        textTerminal.setBookmark("BLANK_SCREEN");
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
     * This function cleans up the terminal and prints the current state of the grid
     *
     * @param track the track to print
     */
    public void refresh(Track track) {
        resetBookmark("BLANK_SCREEN");
        printGrid(track);
    }
}
