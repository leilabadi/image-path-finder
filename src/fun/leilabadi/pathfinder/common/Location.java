package fun.leilabadi.pathfinder.common;

public class Location implements Cloneable<Location> {
    public int y;
    public int x;

    public Location(int y, int x) {
        this.y = y;
        this.x = x;
    }

    public Integer getY() {
        return y;
    }

    public Integer getX() {
        return x;
    }

    public int distanceFrom(Location target) {
        return Math.abs(y - target.y) + Math.abs(x - target.x);
    }

    @Override
    public Location clone() {
        return new Location(y, x);
    }

    @Override
    public String toString() {
        return "(" + y + "," + x + ')';
    }
}
