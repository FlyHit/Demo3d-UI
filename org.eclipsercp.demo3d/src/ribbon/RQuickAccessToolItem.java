package ribbon;

import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public final class RQuickAccessToolItem {
    private RQuickAccessToolBar parent;
    private Image image;
    private Image disImage;
    private Rectangle bounds;
    private boolean enabled = true;
    private boolean selected;
    private boolean isToggle;
    private List<SelectionListener> listeners = new ArrayList();
    private RToolTip toolTip;
    private RMenu menu;
    private Object object;

    public static RQuickAccessToolItem createPush(boolean isToggle) {
        RQuickAccessToolItem item = new RQuickAccessToolItem();
        item.isToggle = isToggle;
        return item;
    }

    public static RQuickAccessToolItem fromButton(RButton button) {
        RQuickAccessToolItem item = new RQuickAccessToolItem();
        item.setImage(button.getImage());
        item.enabled = button.getEnabled();
        item.selected = button.getSelection();
        item.isToggle = button.isToggle();
        item.toolTip = button.getToolTip() != null ? button.getToolTip() : new RToolTip(button.getText());
        item.menu = button.getMenu();
        item.object = button;
        return item;
    }

    public Image getImage() {
        return this.image;
    }

    public void setImage(Image image) {
        this.image = this.reduceImage(image);
        if (this.parent != null && this.parent.getVisible()) {
            this.parent.setVisible(true);
        }

    }

    public boolean getEnabled() {
        return this.enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
        if (this.parent != null && this.parent.getVisible()) {
            this.parent.setVisible(true);
        }

    }

    public boolean getSelection() {
        return this.selected;
    }

    public void setSelection(boolean selected) {
        this.selected = selected;
        if (this.parent != null && this.parent.getVisible()) {
            this.parent.setVisible(true);
        }

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

    public RMenu getMenu() {
        return this.menu;
    }

    public void setMenu(RMenu menu) {
        if (this.menu != null) {
            this.menu.dispose();
        }

        this.menu = menu;
    }

    protected RQuickAccessToolItem() {
    }

    protected void setParent(RQuickAccessToolBar parent) {
        this.parent = parent;
    }

    protected Rectangle getBounds() {
        return this.bounds;
    }

    protected void setBounds(Rectangle bounds) {
        this.bounds = bounds;
    }

    protected boolean isToggle() {
        return this.isToggle;
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

        if (this.object != null && this.object instanceof RButton) {
            RButton button = (RButton)this.object;
            button.onClick(event);
        }

    }

    protected void drawWidget(GC gc, Region region) {
        int x = this.bounds.x;
        int y = this.bounds.y;
        int width = this.bounds.width;
        int height = this.bounds.height;
        boolean hovered = this == this.parent.getHovered();
        boolean clicked = this == this.parent.getClicked();
        if (!this.isToggle) {
            this.selected = false;
        }

        if ((hovered || this.selected) && this.enabled) {
            gc.setForeground(hovered && !clicked ? RUtils.HOVER_HIGH_LIGHT : RUtils.CLICK_HIGH_LIGHT);
            gc.setBackground(hovered && !clicked ? RUtils.HOVER_COLOR : RUtils.CLICK_COLOR);
            gc.fillGradientRectangle(x + 1, y + 1, width - 2, 7, true);
            gc.setForeground(hovered && !clicked ? RUtils.HOVER_DARK_LIGHT : RUtils.CLICK_DARK_LIGHT);
            gc.fillGradientRectangle(x + 1, y + 8, width - 2, 13, true);
            gc.setForeground(!clicked && !this.selected ? RUtils.HOVER_BORDER : RUtils.CLICK_BORDER);
            gc.drawLine(x + 1, y, x + width - 2, y);
            gc.drawLine(x + 1, y + 1, x + 1, y + 1);
            gc.drawLine(x + width - 2, y + 1, x + width - 2, y + 1);
            gc.drawLine(x, y + 1, x, y + 20);
            gc.drawLine(x + width - 1, y + 1, x + width - 1, y + 20);
            gc.drawLine(x + 1, y + 20, x + 1, y + 20);
            gc.drawLine(x + width - 2, y + 20, x + width - 2, y + 20);
            gc.drawLine(x + 1, y + 21, x + width - 2, y + 21);
            if (hovered && !clicked) {
                gc.setForeground(RUtils.HOVER_INSIDE);
                gc.drawLine(x + 1, y + 2, x + 1, y + 19);
                gc.drawLine(x + width - 2, y + 2, x + width - 2, y + 19);
            }

            if (region != null) {
                region.add(this.bounds);
                region.subtract(x, y, 1, 1);
                region.subtract(x + width - 1, y, 1, 1);
                region.subtract(x, y + height - 1, 1, 1);
                region.subtract(x + width - 1, y + height - 1, 1, 1);
            }
        }

        if (this.image != null) {
            if (this.disImage == null) {
                this.disImage = new Image(Display.getCurrent(), this.image, 2);
            }

            gc.drawImage(this.image, x + 3, y + 3);
            if (hovered || region == null) {
                return;
            }

            ImageData data = this.image.getImageData();

            if (data.alphaData != null) {
                for(int i = 0; i < data.width; i++) {
                    for(i = 0; i < data.height; i++) {
                        if (data.getAlpha(i, i) == 255) {
                            region.add(x + 3 + i, y + 3 + i, 1, 1);
                        }
                    }
                }
            } else {
                ImageData mask = data.getTransparencyMask();
                int i;
                for(i = 0; i < mask.width; ++i) {
                    for(int j = 0; j < mask.height; ++j) {
                        if (mask.getPixel(i, j) != 0) {
                            region.add(x + 3 + i, y + 3 + j, 1, 1);
                        }
                    }
                }
            }
        }

    }

    private Image reduceImage(Image image) {
        if (image != null) {
            int width = image.getBounds().width;
            int height = image.getBounds().height;
            double scale = height > 16 ? 16.0D / (double)height : 1.0D;
            if (scale != 1.0D) {
                width = (int)((double)width * scale);
                height = (int)((double)height * scale);
                return new Image(Display.getCurrent(), image.getImageData().scaledTo(width, height));
            }
        }

        return image;
    }
}

