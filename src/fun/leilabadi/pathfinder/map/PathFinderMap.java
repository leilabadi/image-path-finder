package fun.leilabadi.pathfinder.map;

import fun.leilabadi.pathfinder.Cell;
import fun.leilabadi.pathfinder.common.Location;
import fun.leilabadi.pathfinder.common.Size;

public interface PathFinderMap {

    Size getSize();

    Location getStartLocation();

    Location getGoalLocation();

    Cell getCell(int row, int column);

    String toString();
}
