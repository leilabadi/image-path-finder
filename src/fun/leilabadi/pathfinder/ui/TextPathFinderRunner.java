package fun.leilabadi.pathfinder.ui;

import fun.leilabadi.pathfinder.HeuristicPathFinder;
import fun.leilabadi.pathfinder.PathFinder;
import fun.leilabadi.pathfinder.map.MapBuilder;
import fun.leilabadi.pathfinder.map.MapParser;
import fun.leilabadi.pathfinder.map.PathFinderMap;
import fun.leilabadi.pathfinder.map.TextMapBuilder;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;

public class TextPathFinderRunner {
    private final static String textMapFileName = "pacman-complex.txt";
    private String[] lines;

    public static void main(String[] args) {

        new TextPathFinderRunner().run();
    }

    private void run() {
        initTextMapData();

        MapBuilder builder = new TextMapBuilder(lines);
        MapParser parser = new MapParser();
        PathFinderMap map = parser.parseMap(builder);
        PathFinder pathFinder = new HeuristicPathFinder(map);
        new ReadOnlyPathFinderFrame(pathFinder);
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
}
