package ribbon;

import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.graphics.Region;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;

import java.util.*;

public final class RMenu {
    private Shell shell;
    private RMenuItem parent;
    private Map<Integer, String> texts;
    private List<RMenuItem> items;
    private RMenuItem hovered;
    private RMenuItem clicked;
    private Rectangle bounds;
    private List<MenuListener> listeners;
    private RMainButton main;
    private String text;
    private List<RMenuItem> recents;
    private SelectionListener listener;

    public RMenu() {
        this.texts = new HashMap();
        this.items = new ArrayList();
        this.bounds = new Rectangle(0, 0, 0, 0);
        this.listeners = new ArrayList();
    }

    public RMenu(RMainButton parent) {
        this();
        this.main = parent;
        this.main.setMenu(this);
        this.recents = new ArrayList();
        Display.getCurrent().addFilter(1, new Listener() {
            public void handleEvent(Event event) {
                if (RUtils.topMenu == RMenu.this) {
                    Iterator var3 = RMenu.this.recents.iterator();

                    while(var3.hasNext()) {
                        RMenuItem recent = (RMenuItem)var3.next();
                        if (event.keyCode == recent.getText().charAt(0)) {
                            if (RMenu.this.listener != null) {
                                SelectionEvent selectionEvent = new SelectionEvent(event);
                                String[] strs = recent.getText().split("　", 2);
                                selectionEvent.detail = Integer.parseInt(strs[0]) - 1;
                                selectionEvent.text = recent.getToolTip() == null ? strs[1] : recent.getToolTip().getMessage();
                                RMenu.this.listener.widgetSelected(selectionEvent);
                            }

                            while(RUtils.topMenu != null) {
                                RMenu topMenu = RUtils.topMenu;
                                topMenu.setVisible(false);
                                if (topMenu.getParent() != null) {
                                    RUtils.topMenu = topMenu.getParent().getParent();
                                }
                            }

                            return;
                        }
                    }

                }
            }
        });
    }

    public RMenuItem getParent() {
        return this.parent;
    }

    public String getText(int index) {
        return (String)this.texts.get(index);
    }

    public void setText(String text, int index) {
        this.texts.put(index, text);
    }

    public void removeText(int index) {
        this.texts.remove(index);
    }

    public RMenuItem getItem(int index) {
        return (RMenuItem)this.items.get(index);
    }

    public int indexOf(RMenuItem item) {
        return this.items.indexOf(item);
    }

    public int getItemCount() {
        return this.items.size();
    }

    public List<RMenuItem> getItems() {
        return this.items;
    }

    public Point getLocation() {
        return this.getVisible() ? new Point(this.bounds.x, this.bounds.y) : null;
    }

    public void setLocation(Point location) {
        this.bounds.x = location.x;
        this.bounds.y = location.y;
    }

    public Rectangle getBounds() {
        return this.getVisible() ? this.bounds : null;
    }

    public boolean getVisible() {
        return this.shell != null && !this.shell.isDisposed();
    }

    public void setVisible(boolean visible) {
        if (visible) {
            this.show();
        } else {
            this.kill();
        }

        Event e = new Event();
        e.widget = this.shell;
        MenuEvent event = new MenuEvent(e);
        Iterator var5 = this.listeners.iterator();

        while(var5.hasNext()) {
            MenuListener listener = (MenuListener)var5.next();
            if (visible) {
                listener.menuShown(event);
            } else {
                listener.menuHidden(event);
            }
        }

    }

    public void addMenuListener(MenuListener listener) {
        if (!this.listeners.contains(listener)) {
            this.listeners.add(listener);
        }
    }

    public void removeMenuListener(MenuListener listener) {
        this.listeners.remove(listener);
    }

    public String getRecentText() {
        return this.text;
    }

    public void setRecentText(String text) {
        this.text = text;
    }

    public void setRecentList(List<String> recents) {
        this.recents.clear();
        if (recents != null) {
            for(int i = 0; i < Math.min(9, recents.size()); ++i) {
                RMenuItem recent = new RMenuItem(this, (String)null);
                recent.setToolTip(new RToolTip((String)recents.get(i), true));
                this.recents.add(recent);
            }

        }
    }

    public void setRecentListener(SelectionListener listener) {
        this.listener = listener;
    }

    protected void dispose() {
        this.shell.dispose();
    }

    protected void show() {
        RUtils.topMenu = this;
        this.shell = new Shell(540684);
        GC gc = new GC(this.shell);
        gc.setFont(RUtils.initFont);
        int width;
        int y;
        int i;
        if (this.main != null) {
            width = 0;
            width = 27;
            Iterator var5 = this.items.iterator();

            RMenuItem item;
            while(var5.hasNext()) {
                item = (RMenuItem)var5.next();
                if (item.isSeparator()) {
                    item.setBounds(new Rectangle(1, width, 0, 3));
                    width += 3;
                } else {
                    if (item.getOutput() != null) {
                        width = Math.max(width, 43 + gc.stringExtent(item.getOutput()).x);
                    }

                    item.setBounds(new Rectangle(1, width, 0, 43));
                    width += 43;
                }
            }

            for(var5 = this.items.iterator(); var5.hasNext(); item.getBounds().width = width + 21) {
                item = (RMenuItem)var5.next();
            }

            y = 53;

            for(i = 0; i < this.recents.size(); ++i) {
                String text = ((RMenuItem)this.recents.get(i)).getToolTip().getMessage();

                for(text = text.substring(text.lastIndexOf("\\") + 1); gc.stringExtent(text).x > 290; text = text.substring(0, text.length() - 4) + "...") {
                }

                ((RMenuItem)this.recents.get(i)).setText(i + 1 + "　" + text);
                ((RMenuItem)this.recents.get(i)).setBounds(new Rectangle(width + 24, y, 322, 25));
                y += 25;
            }

            width = Math.max(width, 53 + 25 * this.recents.size());
            this.bounds.width = width + 25 + 322;
            this.bounds.height = width + 18;
        } else {
            boolean flag = this.parent != null && this.parent.getParent().isMain();
            width = 0;
            y = 0;

            for(i = 0; i < this.items.size(); ++i) {
                if (this.texts.containsKey(i)) {
                    gc.setFont(RUtils.blodFont);
                    width = Math.max(width, 10 + gc.stringExtent((String)this.texts.get(i)).x);
                    gc.setFont(RUtils.initFont);
                    y += 29;
                } else if (i == 0) {
                    y = 2;
                }

                RMenuItem item = (RMenuItem)this.items.get(i);
                if (item.isSeparator()) {
                    item.setBounds(new Rectangle(2, y, 0, 3));
                    y += 3;
                } else {
                    if (item.getOutput() != null) {
                        width = Math.max(width, 36 + gc.stringExtent(item.getOutput()).x);
                    }

                    item.setBounds(new Rectangle(2, y, 0, flag ? 59 : 25));
                    y += flag ? 59 : 25;
                }
            }

            if (flag) {
                width = 295;
                y = this.parent.getParent().getBounds().height - 51;
            }

            RMenuItem item;
            for(Iterator var13 = this.items.iterator(); var13.hasNext(); item.getBounds().width = width + 21) {
                item = (RMenuItem)var13.next();
            }

            this.bounds.width = width + 25;
            this.bounds.height = y + 2;
        }

        gc.dispose();
        this.shell.setBackground(RUtils.TAB_CLICKED);
        this.shell.addPaintListener(new PaintListener() {
            public void paintControl(PaintEvent event) {
                GC gc = event.gc;
                gc.setFont(RUtils.initFont);
                int width = RMenu.this.bounds.width;
                int height = RMenu.this.bounds.height;
                int x;
                int i;
                RMenuItem recent;
                if (RMenu.this.main != null) {
                    gc.setBackground(RUtils.MAIN_COLOR);
                    gc.fillRectangle(1, 1, RMenu.this.main.getBounds().width - 3, 25);
                    x = 0;
                    i = 13 - gc.stringExtent(RMenu.this.main.getText()).y / 2;
                    gc.setForeground(RUtils.TAB_CLICKED);
                    gc.drawString(RMenu.this.main.getText(), 16, i, true);
                    gc.setBackground(RUtils.MENU_BACK);
                    gc.fillGradientRectangle(RMenu.this.main.getBounds().width - 2, 1, width - RMenu.this.main.getBounds().width + 1, 12, true);
                    gc.setBackground(RUtils.BACK_COLOR);
                    gc.fillRectangle(RMenu.this.main.getBounds().width - 2, 13, width - RMenu.this.main.getBounds().width + 1, 13);
                    gc.setForeground(RUtils.LINE_COLOR);
                    gc.drawLine(1, 26, width - 2, 26);
                    Iterator var8 = RMenu.this.items.iterator();

                    while(var8.hasNext()) {
                        recent = (RMenuItem)var8.next();
                        recent.drawWidget(gc);
                        if (x == 0) {
                            x = recent.getBounds().width + 1;
                        }
                    }

                    gc.setBackground(RUtils.MENU_BACK);
                    gc.fillRectangle(x + 1, 27, width - x - 1, height - 26 - 18);
                    if (RMenu.this.text != null) {
                        gc.setForeground(RUtils.MENU_TEXT);
                        gc.setFont(RUtils.blodFont);
                        gc.drawString(RMenu.this.text, x + 9, 31, true);
                        gc.setFont(RUtils.initFont);
                    }

                    var8 = RMenu.this.recents.iterator();

                    while(var8.hasNext()) {
                        recent = (RMenuItem)var8.next();
                        recent.drawWidget(gc);
                    }

                    gc.setForeground(RUtils.MENU_LINE);
                    gc.drawLine(x, 27, x, height - 18);
                    gc.drawLine(x + 2, 52, width - 2, 52);
                    gc.drawLine(1, height - 18, width - 2, height - 18);
                    gc.setForeground(RUtils.TAB_CLICKED);
                    gc.setBackground(RUtils.MENU_BACK);
                    gc.fillGradientRectangle(1, height - 17, width - 2, 16, true);
                } else {
                    x = 0;

                    for(i = 0; i < RMenu.this.items.size(); ++i) {
                        if (RMenu.this.texts.containsKey(i)) {
                            gc.setBackground(RUtils.MENU_BACK);
                            gc.fillRectangle(1, x, width - 2, 28);
                            gc.setForeground(RUtils.MENU_LINE);
                            gc.drawLine(2, x, width - 3, x);
                            gc.drawLine(2, x + 27, width - 3, x + 27);
                            gc.setForeground(RUtils.TAB_CLICKED);
                            gc.drawLine(1, x + 28, width - 2, x + 28);
                            gc.setForeground(RUtils.MENU_TEXT);
                            gc.setFont(RUtils.blodFont);
                            gc.drawString((String) RMenu.this.texts.get(i), 8, x + 6, true);
                            gc.setFont(RUtils.initFont);
                            x += 29;
                        }

                        recent = (RMenuItem) RMenu.this.items.get(i);
                        recent.drawWidget(gc);
                        x += recent.getBounds().height;
                    }
                }

                gc.setForeground(RUtils.FONT_DISABLE);
                gc.drawRectangle(0, 0, width - 1, height - 1);
                gc.drawPoint(1, 1);
                gc.drawPoint(width - 2, 1);
                gc.drawPoint(width - 2, height - 2);
                gc.drawPoint(1, height - 2);
                Region region = new Region(Display.getCurrent());
                region.add(0, 0, width, height);
                region.subtract(0, 0, 1, 1);
                region.subtract(width - 1, height - 1, 1, 1);
                region.subtract(0, height - 1, 1, 1);
                region.subtract(width - 1, 0, 1, 1);
                RMenu.this.shell.setRegion(region);
            }
        });
        this.shell.addMouseListener(new MouseAdapter() {
            public void mouseDown(MouseEvent event) {
                if (RUtils.toolTip != null) {
                    RUtils.toolTip.kill();
                }

                if (event.button == 1) {
                    Iterator var3 = RMenu.this.items.iterator();

                    RMenuItem recent;
                    Rectangle bounds;
                    while(var3.hasNext()) {
                        recent = (RMenuItem)var3.next();
                        if (recent.getEnabled() && !recent.isSeparator()) {
                            bounds = recent.getBounds();
                            if (bounds.contains(event.x, event.y)) {
                                RMenu.this.clicked = recent;
                                break;
                            }
                        }
                    }

                    if (RMenu.this.main != null) {
                        var3 = RMenu.this.recents.iterator();

                        while(var3.hasNext()) {
                            recent = (RMenuItem)var3.next();
                            bounds = recent.getBounds();
                            if (bounds.contains(event.x, event.y)) {
                                RMenu.this.clicked = recent;
                                break;
                            }
                        }

                    }
                }
            }

            public void mouseUp(MouseEvent event) {
                if (event.button == 1) {
                    if (RMenu.this.clicked != null) {
                        Rectangle bounds = RMenu.this.clicked.getBounds();
                        if (bounds.contains(event.x, event.y)) {
                            RMenu topMenu;
                            if (RMenu.this.clicked.isRadio() && RMenu.this.main == null) {
                                RMenu.this.clicked.setSelection(true);
                            } else if (RMenu.this.clicked.isCheck() && RMenu.this.main == null) {
                                RMenu.this.clicked.setSelection(!RMenu.this.clicked.getSelection());
                            } else if (RMenu.this.clicked.getMenu() != null && !RMenu.this.clicked.getMenu().getVisible()) {
                                while(RUtils.topMenu != null && RUtils.topMenu != RMenu.this) {
                                    topMenu = RUtils.topMenu;
                                    topMenu.setVisible(false);
                                    RUtils.topMenu = topMenu.getParent().getParent();
                                }

                                RMenu.this.clicked.getMenu().setLocation(RMenu.this.shell.toDisplay(bounds.x + bounds.width, bounds.y));
                                RMenu.this.clicked.getMenu().setVisible(true);
                            }

                            if (RMenu.this.clicked.getParent().isMain() && bounds.height == 25) {
                                if (RMenu.this.listener != null) {
                                    Event e = new Event();
                                    e.widget = event.widget;
                                    e.x = event.x;
                                    e.y = event.y;
                                    SelectionEvent selectionEvent = new SelectionEvent(e);
                                    selectionEvent.detail = Integer.parseInt(RMenu.this.clicked.getText().substring(0, 1)) - 1;
                                    selectionEvent.text = RMenu.this.clicked.getToolTip().getMessage();
                                    RMenu.this.listener.widgetSelected(selectionEvent);
                                }

                                while(RUtils.topMenu != null) {
                                    topMenu = RUtils.topMenu;
                                    topMenu.setVisible(false);
                                    if (topMenu.getParent() != null) {
                                        RUtils.topMenu = topMenu.getParent().getParent();
                                    }
                                }
                            } else {
                                RMenu.this.clicked.onClick(event);
                            }
                        }

                        RMenu.this.clicked = null;
                    }

                }
            }
        });
        this.shell.addMouseMoveListener(new MouseMoveListener() {
            public void mouseMove(MouseEvent event) {
                Iterator var3 = RMenu.this.items.iterator();

                while(true) {
                    RMenuItem recent;
                    Rectangle bounds;
                    RMenu topMenu;
                    Rectangle origin;
                    do {
                        while(true) {
                            do {
                                do {
                                    if (!var3.hasNext()) {
                                        if (RMenu.this.main == null) {
                                            return;
                                        }

                                        var3 = RMenu.this.recents.iterator();

                                        while(var3.hasNext()) {
                                            recent = (RMenuItem)var3.next();
                                            bounds = recent.getBounds();
                                            if (bounds.contains(event.x, event.y)) {
                                                if (recent != RMenu.this.hovered) {
                                                    if (RMenu.this.hovered != null) {
                                                        origin = RMenu.this.hovered.getBounds();
                                                        RMenu.this.shell.redraw(origin.x, origin.y, origin.width, origin.height, false);
                                                        RMenu.this.hovered = null;
                                                        if (RUtils.toolTip != null) {
                                                            RUtils.toolTip.kill();
                                                        }
                                                    }

                                                    RMenu.this.shell.redraw(bounds.x, bounds.y, bounds.width, bounds.height, false);
                                                    RMenu.this.hovered = recent;
                                                }
                                            } else if (recent == RMenu.this.hovered) {
                                                RMenu.this.shell.redraw(bounds.x, bounds.y, bounds.width, bounds.height, false);
                                                RMenu.this.hovered = null;
                                                if (RUtils.toolTip != null) {
                                                    RUtils.toolTip.kill();
                                                }
                                            }
                                        }

                                        return;
                                    }

                                    recent = (RMenuItem)var3.next();
                                } while(!recent.getEnabled());
                            } while(recent.isSeparator());

                            bounds = recent.getBounds();
                            if (bounds.contains(event.x, event.y)) {
                                break;
                            }

                            if (recent == RMenu.this.hovered) {
                                while(RUtils.topMenu != null && RUtils.topMenu != RMenu.this) {
                                    topMenu = RUtils.topMenu;
                                    topMenu.setVisible(false);
                                    RUtils.topMenu = topMenu.getParent().getParent();
                                }

                                RMenu.this.shell.redraw(bounds.x, bounds.y, bounds.width, bounds.height, false);
                                RMenu.this.hovered = null;
                                if (RUtils.toolTip != null) {
                                    RUtils.toolTip.kill();
                                }
                            }
                        }
                    } while(recent == RMenu.this.hovered);

                    if (RMenu.this.hovered != null) {
                        while(RUtils.topMenu != null && RUtils.topMenu != RMenu.this) {
                            topMenu = RUtils.topMenu;
                            topMenu.setVisible(false);
                            RUtils.topMenu = topMenu.getParent().getParent();
                        }

                        origin = RMenu.this.hovered.getBounds();
                        RMenu.this.shell.redraw(origin.x, origin.y, origin.width, origin.height, false);
                        RMenu.this.hovered = null;
                        if (RUtils.toolTip != null) {
                            RUtils.toolTip.kill();
                        }
                    }

                    RMenu.this.shell.redraw(bounds.x, bounds.y, bounds.width, bounds.height, false);
                    RMenu.this.hovered = recent;
                }
            }
        });
        this.shell.addMouseTrackListener(new MouseTrackAdapter() {
            public void mouseExit(MouseEvent event) {
                if (RUtils.toolTip != null) {
                    RUtils.toolTip.kill();
                }

                if (RMenu.this.hovered != null) {
                    if (RMenu.this.hovered.getMenu() != null && RUtils.topMenu == RMenu.this.hovered.getMenu()) {
                        return;
                    }

                    Rectangle bounds = RMenu.this.hovered.getBounds();
                    RMenu.this.shell.redraw(bounds.x, bounds.y, bounds.width, bounds.height, false);
                    RMenu.this.hovered = null;
                }

            }

            public void mouseHover(MouseEvent event) {
                Iterator var3 = RMenu.this.items.iterator();

                while(true) {
                    RMenuItem item;
                    Rectangle bounds;
                    do {
                        do {
                            do {
                                do {
                                    do {
                                        if (!var3.hasNext()) {
                                            if (RMenu.this.main == null) {
                                                return;
                                            }

                                            var3 = RMenu.this.recents.iterator();

                                            while(var3.hasNext()) {
                                                item = (RMenuItem)var3.next();
                                                bounds = item.getBounds();
                                                if (bounds.contains(event.x, event.y)) {
                                                    item.getToolTip().show(RMenu.this.shell.toDisplay(event.x, event.y));
                                                }
                                            }

                                            return;
                                        }

                                        item = (RMenuItem)var3.next();
                                    } while(item.isSeparator());

                                    bounds = item.getBounds();
                                } while(!bounds.contains(event.x, event.y));

                                if (item.getToolTip() != null) {
                                    item.getToolTip().show(RMenu.this.shell.toDisplay(event.x, event.y));
                                }
                            } while(!item.getEnabled());
                        } while(item.getMenu() == null);
                    } while(item.getMenu().getVisible());

                    while(RUtils.topMenu != null && RUtils.topMenu != RMenu.this) {
                        RMenu topMenu = RUtils.topMenu;
                        topMenu.setVisible(false);
                        RUtils.topMenu = topMenu.getParent().getParent();
                    }

                    item.getMenu().setLocation(RMenu.this.shell.toDisplay(bounds.x + bounds.width, bounds.y));
                    item.getMenu().setVisible(true);
                }
            }
        });
        Rectangle limit;
        if (this.main == null && this.parent == null) {
            limit = this.shell.getMonitor().getBounds();
            if (this.bounds.x + this.bounds.width > limit.width) {
                this.bounds.x = limit.width - this.bounds.width;
            }

            if (this.bounds.y + this.bounds.height > limit.height) {
                this.bounds.y = limit.height - this.bounds.height;
            }
        } else if (this.parent != null && !this.parent.getParent().isMain()) {
            limit = this.shell.getMonitor().getBounds();
            if (this.bounds.x + this.bounds.width > limit.width) {
                Rectangle var10000 = this.bounds;
                var10000.x -= this.parent.getParent().getBounds().width + this.bounds.width - 4;
            }

            if (this.bounds.y + this.bounds.height > limit.height) {
                this.bounds.y = limit.height - this.bounds.height;
            }
        }

        this.shell.setLocation(this.bounds.x, this.bounds.y);
        this.shell.setVisible(true);
    }

    protected void kill() {
        if (this.shell != null) {
            this.shell.dispose();
        }

        RUtils.topMenu = null;
        this.hovered = null;
    }

    protected boolean isMain() {
        return this.main != null;
    }

    protected Shell getShell() {
        return this.shell;
    }

    protected void setParent(RMenuItem parent) {
        this.parent = parent;
    }

    protected void addMenuItem(RMenuItem item, int index) {
        this.items.add(index, item);
    }

    protected RMenuItem getHovered() {
        return this.hovered;
    }
}
