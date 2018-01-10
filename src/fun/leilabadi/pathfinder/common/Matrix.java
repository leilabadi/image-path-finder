package fun.leilabadi.pathfinder.common;

public class Matrix {
    private final int rowCount;
    private final int columnCount;
    private final MatrixElement[][] elements;

    public Matrix(int rowCount, int columnCount) {
        this.rowCount = rowCount;
        this.columnCount = columnCount;
        elements = new MatrixElement[rowCount][columnCount];
        for (int y = 0; y < rowCount; y++) {
            for (int x = 0; x < columnCount; x++) {
                elements[y][x] = new MatrixElement(x, y);
            }
        }
    }

    public int getRowCount() {
        return rowCount;
    }

    public int getColumnCount() {
        return columnCount;
    }

    public MatrixElement getElement(int x, int y) {
        if (((0 <= y) && (y < rowCount)) && ((0 <= x) && (x < columnCount)))
            return elements[y][x];
        return null;
    }

    public void setData(int[][] rawData) {
        for (int y = 0; y < rowCount; y++) {
            for (int x = 0; x < columnCount; x++) {
                elements[y][x].setState(rawData[y][x]);
            }
        }
    }

    public int[][] getRawData() {
        int[][] result = new int[rowCount][columnCount];
        for (int y = 0; y < rowCount; y++) {
            for (int x = 0; x < columnCount; x++) {
                result[y][x] = elements[y][x].getState();
            }
        }
        return result;
    }
}
