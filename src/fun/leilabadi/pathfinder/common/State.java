package fun.leilabadi.pathfinder.common;

/**
 * @author mosi
 * @version 1.0
 * @since 0.2
 */
public class State {
    private final Location currentLocation;
    private final State previousState;
    private int cost;

    public State(Location currentLocation) {
        this.currentLocation = currentLocation;
        this.previousState = null;
    }

    public State(Location currentLocation, State previousState) {
        this.currentLocation = currentLocation;
        this.previousState = previousState;
        this.cost = previousState.g() + 1;
    }

    public Location getCurrentLocation() {
        return currentLocation;
    }

    public State getPreviousState() {
        return previousState;
    }


    public int g() {
        return cost;
    }

    public int h(Location goalLocation) {
        return currentLocation.distanceFrom(goalLocation);
    }

    public int f(Location goalLocation) {
        return g() + h(goalLocation);
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        State state = (State) o;
        return currentLocation.distanceFrom(state.currentLocation) == 0;
    }

    @Override
    public int hashCode() {
        int result = currentLocation.getRow();
        result = 31 * result + currentLocation.getColumn();
        return result;
    }
}
