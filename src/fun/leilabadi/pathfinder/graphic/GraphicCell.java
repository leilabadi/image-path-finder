package fun.leilabadi.pathfinder.graphic;

import java.awt.*;
import java.awt.geom.Point2D;

/**
 * @author mosi
 * @version 1.0
 * @since 0.2
 */
public class GraphicCell {
    private int rowIndex;
    private int columnIndex;
    private Point2D.Double location;
    private boolean selected;
    private Paint originalPaint;
    private Paint selectedPaint;
    private Paint overridePaint;

    public GraphicCell(int rowIndex, int columnIndex) {
        this.rowIndex = rowIndex;
        this.columnIndex = columnIndex;
    }

    public int getRowIndex() {
        return rowIndex;
    }

    public int getColumnIndex() {
        return columnIndex;
    }

    public Point2D.Double getLocation() {
        return location;
    }

    public void setLocation(Point2D.Double location) {
        this.location = location;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public void toggleSelected() {
        selected = !selected;
    }

    public Paint getOriginalPaint() {
        return originalPaint;
    }

    public void setOriginalPaint(Paint originalPaint) {
        this.originalPaint = originalPaint;
    }

    public Paint getSelectedPaint() {
        return selectedPaint;
    }

    public void setSelectedPaint(Paint selectedPaint) {
        this.selectedPaint = selectedPaint;
    }

    public Paint getOverridePaint() {
        return overridePaint;
    }

    public void setOverridePaint(Paint overridePaint) {
        this.overridePaint = overridePaint;
    }

    @Override
    public String toString() {
        return "(" + rowIndex + "," + columnIndex + ")";
    }
}
