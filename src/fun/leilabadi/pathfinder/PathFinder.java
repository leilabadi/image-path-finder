package fun.leilabadi.pathfinder;

import java.awt.event.ActionListener;
import java.util.List;

public interface PathFinder {

    Map getMap();

    List<State> getVisitedNodes();

    List<State> getExploredNodes();

    List<State> findPath();

    void setFindProgressListener(FindProgressListener listener);
}
