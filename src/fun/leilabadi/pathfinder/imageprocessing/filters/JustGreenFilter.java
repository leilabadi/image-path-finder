package fun.leilabadi.pathfinder.imageprocessing.filters;

import fun.leilabadi.pathfinder.imageprocessing.Constants;

import java.awt.*;

public class JustGreenFilter extends FilterFunction {

    public JustGreenFilter() {
        super(Color.GREEN);
    }

    @Override
    public boolean filter(int r, int g, int b) {
        return g >= Constants.BYTE_MID;
    }
}
