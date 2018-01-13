package fun.leilabadi.pathfinder.map;

import fun.leilabadi.pathfinder.Cell;
import fun.leilabadi.pathfinder.CellType;
import fun.leilabadi.pathfinder.common.Location;
import fun.leilabadi.pathfinder.common.Size;

public class TextMapBuilder extends MapBuilder {
    private String[] data;

    public TextMapBuilder(String[] data) {
        this.data = data;
    }

    @Override
    public void build() {
        int index = 0;
        while (data[index].startsWith("#")) {
            index++;
        }

        String[] tempArray;
        tempArray = data[index++].split(" ");
        int pacmanRow = Integer.parseInt(tempArray[0]);
        int pacmanColumn = Integer.parseInt(tempArray[1]);

        tempArray = data[index++].split(" ");
        int foodRow = Integer.parseInt(tempArray[0]);
        int foodColumn = Integer.parseInt(tempArray[1]);

        tempArray = data[index++].split(" ");
        int rowCount = Integer.parseInt(tempArray[0]);
        int columnCount = Integer.parseInt(tempArray[1]);
        Size size = new Size(rowCount, columnCount);

        String gridRows[] = new String[rowCount];
        for (int row = 0; row < rowCount; row++) {
            gridRows[row] = data[index++];
        }

        Cell[][] cells = new Cell[rowCount][columnCount];
        for (int row = 0; row < rowCount; row++) {
            for (int column = 0; column < columnCount; column++) {
                cells[row][column] = new Cell(CellType.fromSymbol(gridRows[row].charAt(column)));
            }
        }

        Location startLocation = new Location(pacmanRow, pacmanColumn);
        Location goalLocation = new Location(foodRow, foodColumn);
        this.map = new GridMap(size, startLocation, goalLocation, cells);
    }
}
