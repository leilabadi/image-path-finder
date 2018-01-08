package fun.leilabadi.pathfinder.imageprocessing.filters;

import fun.leilabadi.pathfinder.imageprocessing.Constants;

import java.awt.*;

public class JustBlueFilter extends FilterFunction {

    public JustBlueFilter() {
        super(Color.BLUE);
    }

    @Override
    public boolean filter(int r, int g, int b) {
        return b >= Constants.BYTE_MID;
    }
}
