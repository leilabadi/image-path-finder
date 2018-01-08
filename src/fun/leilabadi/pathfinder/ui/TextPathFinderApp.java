package fun.leilabadi.pathfinder.ui;

import fun.leilabadi.pathfinder.PathFinder;
import fun.leilabadi.pathfinder.PathFinderBuilder;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;

public class TextPathFinderApp {
    private final static String mapFileName = "pacman-complex.txt";
    private static String[] lines;

    public static void main(String[] args) {

        readGridFile();

        PathFinderBuilder builder = new PathFinderBuilder();
        PathFinder pathFinder = builder.build(lines);
        new PathFinderFrame(pathFinder);
    }

    private static void readGridFile() {
        final ClassLoader classLoader = TextPathFinderApp.class.getClassLoader();
        final URL url = classLoader.getResource(mapFileName);
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
