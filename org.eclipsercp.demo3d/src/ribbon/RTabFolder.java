package ribbon;

import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.*;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public final class RTabFolder {
    private Composite control;
    private RMainButton main;
    private RHelpButton help;
    private RExpander expander;
    private List<RTabItem> items;
    private int indent;
    private boolean expanded = true;
    private RTabItem hovered;
    private RTabItem clicked;
    private ExpandListener listener;
    private List<SelectionListener> listeners;
    private RMenu rMenu;
    private Menu menu;

    public RTabFolder(Composite parent) {
        this.setInternalFont((Font)null);
        this.control = new Composite(parent, 536870912);
        this.control.setBackground(RUtils.BACK_COLOR);
        this.control.setLayout(new FolderLayout());
        this.control.addPaintListener(event -> {
            GC gc = event.gc;
            gc.setFont(RUtils.initFont);
            gc.setForeground(RUtils.LINE_COLOR);
            gc.drawLine(0, 26, RTabFolder.this.getBounds().width, 26);
            int x;
            byte yx;
            int width;
            int y;
            if (RTabFolder.this.main != null) {
                Rectangle bounds = RTabFolder.this.main.getBounds();
                x = bounds.x;
                yx = 1;
                width = bounds.width;
                if (!RTabFolder.this.main.getHovered()) {
                    gc.setBackground(RUtils.MAIN_COLOR);
                    gc.fillRectangle(x, yx + 1, width - 3, 24);
                    gc.setForeground(RUtils.MAIN_BORDER);
                    gc.drawLine(x, yx, x + width - 5, yx);
                    gc.drawLine(x + width - 4, yx + 1, x + width - 4, yx + 1);
                    gc.drawLine(x + width - 3, yx + 2, x + width - 3, yx + 26 - 2);
                } else {
                    gc.setBackground(RUtils.MAIN_COLOR);
                    gc.fillRectangle(x, yx + 1, width - 3, 24);
                    gc.setForeground(RUtils.MAIN_BORDER);
                    gc.drawLine(x, yx, x + width - 5, yx);
                    gc.drawLine(x + width - 4, yx + 1, x + width - 4, yx + 1);
                    gc.drawLine(x + width - 3, yx + 2, x + width - 3, yx + 26 - 2);
                    gc.setForeground(RUtils.MAIN_INSIDE);
                    gc.drawLine(x, yx + 1, x + width - 5, yx + 1);
                    gc.drawLine(x, yx + 2, x, yx + 26 - 2);
                    gc.drawLine(x + width - 5, yx + 2, x + width - 5, yx + 2);
                    gc.drawLine(x + width - 4, yx + 2, x + width - 4, yx + 26 - 2);
                }

                y = 13 - gc.stringExtent(RTabFolder.this.main.getText()).y / 2;
                gc.setForeground(RUtils.TAB_CLICKED);
                gc.drawString(RTabFolder.this.main.getText(), x + 10 + 6, y, true);
            }

            Iterator var7 = RTabFolder.this.items.iterator();

            while(var7.hasNext()) {
                RTabItem item = (RTabItem)var7.next();
                Rectangle boundsx = item.getBounds();
                x = boundsx.x;
                yx = 1;
                width = boundsx.width;
                if (RTabFolder.this.expanded && item == RTabFolder.this.clicked && item != RTabFolder.this.hovered) {
                    gc.setBackground(RUtils.TAB_CLICKED);
                    gc.fillRectangle(x + 3, yx + 1, width - 6, 25);
                    gc.fillRectangle(x + 2, yx + 26 - 1, width - 4, 1);
                    gc.setForeground(RUtils.LINE_COLOR);
                    gc.drawLine(x + 4, yx, x + width - 5, yx);
                    gc.drawLine(x + 3, yx + 1, x + 3, yx + 1);
                    gc.drawLine(x + width - 4, yx + 1, x + width - 4, yx + 1);
                    gc.drawLine(x + 2, yx + 2, x + 2, yx + 26 - 2);
                    gc.drawLine(x + width - 3, yx + 2, x + width - 3, yx + 26 - 2);
                    gc.drawLine(x + 1, yx + 26 - 2, x + 1, yx + 26 - 2);
                    gc.drawLine(x + width - 2, yx + 26 - 2, x + width - 2, yx + 26 - 2);
                } else if ((!RTabFolder.this.expanded || item != RTabFolder.this.clicked) && item == RTabFolder.this.hovered) {
                    gc.setBackground(RUtils.TAB_HOVERED);
                    gc.fillRectangle(x + 3, yx + 1, width - 6, 24);
                    gc.setForeground(RUtils.HOVER_BORDER);
                    gc.drawLine(x + 4, yx, x + width - 5, yx);
                    gc.drawLine(x + 3, yx + 1, x + 3, yx + 1);
                    gc.drawLine(x + width - 4, yx + 1, x + width - 4, yx + 1);
                    gc.drawLine(x + 2, yx + 2, x + 2, yx + 26 - 2);
                    gc.drawLine(x + width - 3, yx + 2, x + width - 3, yx + 26 - 2);
                    gc.setForeground(RUtils.LINE_COLOR);
                    gc.drawLine(x + 2, yx + 26 - 1, x + width - 3, yx + 26 - 1);
                } else if (RTabFolder.this.expanded && item == RTabFolder.this.clicked && item == RTabFolder.this.hovered) {
                    gc.setBackground(RUtils.TAB_CLICKED);
                    gc.fillRectangle(x + 3, yx + 1, width - 6, 25);
                    gc.fillRectangle(x + 2, yx + 26 - 1, width - 4, 1);
                    gc.setForeground(RUtils.HOVER_BORDER);
                    gc.drawLine(x + 4, yx, x + width - 5, yx);
                    gc.drawLine(x + 3, yx + 1, x + 3, yx + 1);
                    gc.drawLine(x + width - 4, yx + 1, x + width - 4, yx + 1);
                    gc.setBackground(RUtils.LINE_COLOR);
                    gc.fillGradientRectangle(x + 2, yx + 2, 1, 23, true);
                    gc.fillGradientRectangle(x + width - 3, yx + 2, 1, 23, true);
                    gc.setForeground(RUtils.LINE_COLOR);
                    gc.drawLine(x + 1, yx + 26 - 2, x + 1, yx + 26 - 2);
                    gc.drawLine(x + width - 2, yx + 26 - 2, x + width - 2, yx + 26 - 2);
                }

                if (RTabFolder.this.expanded && item == RTabFolder.this.clicked) {
                    gc.setForeground(RUtils.MENU_LINE);
                    gc.drawLine(x + 1, yx + 3, x + 1, yx + 26 - 3);
                    gc.drawLine(x + width - 2, yx + 3, x + width - 2, yx + 26 - 3);
                }

                y = 13 - gc.stringExtent(item.getText()).y / 2;
                gc.setForeground(RTabFolder.this.expanded && item == RTabFolder.this.clicked ? RUtils.FONT_CLICKED : RUtils.FONT_COLOR);
                gc.drawString(item.getText(), x + 10 + 5, y, true);
            }

            if (RTabFolder.this.help != null) {
                RTabFolder.this.help.setBounds(new Rectangle(RTabFolder.this.control.getParent().getClientArea().width - 24, 2, 22, 22));
                RTabFolder.this.help.drawWidget(gc);
            }

            if (RTabFolder.this.expander != null) {
                RTabFolder.this.expander.setBounds(new Rectangle(RTabFolder.this.control.getParent().getClientArea().width - (RTabFolder.this.help == null ? 24 : 46), 2, 22, 22));
                RTabFolder.this.expander.drawWidget(gc);
            }

        });
        this.control.addMouseListener(new MouseListener() {
            private boolean flag;

            public void mouseDoubleClick(MouseEvent event) {
                Iterator var3 = RTabFolder.this.items.iterator();

                while(true) {
                    RTabItem item;
                    Rectangle bounds;
                    do {
                        if (!var3.hasNext()) {
                            return;
                        }

                        item = (RTabItem)var3.next();
                        bounds = item.getBounds();
                    } while(!bounds.contains(event.x, event.y));

                    RTabFolder.this.setExpanded(!RTabFolder.this.expanded);
                    Event e = new Event();
                    e.widget = RTabFolder.this.control;
                    e.x = event.x;
                    e.y = event.y;
                    SelectionEvent selectionEvent = new SelectionEvent(e);
                    selectionEvent.detail = RTabFolder.this.items.indexOf(item);
                    selectionEvent.text = item.getText();
                    Iterator var8 = RTabFolder.this.listeners.iterator();

                    while(var8.hasNext()) {
                        SelectionListener listener = (SelectionListener)var8.next();
                        listener.widgetDefaultSelected(selectionEvent);
                    }
                }
            }

            public void mouseDown(MouseEvent event) {
                if (RUtils.toolTip != null) {
                    RUtils.toolTip.kill();
                }

                if (event.button == 3 && event.x < RTabFolder.this.control.getClientArea().width && event.y < 26) {
                    this.flag = true;
                }

                if (event.button == 1) {
                    RTabFolder.this.control.forceFocus();
                    Rectangle bounds;
                    if (RTabFolder.this.main != null) {
                        bounds = RTabFolder.this.main.getBounds();
                        if (bounds.contains(event.x, event.y)) {
                            RTabFolder.this.main.onClick(event);
                        }
                    }

                    Iterator var3 = RTabFolder.this.items.iterator();

                    while(var3.hasNext()) {
                        RTabItem item = (RTabItem)var3.next();
                        Rectangle boundsx = item.getBounds();
                        if (boundsx.contains(event.x, event.y)) {
                            RTabFolder.this.selectTab(item);
                        }
                    }

                    if (RTabFolder.this.help != null) {
                        bounds = RTabFolder.this.help.getBounds();
                        if (bounds.contains(event.x, event.y)) {
                            RTabFolder.this.help.setClicked(true);
                            RTabFolder.this.control.redraw(bounds.x, bounds.y, bounds.width, bounds.height, false);
                        }
                    }

                    if (RTabFolder.this.expander != null) {
                        bounds = RTabFolder.this.expander.getBounds();
                        if (bounds.contains(event.x, event.y)) {
                            RTabFolder.this.expander.setClicked(true);
                            RTabFolder.this.control.redraw(0, 0, RTabFolder.this.getBounds().width, 27, false);
                        }
                    }

                }
            }

            public void mouseUp(MouseEvent event) {
                if (event.button == 3) {
                    if (event.x < RTabFolder.this.control.getClientArea().width && event.y < 26 && this.flag && RTabFolder.this.rMenu != null) {
                        RTabFolder.this.rMenu.setLocation(RTabFolder.this.control.toDisplay(event.x, event.y));
                        RTabFolder.this.rMenu.setVisible(true);
                    }

                    this.flag = false;
                }

                if (event.button == 1) {
                    Rectangle bounds;
                    if (RTabFolder.this.help != null && RTabFolder.this.help.getClicked()) {
                        bounds = RTabFolder.this.help.getBounds();
                        if (bounds.contains(event.x, event.y)) {
                            RTabFolder.this.control.redraw(bounds.x, bounds.y, bounds.width, bounds.height, false);
                            RTabFolder.this.help.onClick(event);
                        }

                        RTabFolder.this.help.setClicked(false);
                    }

                    if (RTabFolder.this.expander != null && RTabFolder.this.expander.getClicked()) {
                        bounds = RTabFolder.this.expander.getBounds();
                        if (bounds.contains(event.x, event.y)) {
                            RTabFolder.this.setExpanded(!RTabFolder.this.expanded);
                        }

                        RTabFolder.this.expander.setClicked(false);
                    }

                }
            }
        });
        this.control.addMouseMoveListener(new MouseMoveListener() {
            public void mouseMove(MouseEvent event) {
                Rectangle bounds;
                if (RTabFolder.this.main != null) {
                    bounds = RTabFolder.this.main.getBounds();
                    if (bounds.contains(event.x, event.y)) {
                        if (!RTabFolder.this.main.getHovered()) {
                            RTabFolder.this.control.redraw(bounds.x, bounds.y, bounds.width, bounds.height, false);
                            RTabFolder.this.main.setHovered(true);
                        }
                    } else if (RTabFolder.this.main.getHovered()) {
                        RTabFolder.this.control.redraw(bounds.x, bounds.y, bounds.width, bounds.height, false);
                        RTabFolder.this.main.setHovered(false);
                        if (RUtils.toolTip != null) {
                            RUtils.toolTip.kill();
                        }
                    }
                }

                Iterator var3 = RTabFolder.this.items.iterator();

                while(var3.hasNext()) {
                    RTabItem item = (RTabItem)var3.next();
                    Rectangle boundsx = item.getBounds();
                    if (boundsx.contains(event.x, event.y)) {
                        if (item != RTabFolder.this.hovered) {
                            if (RTabFolder.this.hovered != null) {
                                Rectangle origin = RTabFolder.this.hovered.getBounds();
                                RTabFolder.this.control.redraw(origin.x, origin.y, origin.width, origin.height, false);
                                RTabFolder.this.hovered = null;
                                if (RUtils.toolTip != null) {
                                    RUtils.toolTip.kill();
                                }
                            }

                            RTabFolder.this.control.redraw(boundsx.x, boundsx.y, boundsx.width, boundsx.height, false);
                            RTabFolder.this.hovered = item;
                        }
                    } else if (item == RTabFolder.this.hovered) {
                        RTabFolder.this.control.redraw(boundsx.x, boundsx.y, boundsx.width, boundsx.height, false);
                        RTabFolder.this.hovered = null;
                        if (RUtils.toolTip != null) {
                            RUtils.toolTip.kill();
                        }
                    }
                }

                if (RTabFolder.this.help != null) {
                    bounds = RTabFolder.this.help.getBounds();
                    if (bounds.contains(event.x, event.y)) {
                        if (!RTabFolder.this.help.getHovered()) {
                            RTabFolder.this.control.redraw(bounds.x, bounds.y, bounds.width, bounds.height, false);
                            RTabFolder.this.help.setHovered(true);
                        }
                    } else if (RTabFolder.this.help.getHovered()) {
                        RTabFolder.this.control.redraw(bounds.x, bounds.y, bounds.width, bounds.height, false);
                        RTabFolder.this.help.setHovered(false);
                        if (RUtils.toolTip != null) {
                            RUtils.toolTip.kill();
                        }
                    }
                }

                if (RTabFolder.this.expander != null) {
                    bounds = RTabFolder.this.expander.getBounds();
                    if (bounds.contains(event.x, event.y)) {
                        if (!RTabFolder.this.expander.getHovered()) {
                            RTabFolder.this.control.redraw(bounds.x, bounds.y, bounds.width, bounds.height, false);
                            RTabFolder.this.expander.setHovered(true);
                        }
                    } else if (RTabFolder.this.expander.getHovered()) {
                        RTabFolder.this.control.redraw(bounds.x, bounds.y, bounds.width, bounds.height, false);
                        RTabFolder.this.expander.setHovered(false);
                        if (RUtils.toolTip != null) {
                            RUtils.toolTip.kill();
                        }
                    }
                }

            }
        });
        this.control.addMouseTrackListener(new MouseTrackAdapter() {
            public void mouseExit(MouseEvent event) {
                if (RUtils.toolTip != null) {
                    RUtils.toolTip.kill();
                }

                Rectangle bounds;
                if (RTabFolder.this.main != null && RTabFolder.this.main.getHovered()) {
                    bounds = RTabFolder.this.main.getBounds();
                    RTabFolder.this.control.redraw(bounds.x, bounds.y, bounds.width, bounds.height, false);
                    RTabFolder.this.main.setHovered(false);
                }

                if (RTabFolder.this.hovered != null) {
                    bounds = RTabFolder.this.hovered.getBounds();
                    RTabFolder.this.control.redraw(bounds.x, bounds.y, bounds.width, bounds.height, false);
                    RTabFolder.this.hovered = null;
                }

                if (RTabFolder.this.help != null && RTabFolder.this.help.getHovered()) {
                    bounds = RTabFolder.this.help.getBounds();
                    RTabFolder.this.control.redraw(bounds.x, bounds.y, bounds.width, bounds.height, false);
                    RTabFolder.this.help.setHovered(false);
                }

                if (RTabFolder.this.expander != null && RTabFolder.this.expander.getHovered()) {
                    bounds = RTabFolder.this.expander.getBounds();
                    RTabFolder.this.control.redraw(bounds.x, bounds.y, bounds.width, bounds.height, false);
                    RTabFolder.this.expander.setHovered(false);
                }

            }

            public void mouseHover(MouseEvent event) {
                Rectangle bounds;
                if (RTabFolder.this.help != null) {
                    bounds = RTabFolder.this.help.getBounds();
                    if (bounds.contains(event.x, event.y) && RTabFolder.this.help.getToolTip() != null) {
                        RTabFolder.this.help.getToolTip().show(RTabFolder.this.control.toDisplay(bounds.x, 32));
                    }
                }

                if (RTabFolder.this.expander != null) {
                    bounds = RTabFolder.this.expander.getBounds();
                    if (bounds.contains(event.x, event.y) && RTabFolder.this.expander.getToolTip() != null) {
                        RTabFolder.this.expander.getToolTip().show(RTabFolder.this.control.toDisplay(bounds.x, 32));
                    }
                }

            }
        });
        this.control.addMouseWheelListener(new MouseWheelListener() {
            public void mouseScrolled(MouseEvent event) {
                if (RTabFolder.this.getBounds().contains(event.x, event.y)) {
                    int index = RTabFolder.this.getSelectionIndex();
                    RTabFolder var10000;
                    List var10001;
                    if (event.count > 0) {
                        if (index > 0) {
                            var10000 = RTabFolder.this;
                            var10001 = RTabFolder.this.items;
                            --index;
                            var10000.selectTab((RTabItem)var10001.get(index));
                        }
                    } else if (index < RTabFolder.this.items.size() - 1) {
                        var10000 = RTabFolder.this;
                        var10001 = RTabFolder.this.items;
                        ++index;
                        var10000.selectTab((RTabItem)var10001.get(index));
                    }
                }

            }
        });
        this.items = new ArrayList();
        this.listeners = new ArrayList();
        Listener listener = new Listener() {
            public void handleEvent(Event event) {
                while(RUtils.topMenu != null) {
                    RMenu topMenu = RUtils.topMenu;
                    topMenu.setVisible(false);
                    if (topMenu.getParent() != null) {
                        RUtils.topMenu = topMenu.getParent().getParent();
                    }
                }

            }
        };
        Shell shell = Display.getCurrent().getShells()[0];
        shell.addListener(10, listener);
        shell.addListener(11, listener);
        shell.addListener(27, listener);
        shell.getDisplay().addFilter(3, new Listener() {
            public void handleEvent(Event event) {
                Point point = ((Control)event.widget).toDisplay(event.x, event.y);

                while(RUtils.topMenu != null) {
                    RMenu topMenu = RUtils.topMenu;
                    if (topMenu.getBounds().contains(point)) {
                        break;
                    }

                    topMenu.setVisible(false);
                    if (topMenu.getParent() != null) {
                        RMenu parent = topMenu.getParent().getParent();
                        RUtils.topMenu = parent;
                        if (parent.getBounds().contains(point)) {
                            break;
                        }
                    }
                }

            }
        });
    }

    public Composite getParent() {
        return this.control.getParent();
    }

    public RMainButton getMainButton() {
        return this.main;
    }

    public RHelpButton getHelpButton() {
        return this.help;
    }

    public RExpander getExpander() {
        return this.expander;
    }

    public RTabItem getItem(int index) {
        return (RTabItem)this.items.get(index);
    }

    public int indexOf(RTabItem item) {
        return this.items.indexOf(item);
    }

    public int getItemCount() {
        return this.items.size();
    }

    public List<RTabItem> getItems() {
        return this.items;
    }

    public Font getInternalFont() {
        return RUtils.initFont;
    }

    public void setInternalFont(Font font) {
        RUtils.initFont = font == null ? Display.getCurrent().getSystemFont() : font;
        FontData data = RUtils.initFont.getFontData()[0];
        data.setStyle(1);
        RUtils.blodFont = new Font(Display.getCurrent(), data);
    }

    public int getFirstTabIndent() {
        return this.indent;
    }

    public void setFirstTabIndent(int indent) {
        this.indent = Math.abs(indent);
        this.updateBounds();
        this.control.redraw(0, 0, this.getBounds().width, 27, false);
    }

    public boolean getExpanded() {
        return this.expanded;
    }

    public void setExpanded(boolean expanded) {
        if (this.expanded != expanded) {
            this.expanded = expanded;
            this.control.layout(false);
            this.control.redraw(0, 0, this.getBounds().width, 27, false);
            this.control.getParent().layout(true);
        }

        if (this.listener != null) {
            Event e = new Event();
            e.widget = this.control;
            if (expanded) {
                this.listener.itemExpanded(new ExpandEvent(e));
            } else {
                this.listener.itemCollapsed(new ExpandEvent(e));
            }
        }

    }

    public RTabItem getSelection() {
        return this.clicked;
    }

    public int getSelectionIndex() {
        return this.items.indexOf(this.clicked);
    }

    public void setSelection(RTabItem item) {
        this.selectTab(item);
    }

    public void setSelection(int index) {
        this.selectTab((RTabItem)this.items.get(index));
    }

    public void setExpandListener(ExpandListener listener) {
        this.listener = listener;
    }

    public RMenu getrMenu() {
        return this.rMenu;
    }

    public void setrMenu(RMenu rMenu) {
        if (this.rMenu != null) {
            this.rMenu.dispose();
        }

        this.rMenu = rMenu;
    }

    public void addSelectionListener(SelectionListener listener) {
        if (!this.listeners.contains(listener)) {
            this.listeners.add(listener);
        }
    }

    public void removeSelectionListener(SelectionListener listener) {
        this.listeners.remove(listener);
    }

    public Rectangle getBounds() {
        Rectangle bounds = this.control.getClientArea();
        return new Rectangle(bounds.x, bounds.y, bounds.width, this.expanded ? 119 : 27);
    }

    public void dispose() {
        Iterator var2 = this.items.iterator();

        while(var2.hasNext()) {
            RTabItem item = (RTabItem)var2.next();
            item.getControl().dispose();
        }

        this.items.clear();
        this.control.dispose();
    }

    protected Composite getControl() {
        return this.control;
    }

    protected void setMainButton(RMainButton main) {
        this.main = main;
        this.updateBounds();
    }

    protected void setMenu(Menu menu) {
        this.menu=menu;
        this.updateBounds();
    }

    protected void setHelpButton(RHelpButton help) {
        this.help = help;
    }

    protected void setExpander(RExpander expander) {
        this.expander = expander;
    }

    protected void addNewTab(RTabItem item, int index) {
        this.items.add(index, item);
        if (index == 0) {
            this.clicked = item;
        }

        this.updateBounds();
    }

    protected void updateBounds() {
        GC gc = new GC(this.control);
        gc.setFont(RUtils.initFont);
        int x = 0;
        if (this.main != null) {
            int width = gc.stringExtent(this.main.getText()).x + 20 + 15;
            Rectangle bounds = new Rectangle(x, 0, width, 26);
            this.main.setBounds(bounds);
            x += width + 1;
        }

        x += this.indent;

        int width;
        for(Iterator var8 = this.items.iterator(); var8.hasNext(); x += width + 1) {
            RTabItem item = (RTabItem)var8.next();
            width = gc.stringExtent(item.getText()).x + 20 + 10;
            Rectangle bounds = new Rectangle(x, 0, width, 26);
            item.setBounds(bounds);
        }

        gc.dispose();
    }

    protected void selectTab(RTabItem item) {
        if (item != this.clicked) {
            this.clicked.getControl().setVisible(false);
            this.clicked = item;
            this.clicked.getControl().setVisible(true);
            this.control.layout(false);
            if (this.expanded) {
                this.control.redraw();
            } else {
                this.control.redraw(0, 0, this.getBounds().width, 27, false);
            }

            Event e = new Event();
            e.widget = this.control;
            SelectionEvent selectionEvent = new SelectionEvent(e);
            selectionEvent.detail = this.items.indexOf(item);
            selectionEvent.text = item.getText();
            Iterator var5 = this.listeners.iterator();

            while(var5.hasNext()) {
                SelectionListener listener = (SelectionListener)var5.next();
                listener.widgetSelected(selectionEvent);
            }

        }
    }

    private class FolderLayout extends Layout {
        private FolderLayout() {
        }

        protected Point computeSize(Composite composite, int hint, int hint2, boolean flushCache) {
            Rectangle bounds = RTabFolder.this.getBounds();
            return new Point(bounds.width, bounds.height);
        }

        protected void layout(Composite composite, boolean flushCache) {
            Iterator var4 = RTabFolder.this.items.iterator();

            while(var4.hasNext()) {
                RTabItem item = (RTabItem)var4.next();
                if (!RTabFolder.this.expanded) {
                    item.getControl().setVisible(false);
                } else if (item == RTabFolder.this.clicked) {
                    Composite control = item.getControl();
                    control.setLocation(new Point(0, 27));
                    control.setSize(RTabFolder.this.getControl().getClientArea().width, 92);
                    control.setVisible(true);
                }
            }

        }
    }
}

