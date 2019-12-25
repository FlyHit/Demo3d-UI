package ribbon;

import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Rectangle;

public final class RExpander {
    private RTabFolder parent;
    private Rectangle bounds;
    private boolean hovered;
    private boolean clicked;
    private RToolTip toolTip;

    public RExpander(RTabFolder parent) {
        this.parent = parent;
        this.parent.setExpander(this);
    }

    public RTabFolder getParent() {
        return this.parent;
    }

    public RToolTip getToolTip() {
        return this.toolTip;
    }

    public void setToolTip(RToolTip toolTip) {
        this.toolTip = toolTip;
    }

    protected Rectangle getBounds() {
        return this.bounds;
    }

    protected void setBounds(Rectangle bounds) {
        this.bounds = bounds;
    }

    protected boolean getHovered() {
        return this.hovered;
    }

    protected void setHovered(boolean hovered) {
        this.hovered = hovered;
    }

    protected boolean getClicked() {
        return this.clicked;
    }

    protected void setClicked(boolean clicked) {
        this.clicked = clicked;
    }

    protected void drawWidget(GC gc) {
        int x = this.bounds.x;
        int y = this.bounds.y;
        int z = this.parent.getExpanded() ? 1 : -1;
        if (this.hovered) {
            gc.setForeground(!this.clicked ? RUtils.HOVER_HIGH_LIGHT : RUtils.CLICK_HIGH_LIGHT);
            gc.setBackground(!this.clicked ? RUtils.HOVER_COLOR : RUtils.CLICK_COLOR);
            gc.fillGradientRectangle(x + 1, y + 1, 20, 7, true);
            gc.setForeground(!this.clicked ? RUtils.HOVER_DARK_LIGHT : RUtils.CLICK_DARK_LIGHT);
            gc.fillGradientRectangle(x + 1, y + 8, 20, 13, true);
            gc.setForeground(!this.clicked ? RUtils.HOVER_BORDER : RUtils.CLICK_BORDER);
            gc.drawLine(x + 1, y, x + 20, y);
            gc.drawLine(x + 1, y + 1, x + 1, y + 1);
            gc.drawLine(x + 20, y + 1, x + 20, y + 1);
            gc.drawLine(x, y + 1, x, y + 20);
            gc.drawLine(x + 21, y + 1, x + 21, y + 20);
            gc.drawLine(x + 1, y + 20, x + 1, y + 20);
            gc.drawLine(x + 20, y + 20, x + 20, y + 20);
            gc.drawLine(x + 1, y + 21, x + 20, y + 21);
            if (!this.clicked) {
                gc.setForeground(RUtils.HOVER_INSIDE);
                gc.drawLine(x + 1, y + 2, x + 1, y + 19);
                gc.drawLine(x + 20, y + 2, x + 20, y + 19);
            }
        }

        x = this.bounds.x + 7;
        y = this.bounds.y + 11;
        gc.setForeground(RUtils.TAB_CLICKED);
        gc.drawLine(x + 4, y - 2 * z, x + 4, y - 2 * z);
        gc.drawLine(x + 3, y - 1 * z, x + 5, y - 1 * z);
        gc.drawLine(x + 2, y, x + 6, y);
        gc.drawLine(x + 1, y + 1 * z, x + 3, y + 1 * z);
        gc.drawLine(x + 5, y + 1 * z, x + 7, y + 1 * z);
        gc.drawLine(x + 1, y + 2 * z, x + 2, y + 2 * z);
        gc.drawLine(x + 6, y + 2 * z, x + 7, y + 2 * z);
        gc.setForeground(RUtils.EXPANDER_01);
        gc.drawLine(x + 4, y - 3 * z, x + 4, y - 3 * z);
        gc.drawLine(x + 3, y - 2 * z, x + 3, y - 2 * z);
        gc.drawLine(x + 5, y - 2 * z, x + 5, y - 2 * z);
        gc.drawLine(x + 2, y - 1 * z, x + 2, y - 1 * z);
        gc.drawLine(x + 6, y - 1 * z, x + 6, y - 1 * z);
        gc.drawLine(x + 1, y, x + 1, y);
        gc.drawLine(x + 7, y, x + 7, y);
        gc.drawLine(x, y + 1 * z, x, y + 2 * z);
        gc.drawLine(x + 8, y + 1 * z, x + 8, y + 2 * z);
        gc.drawLine(x + 4, y + 1 * z, x + 4, y + 1 * z);
        gc.drawLine(x + 3, y + 2 * z, x + 3, y + 2 * z);
        gc.drawLine(x + 5, y + 2 * z, x + 5, y + 2 * z);
        gc.drawLine(x + 1, y + 3 * z, x + 2, y + 3 * z);
        gc.drawLine(x + 6, y + 3 * z, x + 7, y + 3 * z);
        gc.setForeground(RUtils.EXPANDER_02);
        gc.drawLine(x + 3, y - 3 * z, x + 3, y - 3 * z);
        gc.drawLine(x + 5, y - 3 * z, x + 5, y - 3 * z);
        gc.drawLine(x + 2, y - 2 * z, x + 2, y - 2 * z);
        gc.drawLine(x + 6, y - 2 * z, x + 6, y - 2 * z);
        gc.drawLine(x + 1, y - 1 * z, x + 1, y - 1 * z);
        gc.drawLine(x + 7, y - 1 * z, x + 7, y - 1 * z);
        gc.drawLine(x, y, x, y);
        gc.drawLine(x + 8, y, x + 8, y);
        gc.drawLine(x + 4, y + 2 * z, x + 4, y + 2 * z);
        gc.drawLine(x, y + 3 * z, x, y + 3 * z);
        gc.drawLine(x + 3, y + 3 * z, x + 3, y + 3 * z);
        gc.drawLine(x + 5, y + 3 * z, x + 5, y + 3 * z);
        gc.drawLine(x + 8, y + 3 * z, x + 8, y + 3 * z);
    }
}

