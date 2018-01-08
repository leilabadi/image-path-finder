package fun.leilabadi.pathfinder.imageprocessing.filters;

import java.awt.*;

public abstract class FilterFunction {
    private Color filterColor;

    FilterFunction(Color filterColor) {
        this.filterColor = filterColor;
    }

    public boolean filter(Color color) {
        return filter(color.getRed(), color.getGreen(), color.getBlue());
    }

    public abstract boolean filter(int r, int g, int b);
}
