package fun.leilabadi.pathfinder.imageprocessing.filters;

import java.awt.*;

public abstract class FilterFunction {
    protected Color filterColor;

    FilterFunction(Color filterColor) {
        this.filterColor = filterColor;
    }

    public boolean filter(Color color) {
        return filter(color.getRed(), color.getGreen(), color.getBlue());
    }

    //TODO: find a way to filter based on any color
    public abstract boolean filter(int r, int g, int b);
}
