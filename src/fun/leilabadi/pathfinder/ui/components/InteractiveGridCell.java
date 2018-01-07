package fun.leilabadi.pathfinder.ui.components;

import java.awt.*;
import java.awt.geom.Point2D;

public class InteractiveGridCell {
    private int x;
    private int y;
    private Point2D.Double location;
    private boolean selected;
    private Paint originalPaint;
    private Paint selectedPaint;
    private Paint overridePaint;

    public InteractiveGridCell(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
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
        return "(" + x + "," + y + ")";
    }
}
