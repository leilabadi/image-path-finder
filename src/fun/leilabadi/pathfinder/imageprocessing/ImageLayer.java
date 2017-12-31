package fun.leilabadi.pathfinder.imageprocessing;

import marvin.image.MarvinImage;

public class ImageLayer {
    protected MarvinImage image;
    protected int activePixelCount;

    public ImageLayer(MarvinImage image) {
        this.image = image;
    }

    public ImageLayer(MarvinImage image, int activePixelCount) {
        this.image = image;
        this.activePixelCount = activePixelCount;
    }

    public MarvinImage getImage() {
        return image;
    }

    public int getActivePixelCount() {
        return activePixelCount;
    }
}
