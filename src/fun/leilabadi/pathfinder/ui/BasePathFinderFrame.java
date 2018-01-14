package fun.leilabadi.pathfinder.ui;

import fun.leilabadi.pathfinder.PathFinder;
import fun.leilabadi.pathfinder.State;
import fun.leilabadi.pathfinder.common.Location;
import fun.leilabadi.pathfinder.common.Size;
import fun.leilabadi.pathfinder.config.UiConfigs;
import fun.leilabadi.pathfinder.ui.components.InteractiveGrid;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class BasePathFinderFrame extends JFrame {
    protected InteractiveGrid grid;
    protected PathFinder pathFinder;
    private Thread thread;
    private boolean run;

    protected BasePathFinderFrame(String title) throws HeadlessException {
        super(title);
    }

    protected void setProperSize(Size size) {
        int cellWidth = UiConfigs.MIN_WINDOW_WIDTH / size.getColumnCount();
        int width = size.getColumnCount() * cellWidth;
        if (width < UiConfigs.MIN_WINDOW_WIDTH) width = UiConfigs.MIN_WINDOW_WIDTH;
        int height = size.getRowCount() * cellWidth;
        if (height < UiConfigs.MIN_WINDOW_HEIGHT) height = UiConfigs.MIN_WINDOW_HEIGHT;

        setSize(width, height);
    }

    protected void registerProgressListener() {
        pathFinder.setFindProgressListener(finder -> {
            Location location;
            final int[][] data = grid.getData();
            for (State state : finder.getVisitedNodes()) {
                location = state.getCurrentLocation();
                if (location.distanceFrom(finder.getMap().getStartLocation()) != 0
                        && location.distanceFrom(finder.getMap().getGoalLocation()) != 0) {
                    data[location.getY()][location.getX()] = CellProgress.VISITED.getValue();
                }
            }
            for (State state : finder.getExploredNodes()) {
                location = state.getCurrentLocation();
                if (location.distanceFrom(finder.getMap().getStartLocation()) != 0
                        && location.distanceFrom(finder.getMap().getGoalLocation()) != 0) {
                    data[location.getY()][location.getX()] = CellProgress.EXPLORED.getValue();
                }
            }
            grid.loadData(data);
            try {
                Thread.sleep(UiConfigs.EXPLORE_STEP_WAIT_MILLISECOND);
            } catch (InterruptedException ignored) {
            }
        });
    }

    protected void runPathFinder() {
        if (!run) {
            if (thread != null) {
                try {
                    thread.join();
                } catch (InterruptedException ignored) {
                }
            }

            run = true;
            thread = new Thread(() -> {
                final List<State> path = pathFinder.findPath();

                Location location;
                final int[][] data = grid.getData();
                for (State state : path) {
                    location = state.getCurrentLocation();
                    if (location.distanceFrom(pathFinder.getMap().getStartLocation()) != 0
                            && location.distanceFrom(pathFinder.getMap().getGoalLocation()) != 0) {
                        data[location.getY()][location.getX()] = CellProgress.PATH.getValue();
                    }
                }
                grid.loadData(data);
            });
            thread.start();
        }
    }

    protected void reset() {
        run = false;
    }

    protected void onWindowClosing() {
        if (thread != null) {
            try {
                thread.join();
            } catch (InterruptedException ignored) {
            }
        }

        thread = null;
        System.exit(0);
    }
}
