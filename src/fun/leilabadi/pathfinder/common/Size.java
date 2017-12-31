package fun.leilabadi.pathfinder.common;

/**
 * @author mosi
 * @version 1.0
 * @since 0.2
 */
public class Size {
    private int rows;
    private int columns;

    public Size(int rows, int columns) {
        this.rows = rows;
        this.columns = columns;
    }

    public Integer getRows() {
        return rows;
    }

    public Integer getColumns() {
        return columns;
    }

    @Override
    public String toString() {
        return rows + "X" + columns;
    }
}
