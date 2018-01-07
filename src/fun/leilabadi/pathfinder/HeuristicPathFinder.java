package fun.leilabadi.pathfinder;

import fun.leilabadi.pathfinder.common.Location;
import fun.leilabadi.pathfinder.common.Size;

import java.util.*;

public class HeuristicPathFinder implements PathFinder {
    private final Map map;
    private final State initialState;
    private final List<State> visitedNodes = new ArrayList<>();
    private final List<State> exploredNodes = new ArrayList<>();
    private final HeuristicFunction h;
    private FindProgressListener findProgressListener;

    HeuristicPathFinder(Size gridSize, Location startLocation, Location goalLocation, Cell[][] cells) {
        this.map = new Map(gridSize, startLocation, goalLocation, cells);
        this.h = new GridDistanceHeuristicFunction();
        this.initialState = new State(startLocation, null, 0, h);
    }

    @Override
    public Map getMap() {
        return map;
    }

    @Override
    public List<State> getVisitedNodes() {
        return visitedNodes;
    }

    @Override
    public List<State> getExploredNodes() {
        return exploredNodes;
    }

    @Override
    public void setFindProgressListener(FindProgressListener listener) {
        this.findProgressListener = listener;
    }

    @Override
    public List<State> findPath() {
        final Comparator<State> comparator = Comparator.comparingInt(o -> o.f(map.getGoalLocation()));

        final Queue<State> queue = new PriorityQueue<>(comparator);
        queue.offer(initialState);
        State state = initialState;
        visitedNodes.add(initialState);
        while (!queue.isEmpty() && !hasReachedGoal(state)) {
            state = queue.poll();
            exploredNodes.add(state);

            int g = state.g() + 1;
            State stateNew;
            Location location = state.getCurrentLocation();
            Location locationNew;
            if (map.getCell(location.getY() - 1, location.getX()).getCellType() != CellType.OBSTACLE) {
                locationNew = state.getCurrentLocation().clone();
                locationNew.y--;
                stateNew = new State(locationNew, state, g, h);
                if (!hasVisitedCell(stateNew)) {
                    queue.offer(stateNew);
                    visitedNodes.add(stateNew);
                }
            }
            if (map.getCell(location.getY(), location.getX() - 1).getCellType() != CellType.OBSTACLE) {
                locationNew = state.getCurrentLocation().clone();
                locationNew.x--;
                stateNew = new State(locationNew, state, g, h);
                if (!hasVisitedCell(stateNew)) {
                    queue.offer(stateNew);
                    visitedNodes.add(stateNew);
                }
            }
            if (map.getCell(location.getY(), location.getX() + 1).getCellType() != CellType.OBSTACLE) {
                locationNew = state.getCurrentLocation().clone();
                locationNew.x++;
                stateNew = new State(locationNew, state, g, h);
                if (!hasVisitedCell(stateNew)) {
                    queue.offer(stateNew);
                    visitedNodes.add(stateNew);
                }
            }
            if (map.getCell(location.getY() + 1, location.getX()).getCellType() != CellType.OBSTACLE) {
                locationNew = state.getCurrentLocation().clone();
                locationNew.y++;
                stateNew = new State(locationNew, state, g, h);
                if (!hasVisitedCell(stateNew)) {
                    queue.offer(stateNew);
                    visitedNodes.add(stateNew);
                }
            }

            if (findProgressListener != null) findProgressListener.onProgress(this);
        }

        return getPath(state);
    }

    private boolean hasReachedGoal(State state) {
        return state.getCurrentLocation().distanceFrom(map.getGoalLocation()) == 0;
    }

    private boolean hasVisitedCell(State state) {
        for (State item : visitedNodes) {
            if (item.getCurrentLocation().distanceFrom(state.getCurrentLocation()) == 0) return true;
        }
        return false;
    }

    private List<State> getPath(State state) {
        final Stack<State> reversePath = new Stack<>();
        while (state != null) {
            reversePath.add(state);
            state = state.getPreviousState();
        }

        final List<State> result = new ArrayList<>();
        while (!reversePath.empty())
            result.add(reversePath.pop());

        return result;
    }
}
