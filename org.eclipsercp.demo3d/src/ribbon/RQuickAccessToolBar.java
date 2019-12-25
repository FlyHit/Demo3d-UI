package ribbon;

import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.internal.win32.OS;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.*;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public final class RQuickAccessToolBar {
    private RWindow parent;
    private boolean above;
    private boolean visible = true;
    private List<RQuickAccessToolItem> items;
    private RQuickAccessToolItem hovered;
    private RQuickAccessToolItem clicked;
    private RQuickAccessToolItem popuped;
    private Rectangle drop_bounds;
    private boolean drop_hovered;
    private boolean drop_clicked;
    private boolean drop_popuped;
    private RToolTip toolTip;
    private RMenu menu;
    private Shell top;
    private Shell bar;
    private Composite barpart;

    public RQuickAccessToolBar(RWindow parent, int style) {
        try {
            Field field = RWindow.class.getDeclaredField("bar");
            field.setAccessible(true);
            field.set(parent, this);
            field = RWindow.class.getDeclaredField("barpart");
            field.setAccessible(true);
            this.barpart = (Composite)field.get(parent);
        } catch (Exception var4) {
            var4.printStackTrace();
        }

        this.parent = parent;
        this.above = (style & 1024) == 0;
        this.items = new ArrayList();
        this.bar = new Shell(parent.getShell(), 524296);
        this.top = new Shell(parent.getShell(), 524296);
        this.top.setAlpha(1);
        this.bar.addPaintListener(new PaintListener() {
            public void paintControl(PaintEvent event) {
                GC gc = event.gc;
                Region region = new Region();
                gc.setForeground(RUtils.FONT_GROUP);
                RQuickAccessToolBar.this.drawLine(gc, region, 2, 4, 2, 14);
                gc.setForeground(RUtils.BOX_TL_03);
                RQuickAccessToolBar.this.drawLine(gc, region, 1, 4, 1, 14);
                RQuickAccessToolBar.this.drawLine(gc, region, 3, 4, 3, 14);
                RQuickAccessToolBar.this.drawLine(gc, region, 2, 3, 2, 3);
                RQuickAccessToolBar.this.drawLine(gc, region, 2, 15, 2, 15);
                int width = 7;

                int itemWidth;
                for(Iterator var6 = RQuickAccessToolBar.this.items.iterator(); var6.hasNext(); width += itemWidth) {
                    RQuickAccessToolItem item = (RQuickAccessToolItem)var6.next();
                    Image image = item.getImage();
                    itemWidth = image == null ? 22 : image.getBounds().width + 6;
                    item.setBounds(new Rectangle(width, 0, itemWidth, 22));
                    item.drawWidget(gc, region);
                }

                RQuickAccessToolBar.this.drop_bounds = new Rectangle(width, 0, 13, 22);
                if (RQuickAccessToolBar.this.drop_hovered) {
                    gc.setForeground(!RQuickAccessToolBar.this.drop_clicked ? RUtils.HOVER_HIGH_LIGHT : RUtils.CLICK_HIGH_LIGHT);
                    gc.setBackground(!RQuickAccessToolBar.this.drop_clicked ? RUtils.HOVER_COLOR : RUtils.CLICK_COLOR);
                    gc.fillGradientRectangle(width + 1, 1, 11, 7, true);
                    gc.setForeground(!RQuickAccessToolBar.this.drop_clicked ? RUtils.HOVER_DARK_LIGHT : RUtils.CLICK_DARK_LIGHT);
                    gc.fillGradientRectangle(width + 1, 8, 11, 13, true);
                    gc.setForeground(!RQuickAccessToolBar.this.drop_clicked ? RUtils.HOVER_BORDER : RUtils.CLICK_BORDER);
                    gc.drawLine(width + 1, 0, width + 11, 0);
                    gc.drawLine(width + 1, 1, width + 1, 1);
                    gc.drawLine(width + 11, 1, width + 11, 1);
                    gc.drawLine(width, 1, width, 20);
                    gc.drawLine(width + 12, 1, width + 12, 20);
                    gc.drawLine(width + 1, 20, width + 1, 20);
                    gc.drawLine(width + 11, 20, width + 11, 20);
                    gc.drawLine(width + 1, 21, width + 11, 21);
                    if (!RQuickAccessToolBar.this.drop_clicked) {
                        gc.setForeground(RUtils.HOVER_INSIDE);
                        gc.drawLine(width + 1, 2, width + 1, 19);
                        gc.drawLine(width + 11, 2, width + 11, 19);
                    }

                    region.add(RQuickAccessToolBar.this.drop_bounds);
                    region.subtract(width, 0, 1, 1);
                    region.subtract(width + 12, 0, 1, 1);
                    region.subtract(width, 21, 1, 1);
                    region.subtract(width + 12, 21, 1, 1);
                }

                gc.setForeground(RQuickAccessToolBar.this.drop_hovered ? RUtils.HOVER_INSIDE : RUtils.BOX_TL_03);
                RQuickAccessToolBar.this.drawLine(gc, region, width + 3, 9, width + 9, 9);
                RQuickAccessToolBar.this.drawLine(gc, region, width + 4, 12, width + 8, 12);
                RQuickAccessToolBar.this.drawLine(gc, region, width + 5, 13, width + 7, 13);
                RQuickAccessToolBar.this.drawLine(gc, region, width + 6, 14, width + 6, 14);
                gc.setForeground(Display.getCurrent().getSystemColor(2));
                RQuickAccessToolBar.this.drawLine(gc, region, width + 3, 8, width + 9, 8);
                RQuickAccessToolBar.this.drawLine(gc, region, width + 4, 11, width + 8, 11);
                RQuickAccessToolBar.this.drawLine(gc, region, width + 5, 12, width + 7, 12);
                RQuickAccessToolBar.this.drawLine(gc, region, width + 6, 13, width + 6, 13);
                width += 16;
                gc.setForeground(RUtils.FONT_GROUP);
                RQuickAccessToolBar.this.drawLine(gc, region, width + 2, 4, width + 2, 14);
                gc.setForeground(RUtils.BOX_TL_03);
                RQuickAccessToolBar.this.drawLine(gc, region, width + 1, 4, width + 1, 14);
                RQuickAccessToolBar.this.drawLine(gc, region, width + 3, 4, width + 3, 14);
                RQuickAccessToolBar.this.drawLine(gc, region, width + 2, 3, width + 2, 3);
                RQuickAccessToolBar.this.drawLine(gc, region, width + 2, 15, width + 2, 15);
                width += 13;
                RQuickAccessToolBar.this.bar.setRegion(region);
                RQuickAccessToolBar.this.top.setSize(width, 22);
                RQuickAccessToolBar.this.getParent().getShell().setText(RQuickAccessToolBar.this.getParent().getShell().getText());
                RQuickAccessToolBar.this.getParent().getShell().setMinimumSize(width + 46 + OS.GetSystemMetrics(30) * 3, 20);
            }
        });
        this.top.addMouseListener(new MouseAdapter() {
            public void mouseDown(MouseEvent event) {
                RQuickAccessToolBar.this.getParent().getShell().forceFocus();
                if (RUtils.toolTip != null) {
                    RUtils.toolTip.kill();
                }

                RQuickAccessToolItem item;
                Iterator var3;
                Rectangle bounds;
                if (event.button == 3) {
                    var3 = RQuickAccessToolBar.this.items.iterator();

                    while(var3.hasNext()) {
                        item = (RQuickAccessToolItem)var3.next();
                        if (item.getEnabled()) {
                            bounds = item.getBounds();
                            if (bounds.contains(event.x, event.y)) {
                                RQuickAccessToolBar.this.popuped = item;
                            }
                        }
                    }

                    if (RQuickAccessToolBar.this.drop_bounds.contains(event.x, event.y)) {
                        RQuickAccessToolBar.this.drop_popuped = true;
                    }
                }

                if (event.button == 1) {
                    var3 = RQuickAccessToolBar.this.items.iterator();

                    while(var3.hasNext()) {
                        item = (RQuickAccessToolItem)var3.next();
                        if (item.getEnabled()) {
                            bounds = item.getBounds();
                            if (bounds.contains(event.x, event.y)) {
                                RQuickAccessToolBar.this.clicked = item;
                                RQuickAccessToolBar.this.bar.redraw(bounds.x, bounds.y, bounds.width, bounds.height, false);
                            }
                        }
                    }

                    if (RQuickAccessToolBar.this.drop_bounds.contains(event.x, event.y)) {
                        RQuickAccessToolBar.this.drop_clicked = true;
                        RQuickAccessToolBar.this.bar.redraw(RQuickAccessToolBar.this.drop_bounds.x, RQuickAccessToolBar.this.drop_bounds.y, RQuickAccessToolBar.this.drop_bounds.width, RQuickAccessToolBar.this.drop_bounds.height, false);
                    }

                }
            }

            public void mouseUp(MouseEvent event) {
                if (event.button == 3) {
                    if (RQuickAccessToolBar.this.popuped != null) {
                        if (RQuickAccessToolBar.this.popuped.getBounds().contains(event.x, event.y) && RQuickAccessToolBar.this.popuped.getMenu() != null) {
                            RQuickAccessToolBar.this.popuped.getMenu().setLocation(RQuickAccessToolBar.this.top.toDisplay(event.x, event.y));
                            RQuickAccessToolBar.this.popuped.getMenu().setVisible(true);
                        }

                        RQuickAccessToolBar.this.popuped = null;
                    }

                    if (RQuickAccessToolBar.this.drop_popuped) {
                        if (RQuickAccessToolBar.this.drop_bounds.contains(event.x, event.y) && RQuickAccessToolBar.this.menu != null) {
                            RQuickAccessToolBar.this.menu.setLocation(RQuickAccessToolBar.this.top.toDisplay(event.x, event.y));
                            RQuickAccessToolBar.this.menu.setVisible(true);
                        }

                        RQuickAccessToolBar.this.drop_popuped = false;
                    }
                }

                if (event.button == 1) {
                    if (RQuickAccessToolBar.this.clicked != null) {
                        Rectangle bounds = RQuickAccessToolBar.this.clicked.getBounds();
                        if (bounds.contains(event.x, event.y)) {
                            RQuickAccessToolBar.this.clicked.setSelection(!RQuickAccessToolBar.this.clicked.getSelection());
                            RQuickAccessToolBar.this.clicked.onClick(event);
                        }

                        RQuickAccessToolBar.this.clicked = null;
                    }

                    if (RQuickAccessToolBar.this.drop_clicked) {
                        if (RQuickAccessToolBar.this.drop_bounds.contains(event.x, event.y)) {
                            RQuickAccessToolBar.this.bar.redraw(RQuickAccessToolBar.this.drop_bounds.x, RQuickAccessToolBar.this.drop_bounds.y, RQuickAccessToolBar.this.drop_bounds.width, RQuickAccessToolBar.this.drop_bounds.height, false);
                            if (RQuickAccessToolBar.this.menu != null) {
                                RQuickAccessToolBar.this.menu.setLocation(RQuickAccessToolBar.this.top.toDisplay(RQuickAccessToolBar.this.drop_bounds.x, RQuickAccessToolBar.this.drop_bounds.y + 22));
                                RQuickAccessToolBar.this.menu.setVisible(true);
                            }
                        }

                        RQuickAccessToolBar.this.drop_clicked = false;
                    }

                }
            }
        });
        this.top.addMouseMoveListener(new MouseMoveListener() {
            public void mouseMove(MouseEvent event) {
                Iterator var3 = RQuickAccessToolBar.this.items.iterator();

                while(var3.hasNext()) {
                    RQuickAccessToolItem item = (RQuickAccessToolItem)var3.next();
                    if (item.getEnabled()) {
                        Rectangle bounds = item.getBounds();
                        if (bounds.contains(event.x, event.y)) {
                            if (item != RQuickAccessToolBar.this.hovered) {
                                if (RQuickAccessToolBar.this.hovered != null) {
                                    Rectangle origin = RQuickAccessToolBar.this.hovered.getBounds();
                                    RQuickAccessToolBar.this.bar.redraw(origin.x, origin.y, origin.width, origin.height, false);
                                    RQuickAccessToolBar.this.hovered = null;
                                    if (RUtils.toolTip != null) {
                                        RUtils.toolTip.kill();
                                    }
                                }

                                RQuickAccessToolBar.this.bar.redraw(bounds.x, bounds.y, bounds.width, bounds.height, false);
                                RQuickAccessToolBar.this.hovered = item;
                            }
                        } else if (item == RQuickAccessToolBar.this.hovered) {
                            RQuickAccessToolBar.this.bar.redraw(bounds.x, bounds.y, bounds.width, bounds.height, false);
                            RQuickAccessToolBar.this.hovered = null;
                            if (RUtils.toolTip != null) {
                                RUtils.toolTip.kill();
                            }
                        }
                    }
                }

                if (RQuickAccessToolBar.this.drop_bounds.contains(event.x, event.y)) {
                    if (!RQuickAccessToolBar.this.drop_hovered) {
                        RQuickAccessToolBar.this.bar.redraw(RQuickAccessToolBar.this.drop_bounds.x, RQuickAccessToolBar.this.drop_bounds.y, RQuickAccessToolBar.this.drop_bounds.width, RQuickAccessToolBar.this.drop_bounds.height, false);
                        RQuickAccessToolBar.this.drop_hovered = true;
                    }
                } else if (RQuickAccessToolBar.this.drop_hovered) {
                    RQuickAccessToolBar.this.bar.redraw(RQuickAccessToolBar.this.drop_bounds.x, RQuickAccessToolBar.this.drop_bounds.y, RQuickAccessToolBar.this.drop_bounds.width, RQuickAccessToolBar.this.drop_bounds.height, false);
                    RQuickAccessToolBar.this.drop_hovered = false;
                    if (RUtils.toolTip != null) {
                        RUtils.toolTip.kill();
                    }
                }

            }
        });
        this.top.addMouseTrackListener(new MouseTrackAdapter() {
            public void mouseExit(MouseEvent event) {
                if (RUtils.toolTip != null) {
                    RUtils.toolTip.kill();
                }

                if (RQuickAccessToolBar.this.hovered != null) {
                    Rectangle bounds = RQuickAccessToolBar.this.hovered.getBounds();
                    RQuickAccessToolBar.this.bar.redraw(bounds.x, bounds.y, bounds.width, bounds.height, false);
                    RQuickAccessToolBar.this.hovered = null;
                }

                if (RQuickAccessToolBar.this.drop_hovered) {
                    RQuickAccessToolBar.this.bar.redraw(RQuickAccessToolBar.this.drop_bounds.x, RQuickAccessToolBar.this.drop_bounds.y, RQuickAccessToolBar.this.drop_bounds.width, RQuickAccessToolBar.this.drop_bounds.height, false);
                    RQuickAccessToolBar.this.drop_hovered = false;
                }

            }

            public void mouseHover(MouseEvent event) {
                Iterator var3 = RQuickAccessToolBar.this.items.iterator();

                while(var3.hasNext()) {
                    RQuickAccessToolItem item = (RQuickAccessToolItem)var3.next();
                    Rectangle bounds = item.getBounds();
                    if (bounds.contains(event.x, event.y) && item.getToolTip() != null) {
                        item.getToolTip().show(RQuickAccessToolBar.this.top.toDisplay(bounds.x, 30));
                    }
                }

                if (RQuickAccessToolBar.this.drop_bounds.contains(event.x, event.y) && RQuickAccessToolBar.this.toolTip != null) {
                    RQuickAccessToolBar.this.toolTip.show(RQuickAccessToolBar.this.bar.toDisplay(RQuickAccessToolBar.this.drop_bounds.x, 30));
                }

            }
        });
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
        this.top.addListener(10, listener);
        this.top.addListener(11, listener);
        this.top.addListener(27, listener);
        this.barpart.setBackground(RUtils.BACK_COLOR);
        this.barpart.addPaintListener(new PaintListener() {
            public void paintControl(PaintEvent event) {
                GC gc = event.gc;
                gc.setForeground(RUtils.LINE_COLOR);
                gc.drawLine(0, 24, RQuickAccessToolBar.this.barpart.getClientArea().width, 24);
                int x = 2;

                int itemWidth;
                for(Iterator var5 = RQuickAccessToolBar.this.items.iterator(); var5.hasNext(); x += itemWidth) {
                    RQuickAccessToolItem item = (RQuickAccessToolItem)var5.next();
                    Image image = item.getImage();
                    itemWidth = image == null ? 22 : image.getBounds().width + 6;
                    item.setBounds(new Rectangle(x, 1, itemWidth, 22));
                    item.drawWidget(gc, (Region)null);
                }

                RQuickAccessToolBar.this.drop_bounds = new Rectangle(x, 1, 13, 22);
                if (RQuickAccessToolBar.this.drop_hovered) {
                    gc.setForeground(!RQuickAccessToolBar.this.drop_clicked ? RUtils.HOVER_HIGH_LIGHT : RUtils.CLICK_HIGH_LIGHT);
                    gc.setBackground(!RQuickAccessToolBar.this.drop_clicked ? RUtils.HOVER_COLOR : RUtils.CLICK_COLOR);
                    gc.fillGradientRectangle(x + 1, 2, 11, 7, true);
                    gc.setForeground(!RQuickAccessToolBar.this.drop_clicked ? RUtils.HOVER_DARK_LIGHT : RUtils.CLICK_DARK_LIGHT);
                    gc.fillGradientRectangle(x + 1, 9, 11, 13, true);
                    gc.setForeground(!RQuickAccessToolBar.this.drop_clicked ? RUtils.HOVER_BORDER : RUtils.CLICK_BORDER);
                    gc.drawLine(x + 1, 1, x + 11, 1);
                    gc.drawLine(x + 1, 2, x + 1, 2);
                    gc.drawLine(x + 11, 2, x + 11, 2);
                    gc.drawLine(x, 2, x, 21);
                    gc.drawLine(x + 12, 2, x + 12, 21);
                    gc.drawLine(x + 1, 21, x + 1, 21);
                    gc.drawLine(x + 11, 21, x + 11, 21);
                    gc.drawLine(x + 1, 22, x + 11, 22);
                    if (!RQuickAccessToolBar.this.drop_clicked) {
                        gc.setForeground(RUtils.HOVER_INSIDE);
                        gc.drawLine(x + 1, 3, x + 1, 20);
                        gc.drawLine(x + 11, 3, x + 11, 20);
                    }
                }

                gc.setForeground(RQuickAccessToolBar.this.drop_hovered ? RUtils.HOVER_INSIDE : RUtils.LINE_INSIDE);
                gc.drawLine(x + 3, 10, x + 9, 10);
                gc.drawLine(x + 4, 13, x + 8, 13);
                gc.drawLine(x + 5, 14, x + 7, 14);
                gc.drawLine(x + 6, 15, x + 6, 15);
                gc.setForeground(Display.getCurrent().getSystemColor(2));
                gc.drawLine(x + 3, 9, x + 9, 9);
                gc.drawLine(x + 4, 12, x + 8, 12);
                gc.drawLine(x + 5, 13, x + 7, 13);
                gc.drawLine(x + 6, 14, x + 6, 14);
            }
        });
        this.barpart.addMouseListener(new MouseAdapter() {
            private boolean flag;

            public void mouseDown(MouseEvent event) {
                if (RUtils.toolTip != null) {
                    RUtils.toolTip.kill();
                }

                RQuickAccessToolItem item;
                Iterator var3;
                Rectangle bounds;
                if (event.button == 3) {
                    this.flag = true;
                    var3 = RQuickAccessToolBar.this.items.iterator();

                    while(var3.hasNext()) {
                        item = (RQuickAccessToolItem)var3.next();
                        if (item.getEnabled()) {
                            bounds = item.getBounds();
                            if (bounds.contains(event.x, event.y)) {
                                RQuickAccessToolBar.this.popuped = item;
                                this.flag = false;
                            }
                        }
                    }

                    if (RQuickAccessToolBar.this.drop_bounds.contains(event.x, event.y)) {
                        RQuickAccessToolBar.this.drop_popuped = true;
                        this.flag = false;
                    }
                }

                if (event.button == 1) {
                    var3 = RQuickAccessToolBar.this.items.iterator();

                    while(var3.hasNext()) {
                        item = (RQuickAccessToolItem)var3.next();
                        if (item.getEnabled()) {
                            bounds = item.getBounds();
                            if (bounds.contains(event.x, event.y)) {
                                RQuickAccessToolBar.this.clicked = item;
                                RQuickAccessToolBar.this.barpart.redraw(bounds.x, bounds.y, bounds.width, bounds.height, false);
                            }
                        }
                    }

                    if (RQuickAccessToolBar.this.drop_bounds.contains(event.x, event.y)) {
                        RQuickAccessToolBar.this.drop_clicked = true;
                        RQuickAccessToolBar.this.barpart.redraw(RQuickAccessToolBar.this.drop_bounds.x, RQuickAccessToolBar.this.drop_bounds.y, RQuickAccessToolBar.this.drop_bounds.width, RQuickAccessToolBar.this.drop_bounds.height, false);
                    }

                }
            }

            public void mouseUp(MouseEvent event) {
                if (event.button == 3) {
                    if (RQuickAccessToolBar.this.popuped != null) {
                        if (RQuickAccessToolBar.this.popuped.getBounds().contains(event.x, event.y) && RQuickAccessToolBar.this.popuped.getMenu() != null) {
                            RQuickAccessToolBar.this.popuped.getMenu().setLocation(RQuickAccessToolBar.this.barpart.toDisplay(event.x, event.y));
                            RQuickAccessToolBar.this.popuped.getMenu().setVisible(true);
                        }

                        RQuickAccessToolBar.this.popuped = null;
                    }

                    if (RQuickAccessToolBar.this.drop_popuped) {
                        if (RQuickAccessToolBar.this.drop_bounds.contains(event.x, event.y) && RQuickAccessToolBar.this.menu != null) {
                            RQuickAccessToolBar.this.menu.setLocation(RQuickAccessToolBar.this.barpart.toDisplay(event.x, event.y));
                            RQuickAccessToolBar.this.menu.setVisible(true);
                        }

                        RQuickAccessToolBar.this.drop_popuped = false;
                    }

                    if (this.flag) {
                        Iterator var3 = RQuickAccessToolBar.this.items.iterator();

                        while(var3.hasNext()) {
                            RQuickAccessToolItem item = (RQuickAccessToolItem)var3.next();
                            if (item.getBounds().contains(event.x, event.y)) {
                                this.flag = false;
                            }
                        }

                        if (RQuickAccessToolBar.this.drop_bounds.contains(event.x, event.y)) {
                            this.flag = false;
                        }

                        RMenu menu = RQuickAccessToolBar.this.getParent().getFolder().getrMenu();
                        if (this.flag && menu != null) {
                            menu.setLocation(RQuickAccessToolBar.this.barpart.toDisplay(event.x, event.y));
                            menu.setVisible(true);
                        }
                    }

                    this.flag = false;
                }

                if (event.button == 1) {
                    if (RQuickAccessToolBar.this.clicked != null) {
                        Rectangle bounds = RQuickAccessToolBar.this.clicked.getBounds();
                        if (bounds.contains(event.x, event.y)) {
                            RQuickAccessToolBar.this.clicked.setSelection(!RQuickAccessToolBar.this.clicked.getSelection());
                            RQuickAccessToolBar.this.clicked.onClick(event);
                        }

                        RQuickAccessToolBar.this.clicked = null;
                    }

                    if (RQuickAccessToolBar.this.drop_clicked) {
                        if (RQuickAccessToolBar.this.drop_bounds.contains(event.x, event.y)) {
                            RQuickAccessToolBar.this.barpart.redraw(RQuickAccessToolBar.this.drop_bounds.x, RQuickAccessToolBar.this.drop_bounds.y, RQuickAccessToolBar.this.drop_bounds.width, RQuickAccessToolBar.this.drop_bounds.height, false);
                            if (RQuickAccessToolBar.this.menu != null) {
                                RQuickAccessToolBar.this.menu.setLocation(RQuickAccessToolBar.this.barpart.toDisplay(RQuickAccessToolBar.this.drop_bounds.x, RQuickAccessToolBar.this.drop_bounds.y + 22));
                                RQuickAccessToolBar.this.menu.setVisible(true);
                            }
                        }

                        RQuickAccessToolBar.this.drop_clicked = false;
                    }

                }
            }
        });
        this.barpart.addMouseMoveListener(new MouseMoveListener() {
            public void mouseMove(MouseEvent event) {
                Iterator var3 = RQuickAccessToolBar.this.items.iterator();

                while(var3.hasNext()) {
                    RQuickAccessToolItem item = (RQuickAccessToolItem)var3.next();
                    if (item.getEnabled()) {
                        Rectangle bounds = item.getBounds();
                        if (bounds.contains(event.x, event.y)) {
                            if (item != RQuickAccessToolBar.this.hovered) {
                                if (RQuickAccessToolBar.this.hovered != null) {
                                    Rectangle origin = RQuickAccessToolBar.this.hovered.getBounds();
                                    RQuickAccessToolBar.this.barpart.redraw(origin.x, origin.y, origin.width, origin.height, false);
                                    RQuickAccessToolBar.this.hovered = null;
                                    if (RUtils.toolTip != null) {
                                        RUtils.toolTip.kill();
                                    }
                                }

                                RQuickAccessToolBar.this.barpart.redraw(bounds.x, bounds.y, bounds.width, bounds.height, false);
                                RQuickAccessToolBar.this.hovered = item;
                            }
                        } else if (item == RQuickAccessToolBar.this.hovered) {
                            RQuickAccessToolBar.this.barpart.redraw(bounds.x, bounds.y, bounds.width, bounds.height, false);
                            RQuickAccessToolBar.this.hovered = null;
                            if (RUtils.toolTip != null) {
                                RUtils.toolTip.kill();
                            }
                        }
                    }
                }

                if (RQuickAccessToolBar.this.drop_bounds.contains(event.x, event.y)) {
                    if (!RQuickAccessToolBar.this.drop_hovered) {
                        RQuickAccessToolBar.this.barpart.redraw(RQuickAccessToolBar.this.drop_bounds.x, RQuickAccessToolBar.this.drop_bounds.y, RQuickAccessToolBar.this.drop_bounds.width, RQuickAccessToolBar.this.drop_bounds.height, false);
                        RQuickAccessToolBar.this.drop_hovered = true;
                    }
                } else if (RQuickAccessToolBar.this.drop_hovered) {
                    RQuickAccessToolBar.this.barpart.redraw(RQuickAccessToolBar.this.drop_bounds.x, RQuickAccessToolBar.this.drop_bounds.y, RQuickAccessToolBar.this.drop_bounds.width, RQuickAccessToolBar.this.drop_bounds.height, false);
                    RQuickAccessToolBar.this.drop_hovered = false;
                    if (RUtils.toolTip != null) {
                        RUtils.toolTip.kill();
                    }
                }

            }
        });
        this.barpart.addMouseTrackListener(new MouseTrackAdapter() {
            public void mouseExit(MouseEvent event) {
                if (RUtils.toolTip != null) {
                    RUtils.toolTip.kill();
                }

                if (RQuickAccessToolBar.this.hovered != null) {
                    Rectangle bounds = RQuickAccessToolBar.this.hovered.getBounds();
                    RQuickAccessToolBar.this.barpart.redraw(bounds.x, bounds.y, bounds.width, bounds.height, false);
                    RQuickAccessToolBar.this.hovered = null;
                }

                if (RQuickAccessToolBar.this.drop_hovered) {
                    RQuickAccessToolBar.this.barpart.redraw(RQuickAccessToolBar.this.drop_bounds.x, RQuickAccessToolBar.this.drop_bounds.y, RQuickAccessToolBar.this.drop_bounds.width, RQuickAccessToolBar.this.drop_bounds.height, false);
                    RQuickAccessToolBar.this.drop_hovered = false;
                }

            }

            public void mouseHover(MouseEvent event) {
                Iterator var3 = RQuickAccessToolBar.this.items.iterator();

                while(var3.hasNext()) {
                    RQuickAccessToolItem item = (RQuickAccessToolItem)var3.next();
                    Rectangle bounds = item.getBounds();
                    if (bounds.contains(event.x, event.y) && item.getToolTip() != null) {
                        item.getToolTip().show(RQuickAccessToolBar.this.barpart.toDisplay(bounds.x, 30));
                    }
                }

                if (RQuickAccessToolBar.this.drop_bounds.contains(event.x, event.y) && RQuickAccessToolBar.this.toolTip != null) {
                    RQuickAccessToolBar.this.toolTip.show(RQuickAccessToolBar.this.barpart.toDisplay(RQuickAccessToolBar.this.drop_bounds.x, 30));
                }

            }
        });
        parent.getShell().addControlListener(new ControlAdapter() {
            public void controlMoved(ControlEvent event) {
                if (RQuickAccessToolBar.this.above && RQuickAccessToolBar.this.visible) {
                    Point point = RQuickAccessToolBar.this.getParent().getShell().getLocation();
                    int y = Math.max(point.y, -3);
                    int height = RQuickAccessToolBar.this.getParent().getShell().toDisplay(0, 0).y - y;
                    int offset = (int)Math.ceil((double)height / 2.0D) - 10;
                    RQuickAccessToolBar.this.bar.setLocation(point.x + 28, y + offset);
                    RQuickAccessToolBar.this.top.setLocation(point.x + 28, y + offset);
                }
            }
        });
    }

    public RWindow getParent() {
        return this.parent;
    }

    public boolean getAbove() {
        return this.above;
    }

    public void setAbove(boolean above) {
        this.above = above;
        if (this.visible) {
            this.setVisible(true);
        }

    }

    public List<RQuickAccessToolItem> getItems() {
        return this.items;
    }

    public void addItem(RQuickAccessToolItem item) {
        this.items.add(item);
        item.setParent(this);
        if (this.above) {
            this.bar.redraw();
        } else {
            this.barpart.redraw();
        }

    }

    public void removeItem(RQuickAccessToolItem item) {
        this.items.remove(item);
        item = null;
        if (this.above) {
            this.bar.redraw();
        } else {
            this.barpart.redraw();
        }

    }

    public boolean getVisible() {
        return this.visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
        if (visible && this.above) {
            Point point = this.getParent().getShell().getLocation();
            int y = Math.max(point.y, -3);
            int height = this.getParent().getShell().toDisplay(0, 0).y - y;
            int offset = (int)Math.ceil((double)height / 2.0D) - 10;
            this.bar.setLocation(point.x + 28, y + offset);
            this.top.setLocation(point.x + 28, y + offset);
        }

        this.bar.setVisible(visible && this.above);
        this.top.setVisible(visible && this.above);
        if (this.bar.getVisible()) {
            this.bar.redraw();
        } else {
            this.parent.getShell().setText(this.parent.getShell().getText());
        }

        GridData data = (GridData)this.barpart.getLayoutData();
        data.heightHint = visible && !this.above ? 25 : 0;
        if (visible) {
            this.barpart.redraw();
        }

        this.parent.getShell().layout();
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

    protected RQuickAccessToolItem getHovered() {
        return this.hovered;
    }

    protected void setHovered(RQuickAccessToolItem hovered) {
        this.hovered = hovered;
    }

    protected RQuickAccessToolItem getClicked() {
        return this.clicked;
    }

    protected void setClicked(RQuickAccessToolItem clicked) {
        this.clicked = clicked;
    }

    protected RQuickAccessToolItem getPopuped() {
        return this.popuped;
    }

    protected void setPopuped(RQuickAccessToolItem popuped) {
        this.popuped = popuped;
    }

    protected int getTopWidth() {
        return this.top.getSize().x;
    }

    private void drawLine(GC gc, Region region, int x1, int y1, int x2, int y2) {
        gc.drawLine(x1, y1, x2, y2);
        if (x1 == x2) {
            region.add(x1, y1, 1, y2 - y1 + 1);
        } else {
            region.add(x1, y1, x2 - x1 + 1, 1);
        }

    }
}

