package fun.leilabadi.pathfinder.imageprocessing.filters;

import fun.leilabadi.pathfinder.imageprocessing.Constants;

import java.awt.*;

public class AlmostColorFilter extends FilterFunction {

    public AlmostColorFilter(Color filterColor) {
        super(filterColor);
    }

    @Override
    public boolean filter(int r, int g, int b) {
        return (Math.abs(r - filterColor.getRed()) + Math.abs(g - filterColor.getGreen())
                + Math.abs(b - filterColor.getBlue())) <= Constants.COLOR_SELECT_RATIO * Constants.BYTE_MAX;
    }
}
