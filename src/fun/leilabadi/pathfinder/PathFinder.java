package fun.leilabadi.pathfinder;

import fun.leilabadi.pathfinder.map.PathFinderMap;

import java.util.List;

public interface PathFinder {

    PathFinderMap getMap();

    List<State> getVisitedNodes();

    List<State> getExploredNodes();

    List<State> findPath();

    void setFindProgressListener(FindProgressListener listener);
}
