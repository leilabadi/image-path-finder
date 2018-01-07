package fun.leilabadi.pathfinder.ui.components;

public class InteractiveGrid {
    private final int rows;
    private final int columns;
    private final InteractiveGridCell[][] cells;

    public InteractiveGrid(int rows, int columns)  {
        this.rows = rows;
        this.columns = columns;
        cells = new InteractiveGridCell[rows][columns];
        for (int y = 0; y < rows; y++) {
            for (int x = 0; x < columns; x++) {
                cells[y][x] = new InteractiveGridCell(x, y);
            }
        }
    }

    public int getRows() {
        return rows;
    }

    public int getColumns() {
        return columns;
    }

    public InteractiveGridCell getCell(int x, int y) {
        if (((0 <= y) && (y < rows)) && ((0 <= x) && (x < columns)))
            return cells[y][x];
        return null;
    }
}
