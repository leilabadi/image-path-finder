package fun.leilabadi.pathfinder.map;

public abstract class MapBuilder {
    protected PathFinderMap map;

    public abstract void build();

    public PathFinderMap getResult() {
        return map;
    }
}
