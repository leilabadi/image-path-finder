package fun.leilabadi.pathfinder;

/**
 * @author mosi
 * @version 1.0
 * @since 0.2
 */
public enum PacmanCell {
    EMPTY((byte) 1, '-'), WALL((byte) 2, '%'), PACMAN((byte) 3, 'P'), FOOD((byte) 4, '.');

    private byte value;
    private char symbol;

    PacmanCell(byte value, char symbol) {
        this.value = value;
        this.symbol = symbol;
    }

    public static PacmanCell fromSymbol(char symbol) {
        PacmanCell result;
        switch (symbol) {
            case '-':
                result = EMPTY;
                break;
            case '%':
                result = WALL;
                break;
            case 'P':
                result = PACMAN;
                break;
            case '.':
                result = FOOD;
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
