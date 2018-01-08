package fun.leilabadi.pathfinder.imageprocessing.filters;

import fun.leilabadi.pathfinder.imageprocessing.Constants;

import java.awt.*;

public class JustRedFilter extends FilterFunction {

    public JustRedFilter() {
        super(Color.RED);
    }

    @Override
    public boolean filter(int r, int g, int b) {
        return r >= Constants.BYTE_MID;
    }
}
