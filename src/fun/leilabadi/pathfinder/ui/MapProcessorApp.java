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
        final MarvinImage map = mapProcessor.getProcessedMap();

        MarvinImageIO.saveImage(map, directoryPath + processedMapFileName);
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
