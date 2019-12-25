package ribbon;

import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Pattern;
import org.eclipse.swt.widgets.Event;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public final class RCheckBox extends RWidget {
    private String text;
    private boolean selected;
    private List<SelectionListener> listeners = new ArrayList();

    public RCheckBox(RMerger parent) {
        super(parent, 0);
        parent.addWidget(this);
    }

    public String getText() {
        return this.text;
    }

    public void setText(String text) {
        this.text = text;
        this.parent.updateBounds();
        this.parent.redraw();
        this.parent.getParent().layout(false);
    }

    public boolean getSelection() {
        return this.selected;
    }

    public void setSelection(boolean selected) {
        this.selected = selected;
        this.parent.redraw(this.bounds.x, this.bounds.y, 13, this.bounds.height, false);
    }

    public void addSelectionListener(SelectionListener listener) {
        if (!this.listeners.contains(listener)) {
            this.listeners.add(listener);
        }
    }

    public void removeSelectionListener(SelectionListener listener) {
        this.listeners.remove(listener);
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
        boolean isHover = this == this.parent.getHovered();
        boolean isClick = this == this.parent.getClicked();
        int x = this.bounds.x;
        int y = this.bounds.y + 4;
        if (isHover && !isClick) {
            gc.setForeground(RUtils.HOVER_BORDER);
        } else if (isHover && isClick) {
            gc.setForeground(RUtils.CLICK_BORDER);
        } else {
            gc.setForeground(RUtils.LINE_COLOR);
        }

        gc.drawRectangle(x, y, 12, 12);
        gc.setForeground(isHover ? RUtils.HOVER_INSIDE : RUtils.LINE_INSIDE);
        gc.drawRectangle(x + 1, y + 1, 10, 10);
        Color border_tl;
        Color border_br;
        Color inside_tl;
        Color inside_br;
        if (isHover) {
            border_tl = RUtils.BOX_TL_02;
            border_br = RUtils.BOX_BR_02;
            inside_tl = RUtils.BOX_TL_05;
            inside_br = RUtils.BOX_BR_05;
        } else if (this.enabled) {
            border_tl = RUtils.BOX_TL_01;
            border_br = RUtils.BOX_BR_01;
            inside_tl = RUtils.BOX_TL_04;
            inside_br = RUtils.BOX_BR_04;
        } else {
            border_tl = RUtils.BOX_TL_03;
            border_br = RUtils.BOX_BR_03;
            inside_tl = RUtils.BOX_TL_06;
            inside_br = RUtils.BOX_BR_06;
        }

        Pattern pattern = new Pattern(gc.getDevice(), (float)(x + 2), (float)(y + 2), (float)(x + 2 + 9), (float)(y + 2 + 9), border_tl, border_br);
        gc.setForegroundPattern(pattern);
        gc.setBackgroundPattern(pattern);
        gc.fillRectangle(x + 2, y + 2, 9, 9);
        pattern = new Pattern(gc.getDevice(), (float)(x + 3), (float)(y + 3), (float)(x + 3 + 7), (float)(y + 3 + 7), inside_tl, inside_br);
        gc.setForegroundPattern(pattern);
        gc.setBackgroundPattern(pattern);
        gc.fillRectangle(x + 3, y + 3, 7, 7);
        pattern.dispose();
        gc.setForegroundPattern((Pattern)null);
        gc.setBackgroundPattern((Pattern)null);
        gc.setAdvanced(false);
        if (this.selected) {
            gc.setForeground(this.enabled ? RUtils.MARK_01 : RUtils.FONT_DISABLE);
            gc.drawLine(x + 4, y + 7, x + 4, y + 8);
            gc.drawLine(x + 5, y + 8, x + 5, y + 9);
            gc.drawLine(x + 6, y + 8, x + 6, y + 9);
            gc.drawLine(x + 7, y + 6, x + 7, y + 7);
            gc.drawLine(x + 8, y + 3, x + 8, y + 5);
            gc.drawPoint(x + 9, y + 3);
            gc.setForeground(this.enabled ? RUtils.MARK_02 : RUtils.FONT_DISABLE);
            gc.drawPoint(x + 3, y + 7);
            gc.drawPoint(x + 6, y + 7);
            gc.drawPoint(x + 7, y + 5);
            gc.drawPoint(x + 7, y + 8);
            gc.drawPoint(x + 8, y + 6);
            gc.drawPoint(x + 9, y + 2);
            gc.drawPoint(x + 9, y + 4);
            gc.setForeground(RUtils.MARK_03);
            gc.drawPoint(x + 4, y + 9);
            gc.drawLine(x + 5, y + 10, x + 6, y + 10);
            gc.setForeground(RUtils.MARK_04);
            gc.drawPoint(x + 4, y + 6);
            gc.drawPoint(x + 7, y + 4);
            gc.drawPoint(x + 7, y + 9);
            gc.drawPoint(x + 8, y + 2);
            gc.setForeground(RUtils.MARK_05);
            gc.drawPoint(x + 6, y + 6);
            gc.drawPoint(x + 8, y + 7);
            gc.drawPoint(x + 9, y + 5);
            gc.drawPoint(x + 10, y + 3);
        }

        if (this.text != null) {
            gc.setForeground(this.enabled ? RUtils.FONT_COLOR : RUtils.FONT_DISABLE);
            gc.drawString(this.text, x + 13 + 6, y - 2, true);
        }

    }
}

