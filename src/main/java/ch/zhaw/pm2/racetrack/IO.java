package ch.zhaw.pm2.racetrack;

import org.beryx.textio.TextIO;
import org.beryx.textio.TextIoFactory;
import org.beryx.textio.TextTerminal;
import org.beryx.textio.swing.SwingTextTerminal;

import java.io.*;
import java.nio.charset.StandardCharsets;
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

    public void println(){
        print("\n");
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

    public Config.StrategyType strategiesInputReader(String output){
        return textIO.newEnumInputReader(Config.StrategyType.class).read(output);
    }

    public void printGrid(Track track){
        var grid = track.getGrid();
        for (int i = 0; i < grid.length; ++i) {
            for (int j = 0; j < grid[i].length; ++j) {
                if (grid[i][j].equals(Config.SpaceType.ANY_CAR)){
                    for (Car car: track.getCars()){
                        if (car.getCarPosition().getX() == j && car.getCarPosition().getY() == i) {
                            print(String.valueOf(car.getName()));
                        }
                    }
                } else {
                    print(grid[i][j].toString());
                }
            }
            println();
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
    public void refresh(Track track) {
        resetBookmark("BLANK_SCREEN");
        printGrid(track);
    }

}
