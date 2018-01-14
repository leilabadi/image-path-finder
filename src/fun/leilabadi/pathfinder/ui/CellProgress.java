package fun.leilabadi.pathfinder.ui;

public enum CellProgress {
    VISITED((byte) 100),
    EXPLORED((byte) 101),
    PATH((byte) 102);

    private byte value;

    CellProgress(byte value) {
        this.value = value;
    }

    public byte getValue() {
        return value;
    }
}
