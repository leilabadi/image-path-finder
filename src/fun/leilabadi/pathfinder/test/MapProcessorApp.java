package fun.leilabadi.pathfinder.test;

import fun.leilabadi.pathfinder.imageprocessing.MapProcessor;
import fun.leilabadi.pathfinder.ui.ImagePathFinderRunner;
import marvin.image.MarvinImage;
import marvin.io.MarvinImageIO;

public class MapProcessorApp extends ImagePathFinderRunner {
    private final static String processedMapFileName = "processed-map.png";

    public static void main(String[] args) {
        new MapProcessorApp().run();
    }

    public void run() {
        initImageMapData();

        MapProcessor mapProcessor = new MapProcessor(imagePath);
        mapProcessor.processMap();
        final MarvinImage map = mapProcessor.getFinalMap();

        MarvinImageIO.saveImage(map, directoryPath + processedMapFileName);

        /*for (int i = 1; i < 7; i++) {
            MarvinImageIO.saveImage(mapProcessor.getProcessedImages().get(i), directoryPath + "image-" + i + ".png");
        }*/
    }
}
