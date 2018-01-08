package fun.leilabadi.pathfinder.ui;

import fun.leilabadi.pathfinder.PathFinder;
import fun.leilabadi.pathfinder.PathFinderBuilder;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;

public class ImagePathFinderApp {
    private final static String mapFileName = "raw-map.jpg";
    private final static String processedMapFileName = "processed-map.png";
    private static String imagePath;
    private static String directoryPath;

    public static void main(String[] args) {

        initFiles();

        PathFinderBuilder builder = new PathFinderBuilder();
        PathFinder pathFinder = builder.build(imagePath, directoryPath);
        new PathFinderFrame(pathFinder);
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
