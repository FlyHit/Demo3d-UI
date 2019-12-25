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

public class RLink extends RWidget {
    private String text;
    private String href;
    private String[] out;
    private int width;
    private Rectangle area;
    private List<SelectionListener> listeners = new ArrayList();

    public RLink(RMerger parent) {
        super(parent, 0);
        parent.addWidget(this);
    }

    public String getText() {
        return this.text;
    }

    public void setText(String text) {
        this.text = text;
        this.out = this.parseText(text);
        this.parent.updateBounds();
        this.parent.redraw();
        this.parent.getParent().layout(false);
    }

    public void addSelectionListener(SelectionListener listener) {
        if (!this.listeners.contains(listener)) {
            this.listeners.add(listener);
        }
    }

    public void removeSelectionListener(SelectionListener listener) {
        this.listeners.remove(listener);
    }

    protected void setBounds(Rectangle bounds) {
        super.setBounds(bounds);
        if (this.area != null) {
            Rectangle var10000 = this.area;
            var10000.x += bounds.x;
            this.area.y = bounds.y;
            this.area.height = bounds.height;
        }

    }

    protected Rectangle getArea() {
        return this.area;
    }

    protected int getWidth() {
        return this.width;
    }

    protected void onClick(MouseEvent event) {
        Event e = new Event();
        e.widget = event.widget;
        e.x = event.x;
        e.y = event.y;
        SelectionEvent selectionEvent = new SelectionEvent(e);
        selectionEvent.text = this.href;
        Iterator var5 = this.listeners.iterator();

        while(var5.hasNext()) {
            SelectionListener listener = (SelectionListener)var5.next();
            listener.widgetSelected(selectionEvent);
        }

    }

    protected void drawWidget(GC gc) {
        if (this.out != null) {
            int x = this.bounds.x;
            int y = this.bounds.y + 2;
            gc.setForeground(this.enabled ? RUtils.FONT_COLOR : RUtils.FONT_DISABLE);
            gc.drawString(this.out[0], x, y, true);
            gc.setForeground(this.enabled ? RUtils.FONT_CLICKED : RUtils.FONT_DISABLE);
            gc.drawString(this.out[1], this.area.x, y, true);
            int descent = gc.getFontMetrics().getDescent();
            int lineY = y + gc.stringExtent(this.out[1]).y - descent + 1;
            gc.drawLine(this.area.x, lineY, this.area.x + this.area.width, lineY);
            gc.setForeground(this.enabled ? RUtils.FONT_COLOR : RUtils.FONT_DISABLE);
            gc.drawString(this.out[2], this.area.x + this.area.width, y, true);
        }

    }

    private String[] parseText(String text) {
        this.href = null;
        this.width = 0;
        if (text == null) {
            return null;
        } else {
            int index = text.indexOf("<a");
            if (index < 0) {
                return new String[]{text, "", ""};
            } else {
                String[] out = new String[]{text.substring(0, index), null, null};
                int tmp = text.indexOf("href=\"", index);
                if (tmp > 0) {
                    this.href = text.substring(tmp + 6, text.indexOf("\"", tmp + 6));
                }

                index = text.indexOf(">", index) + 1;
                tmp = text.indexOf("</a>", index);
                out[1] = text.substring(index, tmp);
                out[2] = text.substring(tmp + 4);
                GC gc = new GC(this.parent);
                gc.setFont(RUtils.initFont);
                this.width = gc.stringExtent(out[0] + out[1] + out[2]).x;
                this.area = new Rectangle(gc.stringExtent(out[0]).x, 0, gc.stringExtent(out[1]).x, 0);
                gc.dispose();
                return out;
            }
        }
    }
}
