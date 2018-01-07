package fun.leilabadi.pathfinder;

import fun.leilabadi.pathfinder.common.Location;

public class State {
    private final Location currentLocation;
    private final State previousState;
    private final int _g;
    //heuristic function
    private final HeuristicFunction h;

    public State(Location currentLocation, State previousState, int g, HeuristicFunction h) {
        this.currentLocation = currentLocation;
        this.previousState = previousState;
        this._g = g;
        this.h = h;
    }

    public Location getCurrentLocation() {
        return currentLocation;
    }

    public State getPreviousState() {
        return previousState;
    }

    //cost function
    public int g() {
        return _g;
    }

    //fitness function
    public int f(Location goalLocation) {
        return g() + h.calculate(currentLocation, goalLocation);
    }
}
