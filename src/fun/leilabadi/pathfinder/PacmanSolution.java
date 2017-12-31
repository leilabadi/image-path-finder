package fun.leilabadi.pathfinder;

import fun.leilabadi.pathfinder.common.Location;
import fun.leilabadi.pathfinder.common.Size;
import fun.leilabadi.pathfinder.common.State;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;

/**
 * @author mosi
 * @version 1.0
 * @since 0.2
 */
public class PacmanSolution {
    private final PacmanGrid gridMap;
    private final State initialState;
    private final List<State> visitedNodes = new ArrayList<>();
    private final List<State> exploredNodes = new ArrayList<>();
    private final ActionListener mapUpdated;

    public PacmanSolution(Size gridSize, Location startLocation, Location goalLocation, PacmanCell[][] cells, ActionListener mapUpdated) {
        this.gridMap = new PacmanGrid(gridSize, startLocation, goalLocation, cells);
        this.initialState = new State(startLocation);
        this.mapUpdated = mapUpdated;
    }

    public PacmanGrid getGridMap() {
        return gridMap;
    }


    public List<State> calculatePath() {
        Comparator<State> comparator = (o1, o2) -> o1.f(gridMap.getGoalLocation()) - o2.f(gridMap.getGoalLocation());

        final Queue<State> queue = new PriorityQueue<>(comparator);
        queue.offer(initialState);
        State state = initialState;
        visitedNodes.add(initialState);
        while (!queue.isEmpty() && !reachedGoal(state)) {
            state = queue.poll();
            exploredNodes.add(state);

            State stateNew;
            Location location = state.getCurrentLocation();
            Location locationNew;
            if (gridMap.getCell(location.getRow() - 1, location.getColumn()).getValue() != PacmanCell.WALL) {
                locationNew = state.getCurrentLocation().clone();
                locationNew.row--;
                stateNew = new State(locationNew, state);
                if (!hasVisited(stateNew)) {
                    queue.offer(stateNew);
                    visitedNodes.add(stateNew);
                }
            }
            if (gridMap.getCell(location.getRow(), location.getColumn() - 1).getValue() != PacmanCell.WALL) {
                locationNew = state.getCurrentLocation().clone();
                locationNew.column--;
                stateNew = new State(locationNew, state);
                if (!hasVisited(stateNew)) {
                    queue.offer(stateNew);
                    visitedNodes.add(stateNew);
                }
            }
            if (gridMap.getCell(location.getRow(), location.getColumn() + 1).getValue() != PacmanCell.WALL) {
                locationNew = state.getCurrentLocation().clone();
                locationNew.column++;
                stateNew = new State(locationNew, state);
                if (!hasVisited(stateNew)) {
                    queue.offer(stateNew);
                    visitedNodes.add(stateNew);
                }
            }
            if (gridMap.getCell(location.getRow() + 1, location.getColumn()).getValue() != PacmanCell.WALL) {
                locationNew = state.getCurrentLocation().clone();
                locationNew.row++;
                stateNew = new State(locationNew, state);
                if (!hasVisited(stateNew)) {
                    queue.offer(stateNew);
                    visitedNodes.add(stateNew);
                }
            }

            mapUpdated.actionPerformed(new ActionEvent(this, 0, ""));
        }

        return getPath(state);
    }

    private boolean reachedGoal(State state) {
        return state.getCurrentLocation().distanceFrom(gridMap.getGoalLocation()) == 0;
    }


    private boolean hasVisited(State state) {
        for (State item : visitedNodes) {
            if (item.equals(state))
                return true;
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

    public List<State> getVisitedNodes() {
        return visitedNodes;
    }

    public List<State> getExploredNodes() {
        return exploredNodes;
    }
}
