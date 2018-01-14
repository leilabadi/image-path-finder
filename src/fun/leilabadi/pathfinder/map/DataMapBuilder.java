package fun.leilabadi.pathfinder.map;

import fun.leilabadi.pathfinder.Cell;
import fun.leilabadi.pathfinder.CellType;
import fun.leilabadi.pathfinder.common.Location;
import fun.leilabadi.pathfinder.common.Size;

public class DataMapBuilder extends MapBuilder {
    private int rowCount;
    private int columnCount;
    private int[][] data;

    public DataMapBuilder(int rowCount, int columnCount, int[][] data) {
        if (rowCount < 3 || columnCount < 3 || rowCount * columnCount < 12)
            throw new IllegalArgumentException("The smallest allowed map is 3*4.");
        this.rowCount = rowCount;
        this.columnCount = columnCount;
        this.data = data;
    }

    @Override
    public void build() {

        Size size = new Size(rowCount, columnCount);
        Location startLocation = null;
        Location goalLocation = null;

        CellType cellType;
        Cell[][] cells = new Cell[rowCount][columnCount];
        for (int row = 0; row < rowCount; row++) {
            for (int column = 0; column < columnCount; column++) {

                cellType = CellType.fromValue((byte) data[row][column]);

                if (cellType == CellType.START)
                    startLocation = new Location(row, column);
                else if (cellType == CellType.GOAL)
                    goalLocation = new Location(row, column);

                cells[row][column] = new Cell(cellType);
            }
        }

        if (startLocation == null || goalLocation == null)
            throw new RuntimeException("Provided map data is not valid.");

        this.map = new GridMap(size, startLocation, goalLocation, cells);
    }
}
