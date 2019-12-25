package ribbon;

import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public final class RMenuItem {
    private RMenu parent;
    private int style;
    private String text;
    private String message;
    private Image image;
    private Image disImage;
    private int accelerator;
    private boolean enabled;
    private boolean selected;
    private Rectangle bounds;
    private String out;
    private int keyCode;
    private int keyIndex;
    private List<SelectionListener> listeners;
    private RMenu menu;
    private RToolTip toolTip;
    private Object data;

    public RMenuItem(RMenu parent, int style, int index) {
        this.accelerator = -1;
        this.enabled = true;
        this.parent = parent;
        this.style = style & 58;
        this.listeners = new ArrayList();
        this.parent.addMenuItem(this, index);
        final Display display = Display.getCurrent();
        display.addFilter(1, new Listener() {
            public void handleEvent(Event event) {
                if (display.getActiveShell() == display.getShells()[0]) {
                    if (RMenuItem.this.enabled && !RMenuItem.this.isSeparator()) {
                        if (event.stateMask == 262144 && event.keyCode == Character.toLowerCase(RMenuItem.this.accelerator) || event.keyCode == Character.toLowerCase(RMenuItem.this.keyCode) && RUtils.topMenu == RMenuItem.this.getParent()) {
                            if (RMenuItem.this.isRadio() && !RMenuItem.this.getParent().isMain()) {
                                RMenuItem.this.selected = true;
                            } else if (RMenuItem.this.isCheck() && !RMenuItem.this.getParent().isMain()) {
                                RMenuItem.this.selected = !RMenuItem.this.selected;
                            } else if (RMenuItem.this.menu != null && !RMenuItem.this.menu.getVisible()) {
                                RMenuItem.this.menu.setLocation(RMenuItem.this.getParent().getShell().toDisplay(RMenuItem.this.bounds.x + RMenuItem.this.bounds.width, RMenuItem.this.bounds.y));
                                RMenuItem.this.menu.setVisible(true);
                            }

                            RMenuItem.this.onClick(new MouseEvent(event));
                        }

                    }
                }
            }
        });
    }

    public RMenuItem(RMenu parent, int style) {
        this(parent, style, parent.getItemCount());
    }

    public RMenu getParent() {
        return this.parent;
    }

    public int getStyle() {
        return this.style;
    }

    public String getText() {
        return this.text;
    }

    public void setText(String text) {
        this.text = text;
        this.out = this.parseText(text);
    }

    public String getMessage() {
        return this.message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Image getImage() {
        return this.image;
    }

    public void setImage(Image image) {
        this.image = this.reduceImage(image);
    }

    public int getAccelerator() {
        return this.accelerator;
    }

    public void setAccelerator(int accelerator) {
        this.accelerator = accelerator;
    }

    public boolean getEnabled() {
        return this.enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public boolean getSelection() {
        return this.selected;
    }

    public void setSelection(boolean selected) {
        this.selected = selected;
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

    public void setMenu(RMenu menu) {
        if (menu != this.parent) {
            if (this.menu != null) {
                this.menu.dispose();
            }

            this.menu = menu;
            this.menu.setParent(this);
        }
    }

    public RToolTip getToolTip() {
        return this.toolTip;
    }

    public void setToolTip(RToolTip toolTip) {
        this.toolTip = toolTip;
    }

    public Object getData() {
        return this.data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    protected RMenuItem(RMenu parent, String text) {
        this.accelerator = -1;
        this.enabled = true;
        this.parent = parent;
        this.text = text;
    }

    protected boolean isRadio() {
        return (this.style & 16) != 0;
    }

    protected boolean isCheck() {
        return (this.style & 32) != 0;
    }

    protected boolean isSeparator() {
        return (this.style & 2) != 0;
    }

    protected Rectangle getBounds() {
        return this.bounds;
    }

    protected void setBounds(Rectangle bounds) {
        this.bounds = bounds;
    }

    protected String getOutput() {
        return this.out;
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

        if (this.menu == null) {
            while(RUtils.topMenu != null) {
                RMenu topMenu = RUtils.topMenu;
                topMenu.setVisible(false);
                if (topMenu.getParent() != null) {
                    RUtils.topMenu = topMenu.getParent().getParent();
                }
            }
        }

    }

    protected void drawWidget(GC gc) {
        boolean isHover = this == this.parent.getHovered();
        boolean isMain = this.parent.isMain();
        boolean isRecent = this.listeners == null;
        boolean inMain = this.parent.getParent() != null && this.parent.getParent().getParent().isMain();
        if (!this.isRadio() && !this.isCheck()) {
            this.selected = false;
        }

        int x = this.bounds.x;
        int y = this.bounds.y;
        int width = this.bounds.width;
        int height = this.bounds.height;
        int descent = gc.getFontMetrics().getDescent();
        if (this.isSeparator()) {
            gc.setForeground(RUtils.MENU_LINE);
            if (!isMain && !inMain) {
                gc.drawLine(26, y, 26, y + 2);
                gc.drawLine(35, y + 1, width + 1, y + 1);
            } else {
                gc.drawLine(43, y + 1, width, y + 1);
            }
        } else {
            if (!isMain && !inMain) {
                gc.setForeground(RUtils.MENU_LINE);
                gc.drawLine(26, y, 26, y + 24);
            }

            if (isHover) {
                gc.setForeground(RUtils.HOVER_HIGH_LIGHT);
                gc.setBackground(RUtils.HOVER_COLOR);
                if (isMain && !isRecent) {
                    gc.fillGradientRectangle(x + 1, y + 1, width - 2, 16, true);
                } else if (inMain) {
                    gc.fillGradientRectangle(x + 1, y + 1, width - 2, 22, true);
                } else {
                    gc.fillGradientRectangle(x + 1, y + 1, width - 2, 9, true);
                }

                gc.setForeground(RUtils.HOVER_DARK_LIGHT);
                if (isMain && !isRecent) {
                    gc.fillGradientRectangle(x + 1, y + 17, width - 2, 25, true);
                } else if (inMain) {
                    gc.fillGradientRectangle(x + 1, y + 23, width - 2, 35, true);
                } else {
                    gc.fillGradientRectangle(x + 1, y + 10, width - 2, 14, true);
                }

                gc.setForeground(RUtils.HOVER_BORDER);
                gc.drawLine(x + 1, y, x + width - 2, y);
                gc.drawLine(x + 1, y + 1, x + 1, y + 1);
                gc.drawLine(x + width - 2, y + 1, x + width - 2, y + 1);
                gc.drawLine(x, y + 1, x, y + height - 2);
                gc.drawLine(x + width - 1, y + 1, x + width - 1, y + height - 2);
                gc.drawLine(x + 1, y + height - 2, x + 1, y + height - 2);
                gc.drawLine(x + width - 2, y + height - 2, x + width - 2, y + height - 2);
                gc.drawLine(x + 1, y + height - 1, x + width - 2, y + height - 1);
                gc.setForeground(RUtils.HOVER_INSIDE);
                gc.drawLine(x + 1, y + 2, x + 1, y + height - 3);
                gc.drawLine(x + width - 2, y + 2, x + width - 2, y + height - 3);
            }

            Point start;
            if (isRecent) {
                gc.setForeground(RUtils.MENU_TEXT);
                gc.drawString(this.text, x + 8, y + 4, true);
                start = gc.stringExtent(this.text.substring(0, 1));
                int lineY = y + start.y - descent + 5;
                gc.drawLine(x + 8, lineY, x + start.x + 7, lineY);
                return;
            }

            if (this.isRadio() && !isMain && !inMain) {
                if (this.selected) {
                    gc.setBackground(this.enabled ? RUtils.MENU_01 : RUtils.MENU_BACK);
                    gc.fillRectangle(x + 3, y + 3, 18, 18);
                    gc.setForeground(this.enabled ? RUtils.MENU_02 : RUtils.MENU_LINE);
                    gc.drawLine(x + 3, y + 2, x + 20, y + 2);
                    gc.drawLine(x + 2, y + 3, x + 2, y + 20);
                    gc.drawLine(x + 21, y + 3, x + 21, y + 20);
                    gc.drawLine(x + 3, y + 21, x + 20, y + 21);
                    gc.drawPoint(x + 3, y + 3);
                    gc.drawPoint(x + 20, y + 3);
                    gc.drawPoint(x + 3, y + 20);
                    gc.drawPoint(x + 20, y + 20);
                    gc.setForeground(this.enabled ? RUtils.MENU_03 : RUtils.MENU_LINE);
                    gc.drawPoint(x + 12, y + 9);
                    gc.drawLine(x + 12, y + 10, x + 13, y + 10);
                    gc.setForeground(this.enabled ? RUtils.MENU_04 : RUtils.MENU_LINE);
                    gc.drawLine(x + 11, y + 9, x + 11, y + 10);
                    gc.drawLine(x + 13, y + 8, x + 13, y + 9);
                    gc.drawLine(x + 14, y + 10, x + 14, y + 11);
                    gc.drawLine(x + 12, y + 11, x + 13, y + 11);
                    gc.drawPoint(x + 8, y + 13);
                    gc.drawPoint(x + 15, y + 13);
                    gc.drawPoint(x + 10, y + 15);
                    gc.drawPoint(x + 13, y + 15);
                    gc.setForeground(this.enabled ? RUtils.MENU_05 : RUtils.MARK_04);
                    gc.drawPoint(x + 8, y + 10);
                    gc.drawPoint(x + 14, y + 9);
                    gc.drawPoint(x + 15, y + 10);
                    gc.drawLine(x + 10, y + 8, x + 10, y + 11);
                    gc.drawLine(x + 11, y + 11, x + 11, y + 12);
                    gc.drawRectangle(x + 12, y + 12, 2, 1);
                    gc.setForeground(this.enabled ? RUtils.MENU_06 : RUtils.FONT_DISABLE);
                    gc.drawPoint(x + 12, y + 8);
                    gc.drawLine(x + 9, y + 10, x + 9, y + 11);
                    gc.drawLine(x + 10, y + 12, x + 10, y + 13);
                    gc.drawLine(x + 14, y + 13, x + 14, y + 14);
                    gc.drawLine(x + 11, y + 15, x + 12, y + 15);
                    gc.drawPoint(x + 11, y + 13);
                    gc.drawPoint(x + 9, y + 14);
                    gc.setForeground(this.enabled ? RUtils.MENU_07 : RUtils.FONT_DISABLE);
                    gc.drawPoint(x + 11, y + 8);
                    gc.drawPoint(x + 9, y + 9);
                    gc.drawLine(x + 8, y + 11, x + 8, y + 12);
                    gc.drawLine(x + 15, y + 11, x + 15, y + 12);
                    gc.drawLine(x + 9, y + 12, x + 9, y + 13);
                    gc.drawLine(x + 10, y + 14, x + 13, y + 14);
                }
            } else if (this.isCheck() && !isMain && !inMain) {
                if (this.selected) {
                    gc.setBackground(this.enabled ? RUtils.MENU_01 : RUtils.MENU_BACK);
                    gc.fillRectangle(x + 3, y + 3, 18, 18);
                    gc.setForeground(this.enabled ? RUtils.MENU_02 : RUtils.MENU_LINE);
                    gc.drawLine(x + 3, y + 2, x + 20, y + 2);
                    gc.drawLine(x + 2, y + 3, x + 2, y + 20);
                    gc.drawLine(x + 21, y + 3, x + 21, y + 20);
                    gc.drawLine(x + 3, y + 21, x + 20, y + 21);
                    gc.drawPoint(x + 3, y + 3);
                    gc.drawPoint(x + 20, y + 3);
                    gc.drawPoint(x + 3, y + 20);
                    gc.drawPoint(x + 20, y + 20);
                    gc.setForeground(this.enabled ? RUtils.MENU_03 : RUtils.MENU_LINE);
                    gc.drawLine(x + 14, y + 7, x + 16, y + 7);
                    gc.drawLine(x + 13, y + 9, x + 15, y + 9);
                    gc.drawPoint(x + 14, y + 11);
                    gc.drawPoint(x + 13, y + 13);
                    gc.drawPoint(x + 9, y + 14);
                    gc.drawPoint(x + 12, y + 15);
                    gc.setForeground(this.enabled ? RUtils.MENU_04 : RUtils.MENU_LINE);
                    gc.drawPoint(x + 12, y + 11);
                    gc.drawPoint(x + 8, y + 12);
                    gc.setForeground(this.enabled ? RUtils.MENU_05 : RUtils.MARK_04);
                    gc.drawLine(x + 15, y + 6, x + 16, y + 6);
                    gc.drawPoint(x + 10, y + 15);
                    gc.drawPoint(x + 11, y + 16);
                    gc.setForeground(this.enabled ? RUtils.MENU_06 : RUtils.FONT_DISABLE);
                    gc.drawLine(x + 14, y + 8, x + 15, y + 8);
                    gc.drawPoint(x + 14, y + 10);
                    gc.drawLine(x + 8, y + 11, x + 9, y + 11);
                    gc.drawPoint(x + 10, y + 12);
                    gc.drawPoint(x + 13, y + 12);
                    gc.drawPoint(x + 9, y + 13);
                    gc.drawPoint(x + 12, y + 14);
                    gc.setForeground(this.enabled ? RUtils.MENU_07 : RUtils.FONT_DISABLE);
                    gc.drawPoint(x + 15, y + 7);
                    gc.drawPoint(x + 14, y + 9);
                    gc.drawLine(x + 13, y + 10, x + 13, y + 11);
                    gc.drawPoint(x + 9, y + 12);
                    gc.drawPoint(x + 12, y + 12);
                    gc.drawLine(x + 10, y + 13, x + 12, y + 13);
                    gc.drawLine(x + 10, y + 14, x + 11, y + 14);
                    gc.drawPoint(x + 11, y + 15);
                }
            } else {
                if (this.image != null) {
                    if (this.disImage == null) {
                        this.disImage = new Image(Display.getCurrent(), this.image, 2);
                    }

                    Image temp = this.enabled ? this.image : this.disImage;
                    if (!isMain && !inMain) {
                        gc.drawImage(temp, 13 - temp.getBounds().width / 2, y + 13 - temp.getBounds().height / 2);
                    } else {
                        gc.drawImage(temp, 22 - temp.getBounds().width / 2, y + 22 - temp.getBounds().height / 2);
                    }
                }

                if (this.menu != null) {
                    gc.setForeground(this.enabled ? RUtils.MENU_TEXT : RUtils.FONT_DISABLE);
                    this.drawArrow(gc, width - 12, y + height / 2 - 3);
                }
            }

            if (this.out != null) {
                gc.setForeground(this.enabled ? RUtils.MENU_TEXT : RUtils.FONT_DISABLE);
                Point end;
                int lineY;
                if (isMain) {
                    gc.drawString(this.out, 43, y + 13, true);
                    if (this.keyCode != 0) {
                        start = gc.stringExtent(this.out.substring(0, this.keyIndex));
                        end = gc.stringExtent(this.out.substring(this.keyIndex, this.keyIndex + 1));
                        lineY = y + end.y - descent + 14;
                        gc.drawLine(43 + start.x, lineY, 42 + start.x + end.x, lineY);
                    }
                } else if (inMain) {
                    gc.setFont(RUtils.blodFont);
                    gc.drawString(this.out, 43, y + 4, true);
                    if (this.keyCode != 0) {
                        start = gc.stringExtent(this.out.substring(0, this.keyIndex));
                        end = gc.stringExtent(this.out.substring(this.keyIndex, this.keyIndex + 1));
                        lineY = y + end.y - descent + 5;
                        gc.drawLine(43 + start.x, lineY, 42 + start.x + end.x, lineY);
                    }

                    gc.setFont(RUtils.initFont);
                } else {
                    gc.drawString(this.out, 36, y + 4, true);
                    if (this.keyCode != 0) {
                        start = gc.stringExtent(this.out.substring(0, this.keyIndex));
                        end = gc.stringExtent(this.out.substring(this.keyIndex, this.keyIndex + 1));
                        lineY = y + end.y - descent + 5;
                        gc.drawLine(36 + start.x, lineY, 35 + start.x + end.x, lineY);
                    }
                }
            }

            if (inMain && this.message != null) {
                String[] strs = this.message.split("\n", 2);
                gc.drawString(strs[0].trim(), 43, y + 23, true);
                if (strs.length > 1) {
                    gc.drawString(strs[1].trim(), 43, y + 39, true);
                }
            }
        }

    }

    private void drawArrow(GC gc, int x, int y) {
        gc.drawLine(x, y, x, y + 6);
        gc.drawLine(x + 1, y + 1, x + 1, y + 5);
        gc.drawLine(x + 2, y + 2, x + 2, y + 4);
        gc.drawLine(x + 3, y + 3, x + 3, y + 3);
    }

    private String parseText(String text) {
        this.keyCode = 0;
        this.keyIndex = 0;
        if (text == null) {
            return null;
        } else {
            String out = "";
            boolean escape = false;
            String[] strs = text.split("&");

            for(int i = 1; i < strs.length; ++i) {
                if (strs[i].isEmpty()) {
                    strs[i] = "&";
                    escape = true;
                } else {
                    if (!escape) {
                        this.keyCode = strs[i].charAt(0);
                        this.keyIndex += strs[i - 1].length();
                    }

                    escape = false;
                }
            }

            String[] var8 = strs;
            int var7 = strs.length;

            for(int var6 = 0; var6 < var7; ++var6) {
                String str = var8[var6];
                out = out + str;
            }

            return out;
        }
    }

    private Image reduceImage(Image image) {
        if (image != null) {
            int width = image.getBounds().width;
            int height = image.getBounds().height;
            double scale = 1.0D;
            if (!this.parent.isMain() && (this.parent.getParent() == null || !this.parent.getParent().getParent().isMain())) {
                if (width > 21 || height > 21) {
                    scale = 21.0D / (double)(width > height ? width : height);
                }
            } else if (width > 32 || height > 32) {
                scale = 32.0D / (double)(width > height ? width : height);
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
