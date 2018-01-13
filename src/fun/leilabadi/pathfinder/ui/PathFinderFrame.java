package fun.leilabadi.pathfinder.ui;

import fun.leilabadi.pathfinder.PathFinder;
import fun.leilabadi.pathfinder.State;
import fun.leilabadi.pathfinder.common.Size;
import fun.leilabadi.pathfinder.map.PathFinderMap;
import fun.leilabadi.pathfinder.ui.components.InteractiveGrid;
import fun.leilabadi.pathfinder.ui.components.InteractiveGridImpl;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;

public class PathFinderFrame extends JFrame {
    private InteractiveGrid grid;
    private Thread thread;

    public PathFinderFrame(PathFinder pathFinder) {
        super("Path Finder");

        PathFinderMap map = pathFinder.getMap();
        Size size = map.getSize();

        int[][] data = new int[size.getRowCount()][size.getColumnCount()];
        for (int y = 0; y < size.getRowCount(); y++) {
            for (int x = 0; x < size.getColumnCount(); x++) {
                data[y][x] = map.getCell(y, x).getCellType().getValue();
            }
        }
        grid = new InteractiveGridImpl(size.getRowCount(), size.getColumnCount());
        grid.loadData(data);

        setSize(size.getColumnCount() * UiConfigs.CELL_SIZE, size.getRowCount() * UiConfigs.CELL_SIZE);
        setLayout(new BorderLayout());
        setVisible(true);
        add((JComponent) grid, BorderLayout.CENTER);

        this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent windowEvent) {
                onWindowClosing();
            }
        });

        pathFinder.setFindProgressListener(finder -> {
            final int[][] data1 = grid.getData();
            for (State state : finder.getVisitedNodes()) {
                data1[state.getCurrentLocation().y][state.getCurrentLocation().x] = 5;
            }
            for (State state : finder.getExploredNodes()) {
                data1[state.getCurrentLocation().y][state.getCurrentLocation().x] = 6;
            }
            grid.loadData(data1);
            try {
                Thread.sleep(10);
            } catch (InterruptedException ignored) {
            }
        });

        thread = new Thread(() -> {
            final List<State> path = pathFinder.findPath();
            final int[][] data1 = grid.getData();
            for (State state : path) {
                data1[state.getCurrentLocation().y][state.getCurrentLocation().x] = 7;
            }
            grid.loadData(data1);
        });
        thread.start();
    }

    private void onWindowClosing() {
        if (thread != null)
            try {
                thread.join();
            } catch (InterruptedException ignored) {
            }
        thread = null;
        System.exit(0);
    }
}
