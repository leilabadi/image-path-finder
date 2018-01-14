package fun.leilabadi.pathfinder.ui;

import fun.leilabadi.pathfinder.HeuristicPathFinder;
import fun.leilabadi.pathfinder.PathFinder;
import fun.leilabadi.pathfinder.map.ImageMapBuilder;
import fun.leilabadi.pathfinder.map.MapBuilder;
import fun.leilabadi.pathfinder.map.MapParser;
import fun.leilabadi.pathfinder.map.PathFinderMap;
import fun.leilabadi.pathfinder.test.MapProcessorApp;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;

public class ImagePathFinderRunner {
    private final static String imageMapFileName = "raw-map.jpg";
    protected String imagePath;
    protected String directoryPath;

    public static void main(String[] args) {

        new ImagePathFinderRunner().run();
    }

    private void run() {
        initImageMapData();

        MapBuilder builder = new ImageMapBuilder(imagePath, directoryPath);
        MapParser parser = new MapParser();
        PathFinderMap map = parser.parseMap(builder);
        PathFinder pathFinder = new HeuristicPathFinder(map);
        new ReadOnlyPathFinderFrame(pathFinder);
    }

    protected void initImageMapData() {
        final ClassLoader classLoader = MapProcessorApp.class.getClassLoader();
        final URL url = classLoader.getResource(imageMapFileName);
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
