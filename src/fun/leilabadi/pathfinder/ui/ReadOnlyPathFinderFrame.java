package fun.leilabadi.pathfinder.ui;

import fun.leilabadi.pathfinder.PathFinder;
import fun.leilabadi.pathfinder.common.Size;
import fun.leilabadi.pathfinder.common.UiMode;
import fun.leilabadi.pathfinder.map.PathFinderMap;
import fun.leilabadi.pathfinder.ui.components.InteractiveGridImpl;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class ReadOnlyPathFinderFrame extends BasePathFinderFrame {

    public ReadOnlyPathFinderFrame(PathFinder pathFinder) {
        super("Path Finder");
        super.pathFinder = pathFinder;

        PathFinderMap map = pathFinder.getMap();
        Size size = map.getSize();

        int[][] data = new int[size.getRowCount()][size.getColumnCount()];
        for (int y = 0; y < size.getRowCount(); y++) {
            for (int x = 0; x < size.getColumnCount(); x++) {
                data[y][x] = map.getCell(y, x).getCellType().getValue();
            }
        }
        grid = new InteractiveGridImpl(size.getRowCount(), size.getColumnCount(), UiMode.VIEW);
        grid.loadData(data);

        setProperSize(size);
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

        registerProgressListener();
        runPathFinder();
    }
}
