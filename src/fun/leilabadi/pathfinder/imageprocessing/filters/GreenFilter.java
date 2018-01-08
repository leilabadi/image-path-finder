package fun.leilabadi.pathfinder.imageprocessing.filters;

import fun.leilabadi.pathfinder.imageprocessing.Constants;

import java.awt.*;

public class GreenFilter extends FilterFunction {

    public GreenFilter() {
        super(Color.GREEN);
    }

    @Override
    public boolean filter(int r, int g, int b) {
        return (g >= r * Constants.FILTER_SELECT_RATIO && g >= b * Constants.FILTER_SELECT_RATIO);
    }
}
