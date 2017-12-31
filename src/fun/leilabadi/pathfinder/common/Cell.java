package fun.leilabadi.pathfinder.common;

/**
 * @author mosi
 * @version 1.0
 * @since 0.2
 */
public class Cell<T> {
    private T value;
    private int _gValue;
    private int _hValue;
    private boolean visited;

    public Cell() {
    }

    public Cell(T value) {
        this.value = value;
    }


    public T getValue() {
        return value;
    }

    public int gValue() {
        return _gValue;
    }

    public int hValue() {
        return _hValue;
    }

    public boolean isVisited() {
        return visited;
    }
}
