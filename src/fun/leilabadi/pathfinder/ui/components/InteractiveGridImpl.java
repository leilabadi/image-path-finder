package fun.leilabadi.pathfinder.ui.components;

import fun.leilabadi.pathfinder.common.Matrix;
import fun.leilabadi.pathfinder.common.MatrixElement;
import fun.leilabadi.pathfinder.common.UiMode;
import fun.leilabadi.pathfinder.config.UiConfigs;
import fun.leilabadi.pathfinder.ui.CellProgress;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

public class InteractiveGridImpl extends JComponent implements InteractiveGrid {
    private final Matrix matrix;
    private final Point2D.Double[][] cellLocations;
    private double cellWidth;
    private double cellHeight;
    private BufferedImage buffer;
    private int activationState = 1;//TODO: change this based on obstacle, start or goal button in UI
    private MatrixElement dragTargetCell;
    private UiMode uiMode;

    public InteractiveGridImpl(int rowCount, int columnCount, UiMode uiMode) {
        this.uiMode = uiMode;
        this.matrix = new Matrix(rowCount, columnCount);
        this.cellLocations = new Point2D.Double[rowCount][columnCount];
        init();
        registerListeners();

        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                buffer = null;
                init();
            }
        });
    }

    @Override
    public void setActivationState(int activationState) {
        this.activationState = activationState;
    }

    private void init() {
        cellWidth = getWidth() / (double) matrix.getColumnCount();
        cellHeight = getHeight() / (double) matrix.getRowCount();

        for (int y = 0; y < matrix.getRowCount(); y++) {
            for (int x = 0; x < matrix.getColumnCount(); x++) {
                cellLocations[y][x] = new Point2D.Double(x * cellWidth, y * cellHeight);
            }
        }
    }

    private void registerListeners() {
        if (uiMode == UiMode.EDIT) {

            this.setCursor(new Cursor(Cursor.HAND_CURSOR));

            addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                }

                @Override
                public void mousePressed(MouseEvent e) {
                    MatrixElement cell = getPassingCell(e.getPoint());
                    if (cell != null) {
                        dragTargetCell = cell;
                        dragTargetCell.setState(activationState);
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
                    MatrixElement cell = getPassingCell(e.getPoint());
                    if ((cell != null) && (cell != dragTargetCell)) {
                        dragTargetCell = cell;
                        dragTargetCell.setState(activationState);
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
        }
    }

    private void setGraphicDefaults(Graphics2D g) {
        g.setColor(UiConfigs.DEFAULT_COLOR);
        g.setPaint(UiConfigs.DEFAULT_COLOR);
        g.setStroke(UiConfigs.DEFAULT_STROKE);
    }

    private MatrixElement getPassingCell(Point point) {
        int x = (int) (point.getX() / cellWidth);
        int y = (int) (point.getY() / cellHeight);
        return matrix.getElement(x, y);
    }

    private void drawGrid(Graphics2D graphics) {
        setGraphicDefaults(graphics);

        Rectangle2D.Double rectangle = new Rectangle2D.Double(0, 0, getWidth(), getHeight());
        graphics.draw(rectangle);

        graphics.setColor(Color.black);
        Line2D line;
        for (double i = 0; i < getHeight(); i += getHeight() / (double) matrix.getRowCount()) {
            line = new Line2D.Double(0, i, getWidth(), i);
            graphics.draw(line);
        }
        for (double i = 0; i < getWidth(); i += getWidth() / (double) matrix.getColumnCount()) {
            line = new Line2D.Double(i, 0, i, getHeight());
            graphics.draw(line);
        }
        graphics.setColor(UiConfigs.DEFAULT_COLOR);

        Point2D.Double cellStart, cellEnd;
        MatrixElement element;
        for (int y = 0; y < matrix.getRowCount(); y++) {
            for (int x = 0; x < matrix.getColumnCount(); x++) {
                element = matrix.getElement(x, y);
                cellStart = cellLocations[y][x];
                cellEnd = new Point2D.Double(cellStart.getX() + cellWidth, cellStart.getY() + cellHeight);
                graphics.setPaint(getPaint(element.getState(), cellStart, cellEnd));
                rectangle = new Rectangle2D.Double(cellStart.getX(), cellStart.getY(), cellWidth - 1, cellHeight - 1);
                graphics.fill(rectangle);
            }
        }
    }

    //TODO: separate the interactivity logic from map display and update logic
    protected Paint getPaint(int state, Point2D.Double cellStart, Point2D.Double cellEnd) {
        Paint paint;

        switch (state) {
            case 0:
                paint = UiConfigs.DEFAULT_COLOR;
                break;
            case 1:
                paint = UiConfigs.DEFAULT_COLOR;
                break;
            case 2:
                paint = new GradientPaint(cellStart, UiConfigs.CELL_GRADIENT_COLOR1, cellEnd, UiConfigs.CELL_GRADIENT_COLOR2);
                break;
            case 3:
                paint = UiConfigs.CELL_START_COLOR;
                break;
            case 4:
                paint = UiConfigs.CELL_GOAL_COLOR;
                break;
            default:
                if (state == CellProgress.VISITED.getValue())
                    paint = UiConfigs.CELL_VISITED_COLOR;
                else if (state == CellProgress.EXPLORED.getValue())
                    paint = UiConfigs.CELL_EXPLORED_COLOR;
                else if (state == CellProgress.PATH.getValue())
                    paint = UiConfigs.CELL_PATH_COLOR;
                else throw new IllegalArgumentException("Invalid value for the state");
        }
        return paint;
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

    @Override
    public void loadData(int[][] data) {
        matrix.setData(data);
        repaint();
    }

    @Override
    public int[][] getData() {
        return matrix.getRawData();
    }
}
