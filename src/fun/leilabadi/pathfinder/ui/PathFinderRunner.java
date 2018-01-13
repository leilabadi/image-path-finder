package fun.leilabadi.pathfinder.ui;

import fun.leilabadi.pathfinder.HeuristicPathFinder;
import fun.leilabadi.pathfinder.PathFinder;
import fun.leilabadi.pathfinder.map.*;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;

public class PathFinderRunner {
    private final static String textMapFileName = "pacman-complex.txt";
    private final static String imageMapFileName = "raw-map.jpg";
    private String[] lines;
    private String imagePath;
    private String directoryPath;

    protected void runTextPathFinder() {
        initTextMapData();
        run(new TextMapBuilder(lines));
    }

    protected void runImagePathFinder() {
        initImageMapData();
        run(new ImageMapBuilder(imagePath, directoryPath));
    }

    private void run(MapBuilder builder) {
        MapParser parser = new MapParser();
        PathFinderMap map = parser.parseMap(builder);
        PathFinder pathFinder = new HeuristicPathFinder(map);
        new PathFinderFrame(pathFinder);
    }

    private void initTextMapData() {
        final ClassLoader classLoader = TextPathFinderRunner.class.getClassLoader();
        final URL url = classLoader.getResource(textMapFileName);
        URI uri = null;
        try {
            if (url != null) {
                uri = url.toURI();
                lines = Files.readAllLines(Paths.get(uri).toAbsolutePath()).toArray(new String[0]);
            }
        } catch (URISyntaxException | IOException e) {
            e.printStackTrace();
        }

        if (lines == null) throw new RuntimeException("Unable to read grid file.");
    }

    private void initImageMapData() {
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
