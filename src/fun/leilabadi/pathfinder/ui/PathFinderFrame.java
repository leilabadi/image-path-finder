package fun.leilabadi.pathfinder.ui;

import fun.leilabadi.pathfinder.PathFinder;
import fun.leilabadi.pathfinder.State;
import fun.leilabadi.pathfinder.common.Size;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;

public class PathFinderFrame extends JFrame {
    private static final int CELL_SIZE = 15;
    private PathFinderGrid grid;
    private Thread thread;

    public PathFinderFrame(PathFinder pathFinder) {
        super("Path Finder");

        grid = new PathFinderGrid(pathFinder.getMap());
        Size size = pathFinder.getMap().getSize();

        setSize(size.getColumnCount() * CELL_SIZE, size.getRowCount() * CELL_SIZE);
        setLayout(new BorderLayout());
        setVisible(true);
        add(grid, BorderLayout.CENTER);

        this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent windowEvent) {
                onWindowClosing();
            }
        });

        pathFinder.setFindProgressListener(finder -> {
            grid.updateProgress(finder.getVisitedNodes(), finder.getExploredNodes());
            try {
                Thread.sleep(10);
            } catch (InterruptedException ignored) {
            }
        });

        thread = new Thread(() -> {
            final List<State> path = pathFinder.findPath();
            grid.highlightPath(path);
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
