package fun.leilabadi.pathfinder.common;

/**
 * @author mosi
 * @version 1.0
 * @since 0.2
 */
public class GridMap<T> {
    protected final Size size;
    protected final Location startLocation;
    protected final Location goalLocation;
    protected final Cell<T>[][] cells;

    public GridMap(Size size, Location startLocation, Location goalLocation, Cell[][] cells) {
        this.size = size;
        this.startLocation = startLocation;
        this.goalLocation = goalLocation;
        this.cells = cells;
    }

    public Size getSize() {
        return size;
    }

    public Location getStartLocation() {
        return startLocation;
    }

    public Location getGoalLocation() {
        return goalLocation;
    }

    public Cell<T> getCell(int i, int j) {
        if (((0 <= i) && (i < size.getRows())) && ((0 <= j) && (j < size.getColumns())))
            return cells[i][j];
        return null;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer();
        for (int i = 0; i < size.getRows(); i++) {
            for (int j = 0; j < size.getColumns(); j++) {
                sb.append(cells[i][j]);
            }
            sb.append("\r\n");
        }
        return sb.toString();
    }
}
