package fun.leilabadi.pathfinder.ui;

import fun.leilabadi.pathfinder.Map;
import fun.leilabadi.pathfinder.State;
import fun.leilabadi.pathfinder.common.Location;
import fun.leilabadi.pathfinder.ui.components.InteractiveGrid;
import fun.leilabadi.pathfinder.ui.components.InteractiveGridCell;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.List;

public class PathFinderGrid extends JComponent {
    private final static BasicStroke DEFAULT_STROKE = new BasicStroke(1.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER);
    private final static Color DEFAULT_COLOR = new Color(255, 255, 255);
    private final static Color CELL_GRADIENT_COLOR1 = new Color(200, 200, 200);
    private final static Color CELL_GRADIENT_COLOR2 = new Color(223, 223, 223);
    private final static Color CELL_SELECTED_COLOR = new Color(128, 128, 128);
    private final static Color CELL_START_COLOR = new Color(0, 0, 255);
    private final static Color CELL_GOAL_COLOR = new Color(255, 0, 0);
    private final static Color CELL_VISITED_COLOR = new Color(255, 250, 111);
    private final static Color CELL_EXPLORED_COLOR = new Color(255, 150, 50);
    private final static Color CELL_PATH_COLOR = new Color(0, 255, 0);
    private final InteractiveGrid grid;
    private InteractiveGridCell dragTargetCell;
    private boolean dragTargetSelectState;
    private BufferedImage buffer;
    private double cellWidth;
    private double cellHeight;
    private final Map map;

    public PathFinderGrid(Map map) {
        this.map = map;
        this.grid = new InteractiveGrid(map.getSize().getRowCount(), map.getSize().getColumnCount());

        setCellSize();
        initGrid();
        registerListeners();

        this.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    public void updateProgress(List<State> visitedNodes, List<State> exploredNodes) {
        setOverridePaints(visitedNodes, CELL_VISITED_COLOR);
        setOverridePaints(exploredNodes, CELL_EXPLORED_COLOR);
        repaint();
    }

    public void highlightPath(List<State> path) {
        setOverridePaints(path, CELL_PATH_COLOR);
        repaint();
    }

    private void setCellSize() {
        cellWidth = getWidth() / (double) grid.getColumns();
        cellHeight = getHeight() / (double) grid.getRows();
    }

    private void initGrid() {
        Point2D.Double point1, point2;
        InteractiveGridCell cell;
        for (int y = 0; y < grid.getRows(); y++) {
            for (int x = 0; x < grid.getColumns(); x++) {
                cell = grid.getCell(x, y);
                point1 = new Point2D.Double(x * cellWidth, y * cellHeight);
                point2 = new Point2D.Double(point1.getX() + cellWidth, point1.getY() + cellHeight);
                cell.setLocation(point1);
                cell.setOriginalPaint(new GradientPaint(point1, CELL_GRADIENT_COLOR1, point2, CELL_GRADIENT_COLOR2));
                cell.setSelectedPaint(CELL_SELECTED_COLOR);

                switch (map.getCell(y, x).getCellType()) {
                    case OBSTACLE:
                        cell.setSelected(true);
                        break;
                    case START:
                        cell.setOverridePaint(CELL_START_COLOR);
                        break;
                    case GOAL:
                        cell.setOverridePaint(CELL_GOAL_COLOR);
                        break;
                }
            }
        }
    }

    private void registerListeners() {

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
            }

            @Override
            public void mousePressed(MouseEvent e) {
                InteractiveGridCell cell = getPassingCell(e.getPoint());
                if (cell != null) {
                    dragTargetCell = cell;
                    dragTargetSelectState = !dragTargetCell.isSelected();
                    dragTargetCell.setSelected(dragTargetSelectState);
                    repaint();
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                dragTargetCell = null;
            }
        });

        addMouseMotionListener(new MouseMotionListener() {
            @Override
            public void mouseDragged(MouseEvent e) {
                InteractiveGridCell cell = getPassingCell(e.getPoint());
                if ((cell != null) && (cell != dragTargetCell)) {
                    dragTargetCell = cell;
                    dragTargetCell.setSelected(dragTargetSelectState);
                    repaint();
                }
            }

            @Override
            public void mouseMoved(MouseEvent e) {
            }
        });

        KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(e -> {

            //int key = e.getKeyCode();
            return false;
        });

        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                buffer = null;
                setCellSize();
                initGrid();
            }
        });
    }

    private void setGraphicDefaults(Graphics2D g) {
        g.setColor(DEFAULT_COLOR);
        g.setPaint(DEFAULT_COLOR);
        g.setStroke(DEFAULT_STROKE);
    }

    private InteractiveGridCell getPassingCell(Point point) {
        int x = (int) (point.getX() / cellWidth);
        int y = (int) (point.getY() / cellHeight);
        return grid.getCell(x, y);
    }

    private void drawGrid(Graphics2D graphics) {
        setGraphicDefaults(graphics);

        Rectangle2D.Double rectangle = new Rectangle2D.Double(0, 0, getWidth(), getHeight());
        graphics.draw(rectangle);

        graphics.setColor(Color.black);
        Line2D line;
        for (double i = 0; i < getHeight(); i += getHeight() / (double) grid.getRows()) {
            line = new Line2D.Double(0, i, getWidth(), i);
            graphics.draw(line);
        }
        for (double i = 0; i < getWidth(); i += getWidth() / (double) grid.getColumns()) {
            line = new Line2D.Double(i, 0, i, getHeight());
            graphics.draw(line);
        }
        graphics.setColor(DEFAULT_COLOR);

        Paint paint;
        InteractiveGridCell cell;
        for (int y = 0; y < grid.getRows(); y++) {
            for (int x = 0; x < grid.getColumns(); x++) {
                cell = grid.getCell(x, y);
                paint = cell.getOverridePaint() != null ? cell.getOverridePaint() :
                        (cell.isSelected() ? cell.getSelectedPaint() : cell.getOriginalPaint());
                graphics.setPaint(paint);
                rectangle = new Rectangle2D.Double(cell.getLocation().getX(), cell.getLocation().getY(), cellWidth - 1, cellHeight - 1);
                graphics.fill(rectangle);
            }
        }
    }

    private void setOverridePaints(List<State> nodes, Color color) {
        Location loc;
        InteractiveGridCell cell;
        for (State state : nodes) {
            loc = state.getCurrentLocation();
            cell = grid.getCell(loc.getX(), loc.getY());
            //the cell is not start or goal cells
            if ((loc.distanceFrom(map.getStartLocation()) != 0) && (loc.distanceFrom(map.getGoalLocation()) != 0))
                cell.setOverridePaint(color);
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        if (buffer == null) buffer = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_RGB);

        Graphics2D g2 = (Graphics2D) buffer.getGraphics();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        drawGrid(g2);

        g.drawImage(buffer, 0, 0, null);
    }

    @Override
    public void update(Graphics g) {
        paint(g);
    }
}
