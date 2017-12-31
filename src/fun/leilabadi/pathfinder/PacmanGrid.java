package fun.leilabadi.pathfinder;

import fun.leilabadi.pathfinder.common.Cell;
import fun.leilabadi.pathfinder.common.GridMap;
import fun.leilabadi.pathfinder.common.Location;
import fun.leilabadi.pathfinder.common.Size;

/**
 * @author mosi
 * @version 1.0
 * @since 0.2
 */
public class PacmanGrid extends GridMap<PacmanCell> {

    public PacmanGrid(Size size, Location startLocation, Location goalLocation, PacmanCell[][] cells) {
        super(size, startLocation, goalLocation, convert(size, cells));
    }

    private static Cell[][] convert(Size size, PacmanCell[][] pacmanCells) {
        Cell[][] result = new Cell[size.getRows()][size.getColumns()];
        for (int i = 0; i < size.getRows(); i++) {
            for (int j = 0; j < size.getColumns(); j++) {
                result[i][j] = new Cell(pacmanCells[i][j]);
            }
        }
        return result;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer();
        for (int row = 0; row < size.getRows(); row++) {
            for (int column = 0; column < size.getColumns(); column++) {
                sb.append(cells[row][column].getValue());
            }
            sb.append("\n");
        }
        return sb.toString();
    }
}
