package fun.leilabadi.pathfinder.imageprocessing;

import marvin.color.MarvinColorModelConverter;
import marvin.image.MarvinImage;
import marvin.image.MarvinSegment;
import marvin.io.MarvinImageIO;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import static marvin.MarvinPluginCollection.floodfillSegmentation;
import static marvin.MarvinPluginCollection.invertColors;

public class MapProcessor {
    private final String imagePath;
    private MarvinImage processedMap;
    private MarvinSegment[] mapSegments;

    public MapProcessor(String imagePath) {
        this.imagePath = imagePath;
    }

    public MarvinImage getProcessedMap() {
        return processedMap;
    }

    public MarvinSegment[] getMapSegments() {
        return mapSegments;
    }


    public void processMap() {
        MarvinImage imageOriginal = MarvinImageIO.loadImage(imagePath);

        final List<MarvinImage> imageResults = new ArrayList<>();
        MarvinImage image = imageOriginal.clone();
        imageResults.add(processObjects(image));

        final Filters filters = getFilters(imageOriginal);
        imageResults.add(processObjects(filters.red.image));
        imageResults.add(processObjects(filters.green.image));
        imageResults.add(processObjects(filters.blue.image));
        imageResults.add(processObjects(filters.gray.image));

        BackgroundRemover backgroundRemover = new BackgroundRemover(imageOriginal);
        image = backgroundRemover.removeBackground();
        imageResults.add(processObjects(image));

        image = mergePhotos(imageResults);

        //Use Floodfill segmention to get image segments
        mapSegments = floodfillSegmentation(image);
        for (MarvinSegment seg : mapSegments) {
            image.drawRect(seg.x1, seg.y1, seg.width, seg.height, Color.yellow);
        }
        processedMap = image;
    }

    private Filters getFilters(MarvinImage image) {
        final int w = image.getWidth();
        final int h = image.getHeight();
        final Filters filters = new Filters();

        filters.red = new ImageLayer(new MarvinImage(w, h));
        filters.green = new ImageLayer(new MarvinImage(w, h));
        filters.blue = new ImageLayer(new MarvinImage(w, h));
        filters.gray = new ImageLayer(new MarvinImage(w, h));

        Color c;
        for (int y = 0; y < image.getHeight(); y++) {
            for (int x = 0; x < image.getWidth(); x++) {
                c = new Color(
                        image.getIntComponent0(x, y),
                        image.getIntComponent1(x, y),
                        image.getIntComponent2(x, y)
                );

                //Change pixels with filtered color to transparent based on the filter
                if (c.getRed() >= c.getGreen() * Constants.FILTER_SELECT_RATIO
                        && c.getRed() >= c.getBlue() * Constants.FILTER_SELECT_RATIO) {
                    filters.red.image.setIntColor(x, y, Constants.BYTE_MAX, Constants.BYTE_MAX, Constants.BYTE_MAX, Constants.BYTE_MAX);
                    filters.red.activePixelCount++;
                } else {
                    filters.red.image.setIntColor(x, y, Constants.BYTE_MAX, Constants.BYTE_MIN, Constants.BYTE_MIN, Constants.BYTE_MIN);
                }

                if (c.getGreen() >= c.getRed() * Constants.FILTER_SELECT_RATIO
                        && c.getGreen() >= c.getBlue() * Constants.FILTER_SELECT_RATIO) {
                    filters.green.image.setIntColor(x, y, Constants.BYTE_MAX, Constants.BYTE_MAX, Constants.BYTE_MAX, Constants.BYTE_MAX);
                    filters.green.activePixelCount++;
                } else {
                    filters.green.image.setIntColor(x, y, Constants.BYTE_MAX, Constants.BYTE_MIN, Constants.BYTE_MIN, Constants.BYTE_MIN);
                }

                if (c.getBlue() >= c.getRed() * Constants.FILTER_SELECT_RATIO
                        && c.getBlue() >= c.getGreen() * Constants.FILTER_SELECT_RATIO) {
                    filters.blue.image.setIntColor(x, y, Constants.BYTE_MAX, Constants.BYTE_MAX, Constants.BYTE_MAX, Constants.BYTE_MAX);
                    filters.blue.activePixelCount++;
                } else {
                    filters.blue.image.setIntColor(x, y, Constants.BYTE_MAX, Constants.BYTE_MIN, Constants.BYTE_MIN, Constants.BYTE_MIN);
                }

                if (Math.abs(min(c.getRed(), c.getGreen(), c.getBlue())
                        - max(c.getRed(), c.getGreen(), c.getBlue())) <= Constants.GRAY_SELECT_RATIO * Constants.BYTE_MAX) {
                    filters.gray.image.setIntColor(x, y, Constants.BYTE_MAX, Constants.BYTE_MAX, Constants.BYTE_MAX, Constants.BYTE_MAX);
                    filters.gray.activePixelCount++;
                } else {
                    filters.gray.image.setIntColor(x, y, Constants.BYTE_MAX, Constants.BYTE_MIN, Constants.BYTE_MIN, Constants.BYTE_MIN);
                }
            }
        }

        return filters;
    }

    private MarvinImage processObjects(MarvinImage image) {
        //Use threshold to separate foreground and background.
        MarvinImage binary = MarvinColorModelConverter.rgbToBinary(image, Constants.BINARY_MID_THRESHOLD);

        boolean invertNeeded = invertNeeded(binary);

        //Morphological closing to group separated parts of the same object
        //morphologicalClosing(binary.clone(), binary, MarvinMath.getTrueMatrix(10, 10));

        image = MarvinColorModelConverter.binaryToRgb(binary);

        if (invertNeeded)
            invertColors(image);

        return image;
    }

    private boolean invertNeeded(MarvinImage binaryImage) {

        int falseCount = 0;
        int trueCount = 0;
        for (int y = 0; y < binaryImage.getHeight(); y++) {
            for (int x = 0; x < binaryImage.getWidth(); x++) {
                if (binaryImage.getBinaryColor(x, y))
                    trueCount++;
                else
                    falseCount++;
            }
        }

        return trueCount > falseCount;
    }

    private MarvinImage mergePhotos(List<MarvinImage> images) {

        if (images == null || images.size() == 0) return null;

        if (images.size() == 1) return images.get(0);

        int width = images.get(0).getWidth();
        int height = images.get(0).getHeight();
        for (MarvinImage img : images) {
            if (img.getWidth() != width || img.getHeight() != height)
                throw new IllegalArgumentException("All the passed images should have the same size.");
        }

        MarvinImage result = new MarvinImage(width, height);
        Color c;
        boolean containsObject;
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                containsObject = false;
                for (MarvinImage img : images) {
                    c = new Color(
                            img.getIntComponent0(x, y),
                            img.getIntComponent1(x, y),
                            img.getIntComponent2(x, y)
                    );
                    if (convertToGray(c) <= Constants.BINARY_MID_THRESHOLD) {
                        containsObject = true;
                    }
                }
                // if the pixel in any of the images, contains an object, set it to black in final image.
                if (containsObject)
                    result.setIntColor(x, y, Constants.BYTE_MAX, Constants.BYTE_MIN, Constants.BYTE_MIN, Constants.BYTE_MIN);
                else
                    result.setIntColor(x, y, Constants.BYTE_MAX, Constants.BYTE_MAX, Constants.BYTE_MAX, Constants.BYTE_MAX);
            }
        }
        return result;
    }

    private int convertToGray(Color color) {
        return (int) ((double) color.getRed() * 0.3D + (double) color.getGreen() * 0.59D + (double) color.getBlue() * 0.11D);
    }

    private int min(int a, int b, int c) {
        return Math.min(Math.min(a, b), c);
    }

    private int max(int a, int b, int c) {
        return Math.max(Math.max(a, b), c);
    }
}
