package fun.leilabadi.pathfinder;

public class Cell {
    private CellType cellType;
    private boolean visited;
    private int _g;
    private int _h;

    public Cell(CellType cellType) {
        this.cellType = cellType;
    }

    public CellType getCellType() {
        return cellType;
    }

    public boolean hasVisited() {
        return visited;
    }

    public int g() {
        return _g;
    }

    public int h() {
        return _h;
    }
}
