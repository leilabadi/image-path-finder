package fun.leilabadi.pathfinder.graphic;

import fun.leilabadi.pathfinder.PacmanCell;
import fun.leilabadi.pathfinder.PacmanGrid;
import fun.leilabadi.pathfinder.PacmanSolution;
import fun.leilabadi.pathfinder.common.State;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.List;

/**
 * @author mosi
 * @version 1.0
 * @since 0.2
 */
public class GraphicGrid extends JComponent {
    private final Grid grid;
    private final PacmanGrid gridMap;
    private BufferedImage buffer;
    private double cellWidth;
    private double cellHeight;
    private Color defaultColor;
    private Paint defaultPaint;
    private Stroke defaultStroke;
    private GraphicCell dragTarget;
    private boolean selectionFlag;

    public GraphicGrid(PacmanGrid gridMap) {
        this.gridMap = gridMap;
        grid = new Grid(gridMap.getSize().getRows(), gridMap.getSize().getColumns());
        setCellSize();
        initGrid();

        addMouseMotionListener(new MouseMotionListener() {
            @Override
            public void mouseDragged(MouseEvent e) {
                GraphicCell cell = getPassingCell(e.getPoint());
                if ((cell != null) && (cell != dragTarget)) {
                    dragTarget = cell;
                    dragTarget.setSelected(selectionFlag);
                    repaint();
                }
            }

            @Override
            public void mouseMoved(MouseEvent e) {
            }
        });

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
            }

            @Override
            public void mousePressed(MouseEvent e) {
                GraphicCell cell = getPassingCell(e.getPoint());
                if (cell != null) {
                    dragTarget = cell;
                    selectionFlag = !dragTarget.isSelected();
                    dragTarget.setSelected(selectionFlag);
                    repaint();
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                dragTarget = null;
            }
        });

        KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(new KeyEventDispatcher() {
            @Override
            public boolean dispatchKeyEvent(KeyEvent e) {

                int key = e.getKeyCode();

                return false;
            }
        });

        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                buffer = null;
                setCellSize();
                initGrid();
            }
        });

        this.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    @Override
    protected void paintComponent(Graphics g) {
        if (buffer == null)
            buffer = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_RGB);

        Graphics2D g2 = (Graphics2D) buffer.getGraphics();

        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        saveGraphicState(g2);
        drawGrid(g2);

        g.drawImage(buffer, 0, 0, null);
    }

    @Override
    public void update(Graphics g) {
        paint(g);
    }


    private void initGrid() {
        Point2D.Double point1, point2;
        GraphicCell cell;
        for (int row = 0; row < grid.getRows(); row++) {
            for (int column = 0; column < grid.getColumns(); column++) {
                cell = grid.getCell(row, column);
                point1 = new Point2D.Double(column * cellWidth, row * cellHeight);
                point2 = new Point2D.Double(point1.getX() + cellWidth, point1.getY() + cellHeight);
                cell.setLocation(point1);
                cell.setOriginalPaint(new GradientPaint(point1, new Color(200, 200, 200), point2, new Color(223, 223, 223)));
                cell.setSelectedPaint(new Color(128, 128, 128));

                if (gridMap.getCell(row, column).getValue() == PacmanCell.WALL)
                    cell.setSelected(true);

                switch (gridMap.getCell(row, column).getValue()) {
                    case WALL:
                        cell.setSelected(true);
                        break;
                    case PACMAN:
                        cell.setOverridePaint(new Color(0, 0, 0));
                        break;
                    case FOOD:
                        cell.setOverridePaint(new Color(0, 255, 0));
                        break;
                }
            }
        }
    }

    private void drawGrid(Graphics2D graphics) {
        graphics.setStroke(new BasicStroke(1.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER));
        Rectangle2D.Double mainGrid = new Rectangle2D.Double(0, 0, getWidth(), getHeight());
        graphics.draw(mainGrid);
        restoreGraphicState(graphics);

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
        restoreGraphicState(graphics);

        Paint paint;
        GraphicCell cell;
        Rectangle2D.Double rectangle;
        for (int row = 0; row < grid.getRows(); row++) {
            for (int column = 0; column < grid.getColumns(); column++) {
                cell = grid.getCell(row, column);
                paint = cell.getOverridePaint() != null ? cell.getOverridePaint() :
                        (cell.isSelected() ? cell.getSelectedPaint() : cell.getOriginalPaint());
                graphics.setPaint(paint);
                rectangle = new Rectangle2D.Double(cell.getLocation().getX(), cell.getLocation().getY(), cellWidth - 1, cellHeight - 1);
                graphics.fill(rectangle);
            }
        }
        restoreGraphicState(graphics);
    }


    private void setCellSize() {
        cellWidth = getWidth() / (double) grid.getColumns();
        cellHeight = getHeight() / (double) grid.getRows();
    }

    private void saveGraphicState(Graphics2D graphics) {
        defaultColor = graphics.getColor();
        defaultPaint = graphics.getPaint();
        defaultStroke = graphics.getStroke();
    }

    private void restoreGraphicState(Graphics2D graphics) {
        graphics.setColor(defaultColor);
        graphics.setPaint(defaultPaint);
        graphics.setStroke(defaultStroke);
    }

    private GraphicCell getPassingCell(Point point) {
        int i = (int) (point.getX() / cellWidth);
        int j = (int) (point.getY() / cellHeight);
        return grid.getCell(i, j);
    }

    public void update(PacmanSolution solution) {
        GraphicCell cell;
        for (State state : solution.getVisitedNodes()) {
            cell = grid.getCell(state.getCurrentLocation().getRow(), state.getCurrentLocation().getColumn());
            if ((state.getCurrentLocation().distanceFrom(gridMap.getStartLocation()) != 0)
                    && (state.getCurrentLocation().distanceFrom(gridMap.getGoalLocation()) != 0))
                cell.setOverridePaint(new Color(255, 249, 111));
        }

        for (State state : solution.getExploredNodes()) {
            cell = grid.getCell(state.getCurrentLocation().getRow(), state.getCurrentLocation().getColumn());
            if ((state.getCurrentLocation().distanceFrom(gridMap.getStartLocation()) != 0)
                    && (state.getCurrentLocation().distanceFrom(gridMap.getGoalLocation()) != 0))
                cell.setOverridePaint(new Color(255, 152, 35));
        }

        repaint();
    }

    public void highlightPath(List<State> path) {
        GraphicCell cell;
        for (State state : path) {
            cell = grid.getCell(state.getCurrentLocation().getRow(), state.getCurrentLocation().getColumn());
            if ((state.getCurrentLocation().distanceFrom(gridMap.getStartLocation()) != 0)
                    && (state.getCurrentLocation().distanceFrom(gridMap.getGoalLocation()) != 0))
                cell.setOverridePaint(new Color(0, 0, 255));
        }

        repaint();
    }

    private class Grid {
        private final int rows;
        private final int columns;
        private final GraphicCell[][] cells;

        public Grid(int rows, int columns) {
            this.rows = rows;
            this.columns = columns;
            cells = new GraphicCell[rows][columns];
            for (int row = 0; row < rows; row++) {
                for (int column = 0; column < columns; column++) {
                    cells[row][column] = new GraphicCell(row, column);
                }
            }
        }

        public int getRows() {
            return rows;
        }

        public int getColumns() {
            return columns;
        }

        public GraphicCell getCell(int row, int column) {
            if (((0 <= row) && (row < rows)) && ((0 <= column) && (column < columns)))
                return cells[row][column];
            return null;
        }
    }
}
