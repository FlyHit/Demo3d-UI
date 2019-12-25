package ribbon;

import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Event;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class RGroupButton {
    private RGroup parent;
    private Rectangle bounds;
    private boolean hovered;
    private boolean clicked;
    private List<SelectionListener> listeners;
    private RToolTip toolTip;

    public RGroupButton(RGroup parent) {
        this.parent = parent;
        this.listeners = new ArrayList();
        this.parent.setGroupButton(this);
    }

    public RGroup getParent() {
        return this.parent;
    }

    public void addSelectionListener(SelectionListener listener) {
        if (!this.listeners.contains(listener)) {
            this.listeners.add(listener);
        }
    }

    public void removeSelectionListener(SelectionListener listener) {
        this.listeners.remove(listener);
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

    protected void onClick(MouseEvent event) {
        Event e = new Event();
        e.widget = event.widget;
        e.x = event.x;
        e.y = event.y;
        SelectionEvent selectionEvent = new SelectionEvent(e);
        Iterator var5 = this.listeners.iterator();

        while(var5.hasNext()) {
            SelectionListener listener = (SelectionListener)var5.next();
            listener.widgetSelected(selectionEvent);
        }

    }

    protected void drawWidget(GC gc) {
        int x = this.bounds.x;
        int y = this.bounds.y;
        if (this.hovered) {
            gc.setForeground(!this.clicked ? RUtils.HOVER_HIGH_LIGHT : RUtils.CLICK_HIGH_LIGHT);
            gc.setBackground(!this.clicked ? RUtils.HOVER_COLOR : RUtils.CLICK_COLOR);
            gc.fillGradientRectangle(x + 1, y + 1, 13, 5, true);
            gc.setForeground(!this.clicked ? RUtils.HOVER_DARK_LIGHT : RUtils.CLICK_DARK_LIGHT);
            gc.fillGradientRectangle(x + 1, y + 6, 13, 7, true);
            gc.setForeground(!this.clicked ? RUtils.HOVER_BORDER : RUtils.CLICK_BORDER);
            gc.drawLine(x + 1, y, x + 14, y);
            gc.drawLine(x, y + 1, x, y + 13);
            gc.drawLine(x + 14, y, x + 14, y + 13);
            gc.drawLine(x, y + 13, x + 14, y + 13);
            gc.drawPoint(x + 1, y + 1);
        }

        gc.setForeground(RUtils.FONT_GROUP);
        gc.drawLine(x + 4, y + 4, x + 9, y + 4);
        gc.drawLine(x + 4, y + 5, x + 4, y + 9);
        gc.drawLine(x + 8, y + 8, x + 9, y + 8);
        gc.drawLine(x + 8, y + 9, x + 9, y + 9);
        gc.drawLine(x + 7, y + 10, x + 9, y + 10);
        gc.drawLine(x + 10, y + 7, x + 10, y + 10);
        gc.drawPoint(x + 7, y + 7);
    }
}

