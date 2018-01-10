package fun.leilabadi.pathfinder.common;

public class MatrixElement {
    private int x;
    private int y;
    private int state;

    public MatrixElement(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public MatrixElement(int x, int y, int state) {
        this.x = x;
        this.y = y;
        this.state = state;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }
}
