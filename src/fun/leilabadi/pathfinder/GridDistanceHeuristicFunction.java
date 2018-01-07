package fun.leilabadi.pathfinder;

import fun.leilabadi.pathfinder.HeuristicFunction;
import fun.leilabadi.pathfinder.common.Location;

public class GridDistanceHeuristicFunction implements HeuristicFunction {

    @Override
    public int calculate(Location currentLocation, Location goalLocation) {
        return currentLocation.distanceFrom(goalLocation);
    }
}
