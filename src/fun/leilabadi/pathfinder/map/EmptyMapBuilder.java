package fun.leilabadi.pathfinder.map;

import fun.leilabadi.pathfinder.Cell;
import fun.leilabadi.pathfinder.CellType;
import fun.leilabadi.pathfinder.common.Location;
import fun.leilabadi.pathfinder.common.Size;

public class EmptyMapBuilder extends MapBuilder {
    private int rowCount;
    private int columnCount;

    public EmptyMapBuilder(int rowCount, int columnCount) {
        if (rowCount < 3 || columnCount < 3 || rowCount * columnCount < 12)
            throw new IllegalArgumentException("The smallest allowed map is 3*4.");
        this.rowCount = rowCount;
        this.columnCount = columnCount;
    }

    @Override
    public void build() {

        Size size = new Size(rowCount, columnCount);
        Location startLocation = new Location(1, 1);
        Location goalLocation = new Location(rowCount - 2, columnCount - 2);

        CellType cellType;
        Location location;
        Cell[][] cells = new Cell[rowCount][columnCount];
        for (int row = 0; row < rowCount; row++) {
            for (int column = 0; column < columnCount; column++) {

                location = new Location(row, column);

                if (location.distanceFrom(startLocation) == 0)
                    cellType = CellType.START;
                else if (location.distanceFrom(goalLocation) == 0)
                    cellType = CellType.GOAL;
                else
                    cellType = CellType.EMPTY;

                cells[row][column] = new Cell(cellType);
            }
        }

        this.map = new GridMap(size, startLocation, goalLocation, cells);
    }
}
