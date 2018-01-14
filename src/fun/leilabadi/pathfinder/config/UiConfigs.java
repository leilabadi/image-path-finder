package fun.leilabadi.pathfinder.config;

import java.awt.*;

public class UiConfigs {
    public final static BasicStroke DEFAULT_STROKE = new BasicStroke(1.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER);
    public final static Color DEFAULT_COLOR = new Color(255, 255, 255);
    public final static Color CELL_GRADIENT_COLOR1 = new Color(200, 200, 200);
    public final static Color CELL_GRADIENT_COLOR2 = new Color(223, 223, 223);
    public final static Color CELL_START_COLOR = new Color(0, 0, 255);
    public final static Color CELL_GOAL_COLOR = new Color(255, 0, 0);
    public final static Color CELL_VISITED_COLOR = new Color(255, 250, 111);
    public final static Color CELL_EXPLORED_COLOR = new Color(255, 150, 50);
    public final static Color CELL_PATH_COLOR = new Color(0, 255, 0);
    public final static int MIN_WINDOW_WIDTH = 700;
    public final static int MIN_WINDOW_HEIGHT = 550;
    public final static int EXPLORE_STEP_WAIT_MILLISECOND = 10;
}
