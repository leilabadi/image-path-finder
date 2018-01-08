package fun.leilabadi.pathfinder.imageprocessing.filters;

import fun.leilabadi.pathfinder.imageprocessing.Constants;

import java.awt.*;

public class GrayFilter extends FilterFunction {

    public GrayFilter() {
        super(Color.GRAY);
    }

    @Override
    public boolean filter(int r, int g, int b) {
        return (Math.abs(min(r, g, b) - max(r, g, b)) <= Constants.GRAY_SELECT_RATIO * Constants.BYTE_MAX);
    }

    private int min(int a, int b, int c) {
        return Math.min(Math.min(a, b), c);
    }

    private int max(int a, int b, int c) {
        return Math.max(Math.max(a, b), c);
    }
}
