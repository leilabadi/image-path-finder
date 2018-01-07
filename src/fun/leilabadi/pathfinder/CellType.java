package fun.leilabadi.pathfinder;

public enum CellType {
    EMPTY((byte) 1, '-'), OBSTACLE((byte) 2, '%'), START((byte) 3, 'P'), GOAL((byte) 4, '.');

    private byte value;
    private char symbol;

    CellType(byte value, char symbol) {
        this.value = value;
        this.symbol = symbol;
    }

    public static CellType fromSymbol(char symbol) {
        CellType result;
        switch (symbol) {
            case '-':
                result = EMPTY;
                break;
            case '%':
                result = OBSTACLE;
                break;
            case 'P':
                result = START;
                break;
            case '.':
                result = GOAL;
                break;
            default:
                throw new IllegalArgumentException();
        }
        return result;
    }

    public byte getValue() {
        return value;
    }

    public char getSymbol() {
        return symbol;
    }

    @Override
    public String toString() {
        return Character.toString(symbol);
    }
}
