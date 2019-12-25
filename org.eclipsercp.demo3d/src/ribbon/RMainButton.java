package ribbon;

import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchWindow;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public final class RMainButton {
    private RTabFolder parent;
    private String text;
    private Rectangle bounds;
    private boolean hovered;
    private List<SelectionListener> listeners;
    private RMenu menu;
    private Control control;

    public RMainButton(RTabFolder parent, String text,Control control) {
        this.parent = parent;
        this.text = text;
        this.listeners = new ArrayList();
        this.parent.setMainButton(this);
        this.control=control;
    }

    public RTabFolder getParent() {
        return this.parent;
    }

    public String getText() {
        return this.text;
    }

    public void setText(String text) {
        this.text = text;
        this.parent.updateBounds();
        this.parent.getControl().redraw(0, 0, this.parent.getBounds().width, 27, false);
    }

    public void addSelectionListener(SelectionListener listener) {
        if (!this.listeners.contains(listener)) {
            this.listeners.add(listener);
        }
    }

    public void removeSelectionListener(SelectionListener listener) {
        this.listeners.remove(listener);
    }

    public RMenu getMenu() {
        return this.menu;
    }

    protected void setMenu(RMenu menu) {
        this.menu = menu;
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

        if (this.menu != null) {
            this.menu.setLocation(control.getShell().toDisplay(control.getLocation()));
            this.menu.setVisible(true);
        }
    }
}

