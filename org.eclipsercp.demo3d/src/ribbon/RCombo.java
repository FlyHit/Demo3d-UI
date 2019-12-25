package ribbon;

import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public final class RCombo extends RWidget {
    private Text control;
    private List<String> items;
    private int index;
    private int count;
    private int width;

    public RCombo(RMerger parent, int style) {
        super(parent, style & 12);
        this.control = new Text(parent.getParent(), 4 | style & 12);
        this.control.setFont(RUtils.initFont);
        this.control.setBackground(Display.getCurrent().getSystemColor(25));
        this.control.addMouseListener(new MouseAdapter() {
            public void mouseDown(MouseEvent event) {
                if (event.button == 3) {
                    RCombo.this.getParent().setPopuped(RCombo.this);
                }

            }

            public void mouseUp(MouseEvent event) {
                if (event.button == 3) {
                    if (RCombo.this.control.getBounds().contains(Display.getCurrent().map(RCombo.this.control, RCombo.this.getParent(), event.x, event.y)) && RCombo.this.getParent().getPopuped() == RCombo.this && RCombo.this.menu != null) {
                        RCombo.this.menu.setLocation(RCombo.this.control.toDisplay(event.x, event.y));
                        RCombo.this.menu.setVisible(true);
                    }

                    RCombo.this.getParent().setPopuped((RWidget)null);
                }

            }
        });
        this.control.addMenuDetectListener(new MenuDetectListener() {
            public void menuDetected(MenuDetectEvent event) {
                event.doit = false;
            }
        });
        this.control.addTraverseListener(new TraverseListener() {
            public void keyTraversed(TraverseEvent event) {
                event.doit = false;
            }
        });
        this.control.addMouseTrackListener(new MouseTrackListener() {
            public void mouseEnter(MouseEvent event) {
                RCombo.this.getParent().setHovered(RCombo.this);
                RCombo.this.getParent().redraw(RCombo.this.bounds.x, RCombo.this.bounds.y, RCombo.this.bounds.width, RCombo.this.bounds.height, false);
            }

            public void mouseExit(MouseEvent event) {
                RCombo.this.getParent().setHovered((RWidget)null);
                RCombo.this.getParent().redraw(RCombo.this.bounds.x, RCombo.this.bounds.y, RCombo.this.bounds.width, RCombo.this.bounds.height, false);
            }

            public void mouseHover(MouseEvent event) {
                if (RCombo.this.toolTip != null) {
                    RCombo.this.toolTip.show(RCombo.this.getParent().toDisplay(RCombo.this.bounds.x, 97));
                }

            }
        });
        this.control.addFocusListener(new FocusAdapter() {
            public void focusGained(FocusEvent event) {
                if ((RCombo.this.getStyle() & 8) != 0) {
                    RCombo.this.getParent().setClicked(RCombo.this);
                    RCombo.this.getParent().redraw(RCombo.this.bounds.x, RCombo.this.bounds.y, RCombo.this.bounds.width, RCombo.this.bounds.height, false);
                    RCombo.this.showDropDown();
                } else {
                    Display.getCurrent().asyncExec(new Runnable() {
                        public void run() {
                            RCombo.this.control.selectAll();
                        }
                    });
                }
            }
        });
        this.control.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent event) {
                Text var10000;
                List var10001;
                RCombo var10002;
                int var10004;
                if (event.keyCode == 16777217) {
                    if (RCombo.this.index > 0) {
                        var10000 = RCombo.this.control;
                        var10001 = RCombo.this.items;
                        var10002 = RCombo.this;
                        var10004 = var10002.index - 1;
                        var10002.index = var10004;
                        var10000.setText((String)var10001.get(var10004));
                    }
                } else if (event.keyCode == 16777218 && RCombo.this.index < RCombo.this.items.size() - 1) {
                    var10000 = RCombo.this.control;
                    var10001 = RCombo.this.items;
                    var10002 = RCombo.this;
                    var10004 = var10002.index + 1;
                    var10002.index = var10004;
                    var10000.setText((String)var10001.get(var10004));
                }

            }
        });
        this.items = new ArrayList();
        this.index = -1;
        this.count = 10;
        this.width = 80;
        parent.addWidget(this);
    }

    public String getText() {
        return this.control.getText();
    }

    public void setText(String text) {
        this.control.setText(text);
    }

    public int getTextLimit() {
        return this.control.getTextLimit();
    }

    public void setTextLimit(int limit) {
        this.control.setTextLimit(limit);
    }

    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        this.control.setEnabled(enabled);
    }

    public void setVisible(boolean visible) {
        super.setVisible(visible);
        this.control.setVisible(visible);
    }

    public void add(String item) {
        this.items.add(item);
    }

    public void add(String item, int index) {
        this.items.add(index, item);
        if (this.index >= index) {
            ++this.index;
        }

    }

    public String getItem(int index) {
        return (String)this.items.get(index);
    }

    public void setItem(String item, int index) {
        this.items.set(index, item);
        if (this.index == index) {
            this.index = -1;
        }

    }

    public int indexOf(String item) {
        return this.items.indexOf(item);
    }

    public int indexOf(String item, int start) {
        for(int i = start; i < this.items.size(); ++i) {
            if (((String)this.items.get(i)).equals(item)) {
                return this.items.indexOf(item);
            }
        }

        return -1;
    }

    public int getItemCount() {
        return this.items.size();
    }

    public List<String> getItems() {
        return this.items;
    }

    public void setItems(String... items) {
        this.items = new ArrayList(Arrays.asList(items));
        this.index = -1;
    }

    public void remove(int index) {
        this.items.remove(index);
    }

    public void remove(String item) {
        this.items.remove(item);
    }

    public void remove(int start, int end) {
        this.items.subList(start, end).clear();
    }

    public void removeAll() {
        this.items.clear();
    }

    public int getVisibleItemCount() {
        return this.count;
    }

    public void setVisibleItemCount(int count) {
        this.count = count;
    }

    public int getSelectionIndex() {
        return this.index;
    }

    public void setSelection(int index) {
        if (this.index != index) {
            this.control.setText((String)this.items.get(index));
            this.index = index;
        }
    }

    public Color getForeground() {
        return this.control.getForeground();
    }

    public void setForeground(Color color) {
        this.control.setForeground(color);
    }

    public Color getBackground() {
        return this.control.getBackground();
    }

    public void setBackground(Color color) {
        this.control.setBackground(color);
    }

    public void addFocusListener(FocusListener listener) {
        this.control.addFocusListener(listener);
    }

    public void removeFocusListener(FocusListener listener) {
        this.control.removeFocusListener(listener);
    }

    public void addVerifyListener(VerifyListener listener) {
        this.control.addVerifyListener(listener);
    }

    public void removeVerifyListener(VerifyListener listener) {
        this.control.removeVerifyListener(listener);
    }

    public void addModifyListener(ModifyListener listener) {
        this.control.addModifyListener(listener);
    }

    public void removeModifyListener(ModifyListener listener) {
        this.control.removeModifyListener(listener);
    }

    public int getWidth() {
        return this.width;
    }

    public void setWidth(int width) {
        this.width = width;
        this.parent.updateBounds();
        this.parent.redraw();
        this.parent.getParent().layout(false);
    }

    protected void setBounds(Rectangle bounds) {
        super.setBounds(bounds);
        this.control.setBounds(new Rectangle(bounds.x + 1, bounds.y + 1, bounds.width - 14, bounds.height - 2));
    }

    protected void showDropDown() {
        final Shell shell = new Shell(this.getParent().getShell(), 16396);
        shell.setBackground(RUtils.LINE_COLOR);
        Point point = this.control.toDisplay(0, 0);
        shell.setLocation(point.x - 1, point.y + 19);
        FillLayout layout = new FillLayout();
        layout.marginWidth = 1;
        layout.marginHeight = 1;
        shell.setLayout(layout);
        final org.eclipse.swt.widgets.List list = new org.eclipse.swt.widgets.List(shell, 512);
        GC gc = new GC(list);
        gc.setFont(RUtils.initFont);
        list.setFont(RUtils.initFont);
        int width = 0;

        String item;
        for(Iterator var9 = this.items.iterator(); var9.hasNext(); width = Math.max(gc.stringExtent(item).x, width)) {
            item = (String)var9.next();
            list.add(item);
        }

        gc.dispose();
        if (this.index == -1) {
            this.index = this.items.indexOf(this.control.getText());
        }

        list.setSelection(this.index);
        list.setForeground(this.control.getForeground());
        list.setBackground(this.control.getBackground());
        list.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent event) {
                RCombo.this.control.setText(list.getSelection()[0]);
                RCombo.this.index = list.getSelectionIndex();
            }
        });
        list.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent event) {
                if (event.keyCode == 13) {
                    shell.dispose();
                    RCombo.this.getParent().forceFocus();
                    RCombo.this.getParent().setClicked((RWidget)null);
                    RCombo.this.getParent().redraw(RCombo.this.bounds.x, RCombo.this.bounds.y, RCombo.this.bounds.width, RCombo.this.bounds.height, false);
                }

            }
        });
        list.addMouseListener(new MouseAdapter() {
            public void mouseDown(MouseEvent event) {
                if (event.button == 1) {
                    if (RCombo.this.index != list.getSelectionIndex()) {
                        RCombo.this.control.setText(list.getSelection()[0]);
                        RCombo.this.index = list.getSelectionIndex();
                    }

                    shell.dispose();
                    RCombo.this.getParent().forceFocus();
                    RCombo.this.getParent().setClicked((RWidget)null);
                    RCombo.this.getParent().redraw(RCombo.this.bounds.x, RCombo.this.bounds.y, RCombo.this.bounds.width, RCombo.this.bounds.height, false);
                }

            }
        });
        int height;
        if (this.items.size() > this.count) {
            width = Math.max(width + 6 + list.getVerticalBar().getSize().x, this.getWidth());
            height = list.getItemHeight() * this.count + 2;
        } else {
            width = Math.max(width + 6, this.getWidth());
            height = list.getItemHeight() * (this.items.size() == 0 ? 1 : this.items.size()) + 2;
        }

        shell.setSize(width, height);
        shell.addShellListener(new ShellAdapter() {
            public void shellDeactivated(ShellEvent event) {
                shell.dispose();
                RCombo.this.getParent().forceFocus();
                RCombo.this.getParent().setClicked((RWidget)null);
                RCombo.this.getParent().redraw(RCombo.this.bounds.x, RCombo.this.bounds.y, RCombo.this.bounds.width, RCombo.this.bounds.height, false);
            }
        });
        shell.open();
    }

    protected void drawWidget(GC gc) {
        boolean isHover = this == this.parent.getHovered();
        boolean isClick = this == this.parent.getClicked();
        gc.setForeground(RUtils.LINE_COLOR);
        gc.drawRectangle(this.bounds.x, this.bounds.y, this.bounds.width - 1, this.bounds.height - 1);
        gc.setBackground(this.control.getBackground());
        gc.fillRectangle(this.bounds.x + this.bounds.width - 13, this.bounds.y + 1, 12, this.bounds.height - 2);
        if (isHover || isClick) {
            gc.setForeground(isHover && !isClick ? RUtils.HOVER_HIGH_LIGHT : RUtils.CLICK_HIGH_LIGHT);
            gc.setBackground(isHover && !isClick ? RUtils.HOVER_COLOR : RUtils.CLICK_COLOR);
            gc.fillGradientRectangle(this.bounds.x + this.bounds.width - 13, this.bounds.y + 1, 12, 6, true);
            gc.setForeground(isHover && !isClick ? RUtils.HOVER_DARK_LIGHT : RUtils.CLICK_DARK_LIGHT);
            gc.fillGradientRectangle(this.bounds.x + this.bounds.width - 13, this.bounds.y + 7, 12, 12, true);
            gc.setForeground(isClick ? RUtils.CLICK_BORDER : RUtils.HOVER_BORDER);
            gc.drawRectangle(this.bounds.x + this.bounds.width - 13, this.bounds.y, 12, this.bounds.height - 1);
            if (isHover && !isClick) {
                gc.setForeground(RUtils.HOVER_INSIDE);
                gc.drawLine(this.bounds.x + this.bounds.width - 12, this.bounds.y + 2, this.bounds.x + this.bounds.width - 12, this.bounds.y + this.bounds.height - 3);
                gc.drawLine(this.bounds.x + this.bounds.width - 2, this.bounds.y + 2, this.bounds.x + this.width - 2, this.bounds.y + this.bounds.height - 3);
            }
        }

        gc.setForeground(this.enabled ? RUtils.FONT_COLOR : RUtils.FONT_DISABLE);
        this.drawArrow(gc, this.bounds.x + this.bounds.width - 9, this.bounds.y + 8);
    }

    private void drawArrow(GC gc, int x, int y) {
        gc.drawLine(x, y, x + 4, y);
        gc.drawLine(x + 1, y + 1, x + 3, y + 1);
        gc.drawLine(x + 2, y + 2, x + 2, y + 2);
    }
}

