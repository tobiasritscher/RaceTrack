package ch.zhaw.pm2.racetrack.strategy;

import ch.zhaw.pm2.racetrack.Config;
import ch.zhaw.pm2.racetrack.PositionVector;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class MoveListStrategy implements MoveStrategy {
    int currentLine;
    Scanner scanner;
    List<Integer> lines;

    public MoveListStrategy(File file) throws IOException {
        currentLine = 0;
        lines = new ArrayList<>();
        Config.setStrategyDirectory(file);

        scanner = new Scanner(file, StandardCharsets.UTF_8);
        while (scanner.hasNextLine()) {
            lines.add(scanner.nextInt());
        }

    }

    @Override
    public PositionVector.Direction nextMove() {
        return PositionVector.Direction.NONE;
    }

}
