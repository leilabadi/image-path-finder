package fun.leilabadi.pathfinder;

import fun.leilabadi.pathfinder.imageprocessing.MapProcessor;
import marvin.image.MarvinImage;
import marvin.io.MarvinImageIO;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;

public class App {
    private final static String imageName = "raw-map.jpg";
    private static String imagePath;
    private static String directoryPath;

    public static void main(String[] args) {

        initFiles();

        MapProcessor mapProcessor = new MapProcessor(imagePath);
        final MarvinImage map = mapProcessor.getProcessedMap();

        MarvinImageIO.saveImage(map, directoryPath + "processed-map.png");
    }

    private static void initFiles() {
        final ClassLoader classLoader = App.class.getClassLoader();
        final URL url = classLoader.getResource(imageName);
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
