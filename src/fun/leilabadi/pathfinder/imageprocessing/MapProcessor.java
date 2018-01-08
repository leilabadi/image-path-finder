package fun.leilabadi.pathfinder.imageprocessing;

import fun.leilabadi.pathfinder.imageprocessing.filters.*;
import marvin.color.MarvinColorModelConverter;
import marvin.image.MarvinImage;
import marvin.image.MarvinSegment;
import marvin.io.MarvinImageIO;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import static marvin.MarvinPluginCollection.invertColors;

public class MapProcessor {
    private final String imagePath;
    private final FilterFunction redFilter = new RedFilter();
    private final FilterFunction greenFilter = new GreenFilter();
    private final FilterFunction blueFilter = new BlueFilter();
    private final FilterFunction justRedFilter = new JustRedFilter();
    private final FilterFunction justGreenFilter = new JustGreenFilter();
    private final FilterFunction justBlueFilter = new JustBlueFilter();
    private final FilterFunction almostRedFilter = new AlmostColorFilter(Color.RED);
    private final FilterFunction almostGreenFilter = new AlmostColorFilter(Color.GREEN);
    private final FilterFunction almostBlueFilter = new AlmostColorFilter(Color.BLUE);
    private final FilterFunction grayFilter = new GrayFilter();
    private final List<MarvinImage> processedImages = new ArrayList<>();
    private MarvinImage finalMap;
    private MarvinSegment[] mapSegments;
    private Filters filters;

    public MapProcessor(String imagePath) {
        this.imagePath = imagePath;
    }

    public List<MarvinImage> getProcessedImages() {
        return processedImages;
    }

    public MarvinImage getFinalMap() {
        return finalMap;
    }

    public MarvinSegment[] getMapSegments() {
        return mapSegments;
    }

    public Filters getFilters() {
        return filters;
    }


    public void processMap() {
        MarvinImage imageOriginal = MarvinImageIO.loadImage(imagePath);

        MarvinImage image = imageOriginal.clone();
        processedImages.add(processObjects(image));

        createFilters(imageOriginal);
        processedImages.add(processObjects(filters.getRed().image));
        processedImages.add(processObjects(filters.getGreen().image));
        processedImages.add(processObjects(filters.getBlue().image));
        processedImages.add(processObjects(filters.getJustRed().image));
        processedImages.add(processObjects(filters.getJustGreen().image));
        processedImages.add(processObjects(filters.getJustBlue().image));
        processedImages.add(processObjects(filters.getAlmostRed().image));
        processedImages.add(processObjects(filters.getAlmostGreen().image));
        processedImages.add(processObjects(filters.getAlmostBlue().image));
        processedImages.add(processObjects(filters.getGray().image));

        BackgroundRemover backgroundRemover = new BackgroundRemover(imageOriginal);
        image = backgroundRemover.removeBackground();
        processedImages.add(processObjects(image));

        image = mergePhotos(processedImages);

        //Use Floodfill segmention to get image segments
        /*mapSegments = floodfillSegmentation(image);
        for (MarvinSegment seg : mapSegments) {
            image.drawRect(seg.x1, seg.y1, seg.width, seg.height, Color.yellow);
        }*/
        finalMap = image;
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

                if (justRedFilter.filter(r, g, b)) {
                    filters.getJustRed().image.setBinaryColor(x, y, true);
                    filters.getJustRed().activePixelCount++;
                } else {
                    filters.getJustRed().image.setBinaryColor(x, y, false);
                }

                if (justGreenFilter.filter(r, g, b)) {
                    filters.getJustGreen().image.setBinaryColor(x, y, true);
                    filters.getJustGreen().activePixelCount++;
                } else {
                    filters.getJustGreen().image.setBinaryColor(x, y, false);
                }

                if (justBlueFilter.filter(r, g, b)) {
                    filters.getJustBlue().image.setBinaryColor(x, y, true);
                    filters.getJustBlue().activePixelCount++;
                } else {
                    filters.getJustBlue().image.setBinaryColor(x, y, false);
                }

                if (almostRedFilter.filter(r, g, b)) {
                    filters.getAlmostRed().image.setBinaryColor(x, y, true);
                    filters.getAlmostRed().activePixelCount++;
                } else {
                    filters.getAlmostRed().image.setBinaryColor(x, y, false);
                }

                if (almostGreenFilter.filter(r, g, b)) {
                    filters.getAlmostGreen().image.setBinaryColor(x, y, true);
                    filters.getAlmostGreen().activePixelCount++;
                } else {
                    filters.getAlmostGreen().image.setBinaryColor(x, y, false);
                }

                if (almostBlueFilter.filter(r, g, b)) {
                    filters.getAlmostBlue().image.setBinaryColor(x, y, true);
                    filters.getAlmostBlue().activePixelCount++;
                } else {
                    filters.getAlmostBlue().image.setBinaryColor(x, y, false);
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
            binary = MarvinColorModelConverter.rgbToBinary(image, Constants.BYTE_MID);
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

        MarvinImage result = new MarvinImage(width, height, MarvinImage.COLOR_MODEL_BINARY);
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
                    if (convertToGray(c) <= Constants.BYTE_MID) {
                        containsObject = true;
                    }
                }
                // if the pixel in any of the images, contains an object, set it to black in final image.
                if (containsObject)
                    result.setBinaryColor(x, y, true);
                else
                    result.setBinaryColor(x, y, false);
            }
        }
        return result;
    }

    private int convertToGray(Color color) {
        return (int) ((double) color.getRed() * 0.3D + (double) color.getGreen() * 0.59D + (double) color.getBlue() * 0.11D);
    }
}
