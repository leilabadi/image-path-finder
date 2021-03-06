package fun.leilabadi.pathfinder.imageprocessing.filters;

import fun.leilabadi.pathfinder.imageprocessing.Constants;

import java.awt.*;

public class BlueFilter extends FilterFunction {

    public BlueFilter() {
        super(Color.BLUE);
    }

    @Override
    public boolean filter(int r, int g, int b) {
        return (b >= r * Constants.FILTER_SELECT_RATIO && b >= g * Constants.FILTER_SELECT_RATIO);
    }
}
