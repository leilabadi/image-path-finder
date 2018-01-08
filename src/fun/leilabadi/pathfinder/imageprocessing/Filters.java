package fun.leilabadi.pathfinder.imageprocessing;

import marvin.image.MarvinImage;

public class Filters {
    private ImageLayer red;
    private ImageLayer green;
    private ImageLayer blue;
    private ImageLayer gray;

    public Filters(int width, int height) {
        this.red = new ImageLayer(new MarvinImage(width, height, MarvinImage.COLOR_MODEL_BINARY));
        this.green = new ImageLayer(new MarvinImage(width, height, MarvinImage.COLOR_MODEL_BINARY));
        this.blue = new ImageLayer(new MarvinImage(width, height, MarvinImage.COLOR_MODEL_BINARY));
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

    public ImageLayer getGray() {
        return gray;
    }
}
