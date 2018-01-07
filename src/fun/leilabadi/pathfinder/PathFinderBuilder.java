package fun.leilabadi.pathfinder;

import fun.leilabadi.pathfinder.common.Location;
import fun.leilabadi.pathfinder.common.Size;

public class PathFinderBuilder {
    private String[] lines;

    public PathFinderBuilder(String[] lines) {
        this.lines = lines;
    }

    public PathFinder build() {
        int index = 0;
        while (lines[index].startsWith("#")) {
            index++;
        }

        String[] tempArray;
        tempArray = lines[index++].split(" ");
        int pacmanRow = Integer.parseInt(tempArray[0]);
        int pacmanColumn = Integer.parseInt(tempArray[1]);

        tempArray = lines[index++].split(" ");
        int foodRow = Integer.parseInt(tempArray[0]);
        int foodColumn = Integer.parseInt(tempArray[1]);

        tempArray = lines[index++].split(" ");
        int rows = Integer.parseInt(tempArray[0]);
        int columns = Integer.parseInt(tempArray[1]);

        String gridRows[] = new String[rows];
        for (int row = 0; row < rows; row++) {
            gridRows[row] = lines[index++];
        }

        Cell[][] cells = new Cell[rows][columns];
        for (int row = 0; row < rows; row++) {
            for (int column = 0; column < columns; column++) {
                cells[row][column] = new Cell(CellType.fromSymbol(gridRows[row].charAt(column)));
            }
        }

        Size gridSize = new Size(rows, columns);
        Location startLocation = new Location(pacmanRow, pacmanColumn);
        Location goalLocation = new Location(foodRow, foodColumn);
        return new HeuristicPathFinder(gridSize, startLocation, goalLocation, cells);
    }
}
