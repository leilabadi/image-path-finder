package fun.leilabadi.pathfinder.ui;

import fun.leilabadi.pathfinder.imageprocessing.MapProcessor;
import marvin.image.MarvinImage;
import marvin.io.MarvinImageIO;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;

public class MapProcessorApp {
    private final static String mapFileName = "raw-map.jpg";
    private final static String processedMapFileName = "processed-map.png";
    private static String imagePath;
    private static String directoryPath;

    public static void main(String[] args) {
        initFiles();

        MapProcessor mapProcessor = new MapProcessor(imagePath);
        mapProcessor.processMap();
        final MarvinImage map = mapProcessor.getFinalMap();

        MarvinImageIO.saveImage(map, directoryPath + processedMapFileName);

        /*MarvinImageIO.saveImage(mapProcessor.getFilters().getRed().getImage(), directoryPath + "red.png");
        MarvinImageIO.saveImage(mapProcessor.getFilters().getGreen().getImage(), directoryPath + "green.png");
        MarvinImageIO.saveImage(mapProcessor.getFilters().getBlue().getImage(), directoryPath + "blue.png");
        MarvinImageIO.saveImage(mapProcessor.getFilters().getJustRed().getImage(), directoryPath + "just-red.png");
        MarvinImageIO.saveImage(mapProcessor.getFilters().getJustGreen().getImage(), directoryPath + "just-green.png");
        MarvinImageIO.saveImage(mapProcessor.getFilters().getJustBlue().getImage(), directoryPath + "just-blue.png");
        MarvinImageIO.saveImage(mapProcessor.getFilters().getAlmostRed().getImage(), directoryPath + "almost-red.png");
        MarvinImageIO.saveImage(mapProcessor.getFilters().getAlmostGreen().getImage(), directoryPath + "almost-green.png");
        MarvinImageIO.saveImage(mapProcessor.getFilters().getAlmostBlue().getImage(), directoryPath + "almost-blue.png");*/

        /*for (int i = 1; i < 7; i++) {
            MarvinImageIO.saveImage(mapProcessor.getProcessedImages().get(i), directoryPath + "image-" + i + ".png");
        }*/
    }

    private static void initFiles() {
        final ClassLoader classLoader = MapProcessorApp.class.getClassLoader();
        final URL url = classLoader.getResource(mapFileName);
        URI uri = null;
        try {
            if (url != null) uri = url.toURI();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        if (uri == null) throw new RuntimeException("Unable to get resource address.");

        imagePath = Paths.get(uri).toAbsolutePath().toString();
        directoryPath = Paths.get(uri).getParent().toAbsolutePath().toString() + "/";
    }
}
