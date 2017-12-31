package fun.leilabadi.pathfinder.imageprocessing;

import marvin.image.MarvinImage;

import java.awt.*;

public class BackgroundRemover {
    private final static int MAX_COLOR_DIFFERENCE = (int) Math.sqrt(3 * Math.pow(Constants.BYTE_MAX, 2));

    private MarvinImage image;

    public BackgroundRemover(MarvinImage image) {
        this.image = image.clone();
    }

    public MarvinImage removeBackground() {
        MarvinImage result = image.clone();

        final Color backColor = findAverageColor();

        Color c;
        for (int y = 0; y < image.getHeight(); y++) {
            for (int x = 0; x < image.getWidth(); x++) {
                c = new Color(
                        image.getIntComponent0(x, y),
                        image.getIntComponent1(x, y),
                        image.getIntComponent2(x, y)
                );

                if (getDifferenceRatio(c, backColor) <= Constants.BACKGROUND_SELECT_RATIO) {
                    result.setIntColor(x, y, Constants.BYTE_MIN, Constants.BYTE_MIN, Constants.BYTE_MIN, Constants.BYTE_MIN);
                }
            }
        }

        return result;
    }

    public Color findAverageColor() {
        Color color;
        Color averageColor = new Color(0, 0, 0);
        int count = 0;
        for (int y = 0; y < image.getHeight(); y++) {
            for (int x = 0; x < image.getWidth(); x++) {
                color = new Color(
                        image.getIntComponent0(x, y),
                        image.getIntComponent1(x, y),
                        image.getIntComponent2(x, y)
                );
                averageColor = new Color(
                        getUpdatedAverage(averageColor.getRed(), count, color.getRed()),
                        getUpdatedAverage(averageColor.getGreen(), count, color.getGreen()),
                        getUpdatedAverage(averageColor.getBlue(), count, color.getBlue())
                );
            }
        }
        return averageColor;
    }

    private int getUpdatedAverage(int average, int count, int newValue) {
        return (count * average + newValue) / (count + 1);
    }

    private double getDifferenceRatio(Color c1, Color c2) {

        return Math.sqrt(Math.pow(c1.getRed() - c2.getRed(), 2)
                + Math.pow(c1.getGreen() - c2.getGreen(), 2)
                + Math.pow(c1.getBlue() - c2.getBlue(), 2)) / MAX_COLOR_DIFFERENCE;
    }
}
