package fun.leilabadi.pathfinder.map;

import fun.leilabadi.pathfinder.Cell;
import fun.leilabadi.pathfinder.common.Location;
import fun.leilabadi.pathfinder.common.Size;

public class GridMap implements PathFinderMap {
    private final Size size;
    private final Location startLocation;
    private final Location goalLocation;
    private final Cell[][] cells;

    GridMap(Size size, Location startLocation, Location goalLocation, Cell[][] cells) {
        this.size = size;
        this.startLocation = startLocation;
        this.goalLocation = goalLocation;
        this.cells = cells;
    }

    @Override
    public Size getSize() {
        return size;
    }

    @Override
    public Location getStartLocation() {
        return startLocation;
    }

    @Override
    public Location getGoalLocation() {
        return goalLocation;
    }

    @Override
    public Cell getCell(int row, int column) {
        if (((0 <= row) && (row < size.getRowCount())) && ((0 <= column) && (column < size.getColumnCount())))
            return cells[row][column];
        return null;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer();
        for (int i = 0; i < size.getRowCount(); i++) {
            for (int j = 0; j < size.getColumnCount(); j++) {
                sb.append(cells[i][j]);
            }
            sb.append("\r\n");
        }
        return sb.toString();
    }
}
