package fun.leilabadi.pathfinder.common;

public class Size {
    private int rowCount;
    private int columnCount;

    public Size(int rowCount, int columnCount) {
        this.rowCount = rowCount;
        this.columnCount = columnCount;
    }

    public Integer getRowCount() {
        return rowCount;
    }

    public Integer getColumnCount() {
        return columnCount;
    }

    @Override
    public String toString() {
        return rowCount + "X" + columnCount;
    }
}
