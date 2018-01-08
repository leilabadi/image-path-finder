package fun.leilabadi.pathfinder.imageprocessing.filters;

import fun.leilabadi.pathfinder.imageprocessing.Constants;

import java.awt.*;

public class RedFilter extends FilterFunction {

    public RedFilter() {
        super(Color.RED);
    }

    @Override
    public boolean filter(int r, int g, int b) {
        return (r >= g * Constants.FILTER_SELECT_RATIO && r >= b * Constants.FILTER_SELECT_RATIO);
    }
}
