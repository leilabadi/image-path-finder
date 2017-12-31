package fun.leilabadi.pathfinder.common;

/**
 * @author mosi
 * @version 1.0
 * @since 0.2
 */
public class Location implements Cloneable<Location> {
    public int row;
    public int column;

    public Location(int row, int column) {
        this.row = row;
        this.column = column;
    }

    public Integer getRow() {
        return row;
    }

    public Integer getColumn() {
        return column;
    }

    public int distanceFrom(Location target) {
        return Math.abs(row - target.row) + Math.abs(column - target.column);
    }

    @Override
    public Location clone() {
        return new Location(row, column);
    }

    @Override
    public String toString() {
        return "(" + row + "," + column + ')';
    }
}
