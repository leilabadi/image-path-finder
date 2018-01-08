package fun.leilabadi.pathfinder;

import fun.leilabadi.pathfinder.common.Location;
import fun.leilabadi.pathfinder.common.Size;
import fun.leilabadi.pathfinder.imageprocessing.Constants;
import fun.leilabadi.pathfinder.imageprocessing.MapProcessor;
import marvin.color.MarvinColorModelConverter;
import marvin.image.MarvinImage;

public class PathFinderBuilder {

    public PathFinder build(String[] mapLines) {
        int index = 0;
        while (mapLines[index].startsWith("#")) {
            index++;
        }

        String[] tempArray;
        tempArray = mapLines[index++].split(" ");
        int pacmanRow = Integer.parseInt(tempArray[0]);
        int pacmanColumn = Integer.parseInt(tempArray[1]);

        tempArray = mapLines[index++].split(" ");
        int foodRow = Integer.parseInt(tempArray[0]);
        int foodColumn = Integer.parseInt(tempArray[1]);

        tempArray = mapLines[index++].split(" ");
        int rowCount = Integer.parseInt(tempArray[0]);
        int columnCount = Integer.parseInt(tempArray[1]);
        Size size = new Size(rowCount, columnCount);

        String gridRows[] = new String[rowCount];
        for (int row = 0; row < rowCount; row++) {
            gridRows[row] = mapLines[index++];
        }

        Cell[][] cells = new Cell[rowCount][columnCount];
        for (int row = 0; row < rowCount; row++) {
            for (int column = 0; column < columnCount; column++) {
                cells[row][column] = new Cell(CellType.fromSymbol(gridRows[row].charAt(column)));
            }
        }

        Location startLocation = new Location(pacmanRow, pacmanColumn);
        Location goalLocation = new Location(foodRow, foodColumn);
        return new HeuristicPathFinder(size, startLocation, goalLocation, cells);
    }

    public PathFinder build(String imagePath, String directoryPath) {

        MapProcessor mapProcessor = new MapProcessor(imagePath);
        mapProcessor.processMap();
        final MarvinImage map = mapProcessor.getFinalMap();

        Size size = new Size(map.getHeight(), map.getWidth());

        MarvinImage binary = MarvinColorModelConverter.rgbToBinary(map, Constants.BYTE_MID);

        Cell[][] cells = new Cell[size.getRowCount()][size.getColumnCount()];
        for (int y = 0; y < size.getRowCount(); y++) {
            for (int x = 0; x < size.getColumnCount(); x++) {
                cells[y][x] = new Cell(binary.getBinaryColor(x, y) ? CellType.OBSTACLE : CellType.EMPTY);
            }
        }

        //TODO: find start and goal and decrease resolution when converting to ui grid

        Location startLocation = new Location(1, 1);
        Location goalLocation = new Location(2, 2);
        return new HeuristicPathFinder(size, startLocation, goalLocation, cells);
    }
}
