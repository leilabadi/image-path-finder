package fun.leilabadi.pathfinder.map;

public class MapParser {

    public PathFinderMap parseMap(MapBuilder builder) {
        builder.build();
        return builder.getResult();
    }
}
