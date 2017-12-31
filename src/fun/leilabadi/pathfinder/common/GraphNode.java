package fun.leilabadi.pathfinder.common;

import java.util.ArrayList;
import java.util.List;

/**
 * @author mosi
 * @version 1.0
 * @since 0.2
 */
public class GraphNode {
    private final List<GraphNode> neighbors = new ArrayList<GraphNode>();
    private boolean visited;
    private int gValue;
    private int hValue;
}
