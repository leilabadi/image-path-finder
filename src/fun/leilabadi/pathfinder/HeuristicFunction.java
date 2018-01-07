package fun.leilabadi.pathfinder;

import fun.leilabadi.pathfinder.common.Location;

public interface HeuristicFunction {

    int calculate(Location currentLocation, Location goalLocation);
}
