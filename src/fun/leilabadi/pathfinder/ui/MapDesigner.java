package fun.leilabadi.pathfinder.ui;

import fun.leilabadi.pathfinder.HeuristicPathFinder;
import fun.leilabadi.pathfinder.PathFinder;
import fun.leilabadi.pathfinder.map.InteractiveMapBuilder;
import fun.leilabadi.pathfinder.map.MapBuilder;
import fun.leilabadi.pathfinder.map.MapParser;
import fun.leilabadi.pathfinder.map.PathFinderMap;

public class MapDesigner {

    public MapDesigner() {
        MapBuilder builder = new InteractiveMapBuilder((new int[][]{new int[]{1, 0}, new int[]{0, 1}}));
        MapParser parser = new MapParser();
        PathFinderMap map = parser.parseMap(builder);
        PathFinder pathFinder = new HeuristicPathFinder(map);
        new PathFinderFrame(pathFinder);
    }

    public static void main(String[] args) {
        new MapDesigner();
    }
}
