package fun.leilabadi.pathfinder.map;

import fun.leilabadi.pathfinder.Cell;
import fun.leilabadi.pathfinder.CellType;
import fun.leilabadi.pathfinder.common.Location;
import fun.leilabadi.pathfinder.common.Size;
import fun.leilabadi.pathfinder.imageprocessing.MapProcessor;
import marvin.color.MarvinColorModelConverter;
import marvin.image.MarvinImage;
import marvin.image.MarvinSegment;

import java.util.ArrayList;

import static marvin.MarvinPluginCollection.floodfillSegmentation;

public class ImageMapBuilder extends MapBuilder {
    private final String imagePath;
    private final String directoryPath;//will be used when saving images

    public ImageMapBuilder(String imagePath, String directoryPath) {
        this.imagePath = imagePath;
        this.directoryPath = directoryPath;
    }

    @Override
    public void build() {
        MapProcessor mapProcessor = new MapProcessor(imagePath);
        mapProcessor.processMap();
        final MarvinImage map = mapProcessor.getFinalMap();

        Size size = new Size(map.getHeight(), map.getWidth());

        Cell[][] cells = new Cell[size.getRowCount()][size.getColumnCount()];
        for (int y = 0; y < size.getRowCount(); y++) {
            for (int x = 0; x < size.getColumnCount(); x++) {
                cells[y][x] = new Cell(map.getBinaryColor(x, y) ? CellType.OBSTACLE : CellType.EMPTY);
            }
        }

        MarvinImage filteredImage;
        MarvinSegment[] segments;
        filteredImage = mapProcessor.getFilters().getAlmostBlue().getImage();
        filteredImage = MarvinColorModelConverter.binaryToRgb(filteredImage);
        segments = floodfillSegmentation(filteredImage);
        MarvinSegment startSegment = getTargetSegment(map, segments);

        filteredImage = mapProcessor.getFilters().getAlmostRed().getImage();
        filteredImage = MarvinColorModelConverter.binaryToRgb(filteredImage);
        segments = floodfillSegmentation(filteredImage);
        MarvinSegment goalSegment = getTargetSegment(map, segments);

        Location startLocation = new Location(startSegment.y1, startSegment.x1);
        Location goalLocation = new Location(goalSegment.y1, goalSegment.x1);
        //TODO: decrease resolution when converting to ui grid
        this.map = new GridMap(size, startLocation, goalLocation, cells);
    }

    private MarvinSegment getTargetSegment(MarvinImage map, MarvinSegment[] segments) {
        java.util.List<MarvinSegment> list = new ArrayList<>();
        for (MarvinSegment seg : segments) {
            if (seg.width == map.getWidth() && seg.height == map.getHeight())
                continue;
            else
                list.add(seg);
        }
        if (list.size() != 1) throw new RuntimeException("Error identifying start/goal point.");
        return list.get(0);
    }
}
