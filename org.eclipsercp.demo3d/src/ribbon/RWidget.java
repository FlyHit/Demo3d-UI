package ribbon;

import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Rectangle;

public abstract class RWidget {
    protected RGroup parent;
    protected int style;
    protected boolean enabled = true;
    protected boolean visible = true;
    protected int indent;
    protected Rectangle bounds;
    protected RToolTip toolTip;
    protected RMenu menu;
    protected Object data;

    protected RWidget(RGroup parent, int style) {
        this.parent = parent;
        this.style = style;
    }

    protected RWidget(RMerger parent, int style) {
        this.parent = parent.getParent();
        this.style = style;
    }

    protected Rectangle getBounds() {
        return this.bounds;
    }

    protected void setBounds(Rectangle bounds) {
        this.bounds = bounds;
    }

    protected abstract void drawWidget(GC var1);

    public RGroup getParent() {
        return this.parent;
    }

    public int getStyle() {
        return this.style;
    }

    public boolean getEnabled() {
        return this.enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
        this.parent.redraw(this.bounds.x, this.bounds.y, this.bounds.width, this.bounds.height, false);
    }

    public boolean getVisible() {
        return this.visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
        this.parent.updateBounds();
        this.parent.redraw();
        this.parent.getParent().layout(false);
    }

    public int getIndent() {
        return this.indent;
    }

    public void setIndent(int indent) {
        this.indent = Math.abs(indent);
        this.parent.updateBounds();
        this.parent.redraw();
        this.parent.getParent().layout(false);
    }

    public RToolTip getToolTip() {
        return this.toolTip;
    }

    public void setToolTip(RToolTip toolTip) {
        this.toolTip = toolTip;
    }

    public RMenu getMenu() {
        return this.menu;
    }

    public void setMenu(RMenu menu) {
        if (this.menu != null) {
            this.menu.dispose();
        }

        this.menu = menu;
    }

    public Object getData() {
        return this.data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}

