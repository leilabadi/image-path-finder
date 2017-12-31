package fun.leilabadi.pathfinder;

import fun.leilabadi.pathfinder.common.Location;
import fun.leilabadi.pathfinder.common.Size;
import fun.leilabadi.pathfinder.common.State;
import fun.leilabadi.pathfinder.graphic.GraphicGrid;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

/**
 * @author mosi
 * @version 1.0
 * @since 0.2
 */
public class MainApplet extends JApplet {
    private static final int CELL_SIZE = 15;
    private GraphicGrid component;
    private Thread thread;

    @Override
    public void init() {
        String[] lines;
        try {
            ClassLoader classLoader = getClass().getClassLoader();
            URL url = classLoader.getResource("pacman-complex.txt");
            //noinspection ToArrayCallWithZeroLengthArrayArgument
            lines = Files.readAllLines(Paths.get(url.toURI()).toAbsolutePath()).toArray(new String[0]);
        } catch (URISyntaxException | IOException | NullPointerException e) {
            System.out.println(e.getMessage());
            return;
        }

        int index = 0;
        while (lines[index].startsWith("#")) {
            index++;
        }

        String[] tempArray;
        tempArray = lines[index++].split(" ");
        int pacmanRow = Integer.parseInt(tempArray[0]);
        int pacmanColumn = Integer.parseInt(tempArray[1]);

        tempArray = lines[index++].split(" ");
        int foodRow = Integer.parseInt(tempArray[0]);
        int foodColumn = Integer.parseInt(tempArray[1]);

        tempArray = lines[index++].split(" ");
        int rows = Integer.parseInt(tempArray[0]);
        int columns = Integer.parseInt(tempArray[1]);

        String gridRows[] = new String[rows];
        for (int row = 0; row < rows; row++) {
            gridRows[row] = lines[index++];
        }

        PacmanCell[][] cells = new PacmanCell[rows][columns];
        for (int row = 0; row < rows; row++) {
            for (int column = 0; column < columns; column++) {
                cells[row][column] = PacmanCell.fromSymbol(gridRows[row].charAt(column));
            }
        }

        Size gridSize = new Size(rows, columns);
        Location startLocation = new Location(pacmanRow, pacmanColumn);
        Location goalLocation = new Location(foodRow, foodColumn);
        PacmanSolution pacmanSolution = new PacmanSolution(gridSize, startLocation, goalLocation, cells, e -> {
            component.update((PacmanSolution) e.getSource());
            try {
                Thread.sleep(50);
            } catch (InterruptedException ignored) {
            }
        });

        component = new GraphicGrid(pacmanSolution.getGridMap());
        setSize(gridSize.getColumns() * CELL_SIZE, gridSize.getRows() * CELL_SIZE);
        setLayout(new BorderLayout());
        add(component, BorderLayout.CENTER);

        thread = new Thread(() -> {
            List<State> path = pacmanSolution.calculatePath();
            component.highlightPath(path);
        });
        thread.start();
    }

    @Override
    public void destroy() {
        if (thread != null)
            try {
                thread.join();
            } catch (InterruptedException ignored) {
            }
        thread = null;
    }

    @Override
    public void start() {
    }

    @Override
    public void stop() {
    }
}
