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

public final class RHelpButton {
    private RTabFolder parent;
    private Rectangle bounds;
    private boolean hovered;
    private boolean clicked;
    private List<SelectionListener> listeners;
    private RToolTip toolTip;

    public RHelpButton(RTabFolder parent) {
        this.parent = parent;
        this.listeners = new ArrayList();
        this.parent.setHelpButton(this);
    }

    public RTabFolder getParent() {
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

        gc.setBackground(RUtils.HELP_01);
        gc.fillRectangle(x + 7, y + 10, 9, 6);
        gc.setBackground(RUtils.HELP_02);
        gc.fillRectangle(x + 7, y + 6, 8, 4);
        gc.fillRectangle(x + 6, y + 10, 5, 2);
        gc.fillRectangle(x + 7, y + 12, 1, 1);
        gc.setForeground(RUtils.HELP_03);
        gc.drawPoint(x + 15, y + 9);
        gc.drawPoint(x + 15, y + 12);
        gc.drawLine(x + 4, y + 10, x + 4, y + 11);
        gc.drawPoint(x + 10, y + 13);
        gc.drawPoint(x + 9, y + 15);
        gc.drawPoint(x + 12, y + 15);
        gc.setForeground(RUtils.HELP_04);
        gc.drawPoint(x + 9, y + 6);
        gc.drawPoint(x + 12, y + 6);
        gc.drawPoint(x + 13, y + 7);
        gc.drawLine(x + 8, y + 8, x + 11, y + 8);
        gc.drawPoint(x + 6, y + 9);
        gc.drawPoint(x + 12, y + 9);
        gc.drawPoint(x + 11, y + 10);
        gc.drawPoint(x + 6, y + 12);
        gc.setForeground(RUtils.HELP_05);
        gc.drawLine(x + 10, y + 4, x + 11, y + 4);
        gc.drawLine(x + 7, y + 5, x + 14, y + 5);
        gc.drawPoint(x + 15, y + 6);
        gc.drawPoint(x + 5, y + 7);
        gc.drawPoint(x + 4, y + 12);
        gc.drawPoint(x + 11, y + 13);
        gc.drawPoint(x + 5, y + 14);
        gc.drawPoint(x + 14, y + 14);
        gc.drawPoint(x + 6, y + 15);
        gc.setForeground(RUtils.HELP_06);
        gc.drawPoint(x + 12, y + 4);
        gc.drawPoint(x + 14, y + 7);
        gc.drawPoint(x + 4, y + 9);
        gc.drawPoint(x + 9, y + 9);
        gc.drawPoint(x + 10, y + 12);
        gc.drawPoint(x + 15, y + 13);
        gc.drawPoint(x + 7, y + 14);
        gc.drawPoint(x + 10, y + 14);
        gc.drawPoint(x + 8, y + 15);
        gc.drawPoint(x + 13, y + 15);
        gc.setForeground(RUtils.HELP_07);
        gc.drawPoint(x + 9, y + 4);
        gc.drawPoint(x + 6, y + 6);
        gc.drawPoint(x + 7, y + 7);
        gc.drawPoint(x + 9, y + 7);
        gc.drawPoint(x + 10, y + 8);
        gc.drawPoint(x + 12, y + 8);
        gc.drawPoint(x + 15, y + 8);
        gc.drawPoint(x + 12, y + 11);
        gc.drawPoint(x + 6, y + 13);
        gc.setForeground(RUtils.HELP_08);
        gc.drawLine(x + 8, y + 4, x + 8, y + 6);
        gc.drawLine(x + 13, y + 4, x + 13, y + 6);
        gc.drawLine(x + 4, y + 8, x + 6, y + 8);
        gc.drawLine(x + 4, y + 13, x + 5, y + 13);
        gc.drawPoint(x + 13, y + 10);
        gc.setForeground(RUtils.HELP_09);
        gc.drawPoint(x + 6, y + 5);
        gc.drawPoint(x + 15, y + 5);
        gc.drawPoint(x + 5, y + 6);
        gc.drawPoint(x + 16, y + 6);
        gc.drawPoint(x + 5, y + 15);
        gc.drawPoint(x + 16, y + 15);
        gc.drawPoint(x + 6, y + 16);
        gc.drawPoint(x + 15, y + 16);
        gc.setForeground(RUtils.HELP_10);
        gc.drawPoint(x + 7, y + 4);
        gc.drawPoint(x + 14, y + 4);
        gc.drawPoint(x + 4, y + 7);
        gc.drawPoint(x + 17, y + 7);
        gc.drawPoint(x + 4, y + 14);
        gc.drawPoint(x + 17, y + 14);
        gc.drawPoint(x + 7, y + 17);
        gc.drawPoint(x + 14, y + 17);
        gc.setForeground(RUtils.HELP_10);
        gc.drawPoint(x + 14, y + 6);
        gc.drawPoint(x + 12, y + 7);
        gc.drawPoint(x + 15, y + 7);
        gc.drawPoint(x + 6, y + 14);
        gc.drawPoint(x + 15, y + 14);
        gc.drawPoint(x + 7, y + 15);
        gc.drawPoint(x + 14, y + 15);
        gc.drawPoint(x + 9, y + 5);
        gc.drawPoint(x + 12, y + 5);
        gc.drawPoint(x + 7, y + 6);
        gc.drawPoint(x + 6, y + 7);
        gc.drawPoint(x + 10, y + 7);
        gc.drawPoint(x + 13, y + 8);
        gc.drawPoint(x + 5, y + 9);
        gc.drawPoint(x + 16, y + 9);
        gc.drawPoint(x + 12, y + 10);
        gc.drawPoint(x + 5, y + 12);
        gc.drawPoint(x + 16, y + 12);
        gc.drawPoint(x + 9, y + 16);
        gc.drawPoint(x + 12, y + 16);
        gc.setForeground(RUtils.HELP_12);
        gc.drawLine(x + 10, y + 5, x + 11, y + 5);
        gc.drawLine(x + 5, y + 10, x + 5, y + 11);
        gc.drawLine(x + 16, y + 10, x + 16, y + 11);
        gc.drawLine(x + 10, y + 16, x + 11, y + 16);
        gc.drawPoint(x + 11, y + 7);
        gc.drawPoint(x + 9, y + 8);
        gc.drawPoint(x + 13, y + 9);
        gc.drawLine(x + 11, y + 11, x + 11, y + 12);
        gc.drawPoint(x + 11, y + 14);
        gc.setForeground(RUtils.HELP_13);
        gc.drawLine(x + 16, y + 8, x + 17, y + 8);
        gc.drawLine(x + 16, y + 13, x + 17, y + 13);
        gc.drawLine(x + 8, y + 16, x + 8, y + 17);
        gc.drawLine(x + 13, y + 16, x + 13, y + 17);
        gc.setForeground(RUtils.HELP_14);
        gc.drawPoint(x + 16, y + 7);
        gc.drawLine(x + 17, y + 9, x + 17, y + 12);
        gc.drawPoint(x + 16, y + 14);
        gc.drawPoint(x + 15, y + 15);
        gc.drawPoint(x + 7, y + 16);
        gc.drawPoint(x + 14, y + 16);
        gc.drawLine(x + 9, y + 17, x + 12, y + 17);
        gc.setForeground(RUtils.HELP_15);
        gc.drawLine(x + 17, y + 10, x + 17, y + 11);
        gc.drawPoint(x + 10, y + 17);
    }
}
