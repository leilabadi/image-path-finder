package fun.leilabadi.pathfinder.imageprocessing;

import marvin.image.MarvinImage;

public class Filters {
    private ImageLayer red;
    private ImageLayer green;
    private ImageLayer blue;
    private ImageLayer justRed;
    private ImageLayer justGreen;
    private ImageLayer justBlue;
    private ImageLayer almostRed;
    private ImageLayer almostGreen;
    private ImageLayer almostBlue;
    private ImageLayer gray;

    public Filters(int width, int height) {
        this.red = new ImageLayer(new MarvinImage(width, height, MarvinImage.COLOR_MODEL_BINARY));
        this.green = new ImageLayer(new MarvinImage(width, height, MarvinImage.COLOR_MODEL_BINARY));
        this.blue = new ImageLayer(new MarvinImage(width, height, MarvinImage.COLOR_MODEL_BINARY));
        this.justRed = new ImageLayer(new MarvinImage(width, height, MarvinImage.COLOR_MODEL_BINARY));
        this.justGreen = new ImageLayer(new MarvinImage(width, height, MarvinImage.COLOR_MODEL_BINARY));
        this.justBlue = new ImageLayer(new MarvinImage(width, height, MarvinImage.COLOR_MODEL_BINARY));
        this.almostRed = new ImageLayer(new MarvinImage(width, height, MarvinImage.COLOR_MODEL_BINARY));
        this.almostGreen = new ImageLayer(new MarvinImage(width, height, MarvinImage.COLOR_MODEL_BINARY));
        this.almostBlue = new ImageLayer(new MarvinImage(width, height, MarvinImage.COLOR_MODEL_BINARY));
        this.gray = new ImageLayer(new MarvinImage(width, height, MarvinImage.COLOR_MODEL_BINARY));
    }

    public ImageLayer getRed() {
        return red;
    }

    public ImageLayer getGreen() {
        return green;
    }

    public ImageLayer getBlue() {
        return blue;
    }

    public ImageLayer getJustRed() {
        return justRed;
    }

    public ImageLayer getJustGreen() {
        return justGreen;
    }

    public ImageLayer getJustBlue() {
        return justBlue;
    }

    public ImageLayer getAlmostRed() {
        return almostRed;
    }

    public ImageLayer getAlmostGreen() {
        return almostGreen;
    }

    public ImageLayer getAlmostBlue() {
        return almostBlue;
    }

    public ImageLayer getGray() {
        return gray;
    }
}
