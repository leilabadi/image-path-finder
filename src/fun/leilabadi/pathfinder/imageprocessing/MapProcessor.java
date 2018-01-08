package fun.leilabadi.pathfinder.imageprocessing;

import fun.leilabadi.pathfinder.imageprocessing.filters.*;
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
    private final FilterFunction redFilter = new RedFilter();
    private final FilterFunction greenFilter = new GreenFilter();
    private final FilterFunction blueFilter = new BlueFilter();
    private final FilterFunction grayFilter = new GrayFilter();
    private Filters filters;

    public void processMap() {
        MarvinImage imageOriginal = MarvinImageIO.loadImage(imagePath);

        final List<MarvinImage> imageResults = new ArrayList<>();
        MarvinImage image = imageOriginal.clone();
        imageResults.add(processObjects(image));

        createFilters(imageOriginal);
        imageResults.add(processObjects(filters.getRed().image));
        imageResults.add(processObjects(filters.getGreen().image));
        imageResults.add(processObjects(filters.getBlue().image));
        imageResults.add(processObjects(filters.getGray().image));

        BackgroundRemover backgroundRemover = new BackgroundRemover(imageOriginal);
        image = backgroundRemover.removeBackground();
        imageResults.add(processObjects(image));

        image = mergePhotos(imageResults);

        //Use Floodfill segmention to get image segments
        /*mapSegments = floodfillSegmentation(image);
        for (MarvinSegment seg : mapSegments) {
            image.drawRect(seg.x1, seg.y1, seg.width, seg.height, Color.yellow);
        }*/
        processedMap = image;
    }

    public MapProcessor(String imagePath) {
        this.imagePath = imagePath;
    }

    public MarvinImage getProcessedMap() {
        return processedMap;
    }

    public MarvinSegment[] getMapSegments() {
        return mapSegments;
    }

    public Filters getFilters() {
        return filters;
    }


    private void createFilters(MarvinImage image) {
        final int w = image.getWidth();
        final int h = image.getHeight();
        filters = new Filters(w, h);

        int r, g, b;
        for (int y = 0; y < image.getHeight(); y++) {
            for (int x = 0; x < image.getWidth(); x++) {
                r = image.getIntComponent0(x, y);
                g = image.getIntComponent1(x, y);
                b = image.getIntComponent2(x, y);

                //Change pixels with filtered color to transparent based on the filter
                if (redFilter.filter(r, g, b)) {
                    filters.getRed().image.setBinaryColor(x, y, true);
                    filters.getRed().activePixelCount++;
                } else {
                    filters.getRed().image.setBinaryColor(x, y, false);
                }

                if (greenFilter.filter(r, g, b)) {
                    filters.getGreen().image.setBinaryColor(x, y, true);
                    filters.getGreen().activePixelCount++;
                } else {
                    filters.getGreen().image.setBinaryColor(x, y, false);
                }

                if (blueFilter.filter(r, g, b)) {
                    filters.getBlue().image.setBinaryColor(x, y, true);
                    filters.getBlue().activePixelCount++;
                } else {
                    filters.getBlue().image.setBinaryColor(x, y, false);
                }

                if (grayFilter.filter(r, g, b)) {
                    filters.getGray().image.setBinaryColor(x, y, true);
                    filters.getGray().activePixelCount++;
                } else {
                    filters.getGray().image.setBinaryColor(x, y, false);
                }
            }
        }
    }

    private MarvinImage processObjects(MarvinImage image) {

        MarvinImage binary;
        if (image.getColorModel() == MarvinImage.COLOR_MODEL_BINARY)
            binary = image;
        else {
            //Use threshold to separate foreground and background.
            binary = MarvinColorModelConverter.rgbToBinary(image, Constants.BINARY_MID_THRESHOLD);
        }

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
}
