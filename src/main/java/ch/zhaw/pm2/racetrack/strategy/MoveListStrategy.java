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
    List<PositionVector> lines;

    public MoveListStrategy(File file) throws IOException {
        currentLine = 0;
        lines = new ArrayList<>();
        Config.setStrategyDirectory(file);

        scanner = new Scanner(file, StandardCharsets.UTF_8);
        while (scanner.hasNextLine()) {
            String[] line;
            line = scanner.nextLine().trim().split(" ");
            PositionVector vektor = new PositionVector(Integer.parseInt(line[0]), Integer.parseInt(line[1]));

            lines.add(new PositionVector.Direction(vektor));
        }
    }

    @Override
    public PositionVector.Direction nextMove() {
        return PositionVector.Direction.NONE;
    }

}
