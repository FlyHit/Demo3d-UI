package ribbon;

import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public final class RButton extends RWidget {
    private String text;
    private Image image;
    private Image disImage;
    private boolean selected;
    private boolean smallButton;
    private boolean arrowHovered;
    private boolean arrowClicked;
    private List<SelectionListener> listeners;

    public RButton(RGroup parent, int style) {
        super(parent, style & 14);
        this.listeners = new ArrayList();
        parent.addWidget(this);
    }

    public RButton(RMerger parent, int style) {
        super(parent, style & 14);
        this.smallButton = true;
        this.listeners = new ArrayList();
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

    public Image getImage() {
        return this.image;
    }

    public void setImage(Image image) {
        this.image = this.reduceImage(image);
        this.parent.updateBounds();
        this.parent.redraw();
        this.parent.getParent().layout(false);
    }

    public boolean getSelection() {
        return this.selected;
    }

    public void setSelection(boolean selected) {
        this.selected = selected;
        this.parent.redraw(this.bounds.x, this.bounds.y, this.bounds.width, this.bounds.height, false);
    }

    public void addSelectionListener(SelectionListener listener) {
        if (!this.listeners.contains(listener)) {
            this.listeners.add(listener);
        }
    }

    public void removeSelectionListener(SelectionListener listener) {
        this.listeners.remove(listener);
    }

    protected boolean isSmall() {
        return this.smallButton;
    }

    protected boolean isSplit() {
        return (this.style & 8) == 0;
    }

    protected boolean isArrow() {
        return (this.style & 4) != 0;
    }

    protected boolean isToggle() {
        return (this.style & 2) != 0;
    }

    protected boolean isArrowHovered() {
        return this.arrowHovered;
    }

    protected boolean checkHovered(int x, int y) {
        boolean changed = false;
        Rectangle arrow = this.smallButton ? new Rectangle(this.bounds.x + this.bounds.width - 12, this.bounds.y, 12, 22) : new Rectangle(this.bounds.x, this.bounds.y + 31, this.bounds.width, 32);
        if (arrow.contains(x, y)) {
            if (!this.arrowHovered) {
                changed = true;
            }

            this.arrowHovered = true;
        } else {
            if (this.arrowHovered) {
                changed = true;
            }

            this.arrowHovered = false;
        }

        return changed;
    }

    protected void checkClicked(int x, int y) {
        Rectangle arrow = this.smallButton ? new Rectangle(this.bounds.x + this.bounds.width - 12, this.bounds.y, 12, 22) : new Rectangle(this.bounds.x, this.bounds.y + 31, this.bounds.width, 32);
        this.arrowClicked = arrow.contains(x, y);
    }

    protected void onClick(MouseEvent event) {
        Event e = new Event();
        e.widget = event.widget;
        e.x = event.x;
        e.y = event.y;
        SelectionEvent selectionEvent = new SelectionEvent(e);
        if (this.isSplit()) {
            Rectangle arrow = this.smallButton ? new Rectangle(this.bounds.x + this.bounds.width - 12, this.bounds.y, 12, 22) : new Rectangle(this.bounds.x, this.bounds.y + 33, this.bounds.width, 33);
            SelectionListener listener;
            Iterator var6;
            if (!arrow.contains(event.x, event.y) && !this.arrowClicked) {
                var6 = this.listeners.iterator();

                while(var6.hasNext()) {
                    listener = (SelectionListener)var6.next();
                    listener.widgetSelected(selectionEvent);
                }
            } else if (arrow.contains(event.x, event.y) && this.arrowClicked) {
                selectionEvent.detail = 4;
                var6 = this.listeners.iterator();

                while(var6.hasNext()) {
                    listener = (SelectionListener)var6.next();
                    listener.widgetSelected(selectionEvent);
                }
            }
        } else {
            Iterator var8 = this.listeners.iterator();

            while(var8.hasNext()) {
                SelectionListener listener = (SelectionListener)var8.next();
                listener.widgetSelected(selectionEvent);
            }
        }

    }

    protected void drawWidget(GC gc) {
        boolean isHover = this == this.parent.getHovered();
        boolean isClick = this == this.parent.getClicked();
        if (!this.isToggle()) {
            this.selected = false;
        }

        int x;
        int y;
        int width;
        int height;
        if ((isHover || this.selected) && this.enabled) {
            x = this.bounds.x;
            y = this.bounds.y;
            width = this.bounds.width;
            height = this.bounds.height;
            gc.setForeground(isHover && !isClick ? RUtils.HOVER_HIGH_LIGHT : RUtils.CLICK_HIGH_LIGHT);
            gc.setBackground(isHover && !isClick ? RUtils.HOVER_COLOR : RUtils.CLICK_COLOR);
            if (this.smallButton) {
                gc.fillGradientRectangle(x + 1, y + 1, width - 2, 7, true);
            } else {
                gc.fillGradientRectangle(x + 1, y + 1, width - 2, 21, true);
            }

            gc.setForeground(isHover && !isClick ? RUtils.HOVER_DARK_LIGHT : RUtils.CLICK_DARK_LIGHT);
            if (this.smallButton) {
                gc.fillGradientRectangle(x + 1, y + 8, width - 2, 13, true);
            } else {
                gc.fillGradientRectangle(x + 1, y + 22, width - 2, 41, true);
            }

            gc.setForeground(!isClick && !this.selected ? RUtils.HOVER_BORDER : RUtils.CLICK_BORDER);
            gc.drawLine(x + 1, y, x + width - 2, y);
            gc.drawLine(x, y + 1, x, y + height - 2);
            gc.drawLine(x + width - 1, y + 1, x + width - 1, y + height - 2);
            gc.drawLine(x + 1, y + height - 1, x + width - 2, y + height - 1);
            if (isHover && !isClick) {
                gc.setForeground(RUtils.HOVER_INSIDE);
                gc.drawLine(x + 1, y + 2, x + 1, y + height - 3);
                gc.drawLine(x + width - 2, y + 2, x + width - 2, y + height - 3);
            }

            if (this.isSplit() && isHover) {
                if (this.smallButton) {
                    gc.setForeground(RUtils.HOVER_INSIDE);
                    gc.setBackground(RUtils.HOVER_COLOR);
                    if (this.arrowHovered) {
                        gc.fillGradientRectangle(x + 1, y + 2, width - 14, 18, true);
                    } else {
                        gc.fillGradientRectangle(x + width - 12, y + 2, 11, 18, true);
                    }

                    gc.setForeground(isClick ? RUtils.CLICK_BORDER : RUtils.HOVER_BORDER);
                    gc.drawLine(x + width - 13, y + 1, x + width - 13, y + height - 2);
                    if (!isClick) {
                        gc.setForeground(RUtils.HOVER_INSIDE);
                        gc.drawLine(x + width - 12, y + 1, x + width - 12, y + height - 2);
                        gc.drawLine(x + width - 14, y + 1, x + width - 14, y + height - 2);
                    }
                } else {
                    if (this.arrowHovered) {
                        gc.setBackground(RUtils.HOVER_HIGH_LIGHT);
                        gc.fillRectangle(x + 1, y + 1, width - 2, 21);
                        gc.setBackground(RUtils.HOVER_COLOR);
                        gc.fillRectangle(x + 1, y + 22, width - 2, 8);
                    } else {
                        gc.setForeground(RUtils.HOVER_INSIDE);
                        gc.setBackground(RUtils.HOVER_COLOR);
                        gc.fillGradientRectangle(x + 1, y + 31, width - 2, 31, true);
                    }

                    gc.setForeground(isClick ? RUtils.CLICK_BORDER : RUtils.HOVER_BORDER);
                    gc.drawLine(x + 1, y + 30, x + width - 2, y + 30);
                    if (!isClick) {
                        gc.setForeground(RUtils.HOVER_INSIDE);
                        gc.drawLine(x + 2, y + 29, x + width - 3, y + 29);
                        gc.drawLine(x + 2, y + 31, x + width - 3, y + 31);
                    }
                }
            }

            gc.setForeground(!isClick && !this.selected ? RUtils.HOVER_BORDER : RUtils.CLICK_BORDER);
            gc.drawLine(x + 1, y + 1, x + 1, y + 1);
            gc.drawLine(x + width - 2, y + 1, x + width - 2, y + 1);
            gc.drawLine(x + 1, y + height - 2, x + 1, y + height - 2);
            gc.drawLine(x + width - 2, y + height - 2, x + width - 2, y + height - 2);
        }

        if (this.image != null) {
            if (this.disImage == null) {
                this.disImage = new Image(Display.getCurrent(), this.image, 2);
            }

            if (this.smallButton) {
                x = this.bounds.x + 3;
                y = this.bounds.y + 3;
                gc.drawImage(this.enabled ? this.image : this.disImage, x, y);
            } else {
                x = this.bounds.x + this.bounds.width / 2 - this.image.getBounds().width / 2;
                y = this.bounds.y + 3;
                gc.drawImage(this.enabled ? this.image : this.disImage, x, y);
            }
        }

        gc.setForeground(this.enabled ? RUtils.FONT_COLOR : RUtils.FONT_DISABLE);
        if (this.smallButton) {
            if (this.text != null) {
                x = this.bounds.x + (this.image == null ? 0 : this.image.getBounds().width + 1);
                y = this.bounds.y + 2;
                gc.drawString(this.text, x + 4, y, true);
            }

            if (this.isArrow()) {
                x = this.bounds.x + this.bounds.width - 9;
                y = this.bounds.y + 9;
                this.drawArrow(gc, x, y);
            }
        } else {
            x = 0;
            if (this.text != null) {
                String[] strs = this.text.split("\n", 2);
                width = this.bounds.x + this.bounds.width / 2 - gc.stringExtent(strs[0].trim()).x / 2;
                height = this.bounds.y + 30;
                gc.drawString(strs[0].trim(), width, height, true);
                if (strs.length > 1) {
                    x = gc.stringExtent(strs[1].trim()).x;
                    width = this.bounds.x + this.bounds.width / 2 - x / 2;
                    y = 49;
                    gc.drawString(strs[1].trim(), this.isArrow() ? width - 5 : width, y, true);
                }
            }

            if (this.isArrow()) {
                y = this.bounds.x + this.bounds.width / 2 - 2;
                y = 56;
                this.drawArrow(gc, x > 0 ? y + x / 2 + 3 : y, y);
            }
        }

    }

    private void drawArrow(GC gc, int x, int y) {
        gc.drawLine(x, y, x + 4, y);
        gc.drawLine(x + 1, y + 1, x + 3, y + 1);
        gc.drawLine(x + 2, y + 2, x + 2, y + 2);
    }

    private Image reduceImage(Image image) {
        if (image != null) {
            int width = image.getBounds().width;
            int height = image.getBounds().height;
            double scale = 1.0D;
            if (this.smallButton) {
                if (height > 16) {
                    scale = 16.0D / (double)height;
                } else if (height > 24) {
                    scale = 24.0D / (double)height;
                }
            }

            if (scale != 1.0D) {
                width = (int)((double)width * scale);
                height = (int)((double)height * scale);
                return new Image(Display.getCurrent(), image.getImageData().scaledTo(width, height));
            }
        }

        return image;
    }
}
