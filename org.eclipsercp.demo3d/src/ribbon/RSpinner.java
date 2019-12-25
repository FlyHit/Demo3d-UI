package ribbon;

import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Text;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public final class RSpinner extends RWidget {
    private static final int TEXT_HOVER = 0;
    private static final int UP_HOVER = 1;
    private static final int DOWN_HOVER = -1;
    private Text control;
    private int selection;
    private int minimum;
    private int maximum;
    private int digits;
    private int increment;
    private int pageIncrement;
    private int status;
    private boolean upClicked;
    private int width;
    private List<SelectionListener> listeners;

    public RSpinner(RMerger parent, int style) {
        super(parent, style & 8);
        this.control = new Text(parent.getParent(), 4 | style & 8);
        this.control.setFont(RUtils.initFont);
        this.control.setBackground(Display.getCurrent().getSystemColor(25));
        this.control.addMouseListener(new MouseAdapter() {
            public void mouseDown(MouseEvent event) {
                if (event.button == 3) {
                    RSpinner.this.getParent().setPopuped(RSpinner.this);
                }

            }

            public void mouseUp(MouseEvent event) {
                if (event.button == 3) {
                    if (RSpinner.this.control.getBounds().contains(Display.getCurrent().map(RSpinner.this.control, RSpinner.this.getParent(), event.x, event.y)) && RSpinner.this.getParent().getPopuped() == RSpinner.this && RSpinner.this.menu != null) {
                        RSpinner.this.menu.setLocation(RSpinner.this.control.toDisplay(event.x, event.y));
                        RSpinner.this.menu.setVisible(true);
                    }

                    RSpinner.this.getParent().setPopuped((RWidget)null);
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
                RSpinner.this.status = 0;
                RSpinner.this.getParent().setHovered(RSpinner.this);
                RSpinner.this.getParent().redraw(RSpinner.this.bounds.x, RSpinner.this.bounds.y, RSpinner.this.bounds.width, RSpinner.this.bounds.height, false);
            }

            public void mouseExit(MouseEvent event) {
                RSpinner.this.getParent().setHovered((RWidget)null);
                RSpinner.this.getParent().redraw(RSpinner.this.bounds.x, RSpinner.this.bounds.y, RSpinner.this.bounds.width, RSpinner.this.bounds.height, false);
            }

            public void mouseHover(MouseEvent event) {
                if (RSpinner.this.toolTip != null) {
                    RSpinner.this.toolTip.show(RSpinner.this.getParent().toDisplay(RSpinner.this.bounds.x, 97));
                }

            }
        });
        this.control.addFocusListener(new FocusAdapter() {
            public void focusGained(FocusEvent event) {
                if ((RSpinner.this.getStyle() & 8) == 0) {
                    Display.getCurrent().asyncExec(new Runnable() {
                        public void run() {
                            RSpinner.this.control.selectAll();
                        }
                    });
                }
            }
        });
        this.control.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent event) {
                if (event.keyCode == 16777217) {
                    RSpinner.this.setSelection(RSpinner.this.selection + RSpinner.this.increment);
                } else if (event.keyCode == 16777218) {
                    RSpinner.this.setSelection(RSpinner.this.selection - RSpinner.this.increment);
                } else if (event.keyCode == 16777221) {
                    RSpinner.this.setSelection(RSpinner.this.selection + RSpinner.this.pageIncrement);
                } else if (event.keyCode == 16777222) {
                    RSpinner.this.setSelection(RSpinner.this.selection - RSpinner.this.pageIncrement);
                }

            }
        });
        this.control.addVerifyListener(new VerifyListener() {
            public void verifyText(VerifyEvent event) {
                String text = RSpinner.this.control.getText();
                String repl = text.substring(0, event.start) + event.text + text.substring(event.end);

                try {
                    BigDecimal num = (new BigDecimal(repl)).movePointRight(RSpinner.this.digits);
                    if (num.intValueExact() < RSpinner.this.minimum || num.intValueExact() > RSpinner.this.maximum) {
                        throw new Exception();
                    }

                    RSpinner.this.selection = num.intValueExact();
                } catch (Exception var5) {
                    event.doit = false;
                }

            }
        });
        this.control.setText("0");
        this.selection = 0;
        this.minimum = 0;
        this.maximum = 100;
        this.digits = 0;
        this.increment = 1;
        this.pageIncrement = 10;
        this.width = 80;
        this.listeners = new ArrayList();
        parent.addWidget(this);
    }

    public String getText() {
        return this.control.getText();
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

    public int getSelection() {
        return this.selection;
    }

    public void setSelection(int selection) {
        this.selection = selection;
        this.updateValue();
    }

    public int getMinimum() {
        return this.minimum;
    }

    public void setMinimum(int minimum) {
        this.minimum = minimum;
        this.updateValue();
    }

    public int getMaximum() {
        return this.maximum;
    }

    public void setMaximum(int maximum) {
        this.maximum = maximum;
        this.updateValue();
    }

    public int getDigits() {
        return this.digits;
    }

    public void setDigits(int digits) {
        this.digits = Math.abs(digits);
        this.updateValue();
    }

    public int getIncrement() {
        return this.increment;
    }

    public void setIncrement(int increment) {
        this.increment = Math.abs(increment);
    }

    public int getPageIncrement() {
        return this.pageIncrement;
    }

    public void setPageIncrement(int pageIncrement) {
        this.pageIncrement = Math.abs(pageIncrement);
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

    public void addSelectionListener(SelectionListener listener) {
        if (!this.listeners.contains(listener)) {
            this.listeners.add(listener);
        }
    }

    public void removeSelectionListener(SelectionListener listener) {
        this.listeners.remove(listener);
    }

    public void addModifyListener(ModifyListener listener) {
        this.control.addModifyListener(listener);
    }

    public void removeModifyListener(ModifyListener listener) {
        this.control.removeModifyListener(listener);
    }

    public void setValues(int selection, int minimum, int maximum, int digits, int increment, int pageIncrement) {
        this.selection = selection;
        this.minimum = minimum;
        this.maximum = maximum;
        this.digits = Math.abs(digits);
        this.increment = Math.abs(increment);
        this.pageIncrement = Math.abs(pageIncrement);
        this.updateValue();
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

    protected boolean checkHovered(int x, int y) {
        boolean changed = false;
        Rectangle up = new Rectangle(this.bounds.x + this.bounds.width - 13, this.bounds.y, 13, 11);
        Rectangle down = new Rectangle(this.bounds.x + this.bounds.width - 13, this.bounds.y + 10, 13, 11);
        if (up.contains(x, y)) {
            if (this.status != 1) {
                changed = true;
            }

            this.status = 1;
        } else if (down.contains(x, y)) {
            if (this.status != -1) {
                changed = true;
            }

            this.status = -1;
        } else {
            if (this.status != 0) {
                changed = true;
            }

            this.status = 0;
        }

        return changed;
    }

    protected void checkClicked(int x, int y) {
        Rectangle up = new Rectangle(this.bounds.x + this.bounds.width - 13, this.bounds.y, 13, 11);
        this.upClicked = up.contains(x, y);
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

    protected void autoIncrease() {
        (new Thread() {
            public void run() {
                int time = 200;

                while(true) {
                    while(RSpinner.this == RSpinner.this.parent.getClicked()) {
                        if (RSpinner.this != RSpinner.this.parent.getHovered()) {
                            time = 200;
                        } else if (RSpinner.this.status == 1 && !RSpinner.this.upClicked) {
                            time = 200;
                        } else if (RSpinner.this.status == -1 && RSpinner.this.upClicked) {
                            time = 200;
                        } else {
                            RSpinner.this.parent.getDisplay().asyncExec(new Runnable() {
                                public void run() {
                                    RSpinner var10000 = RSpinner.this;
                                    var10000.selection = var10000.selection + RSpinner.this.increment * RSpinner.this.status;
                                    RSpinner.this.updateValue();
                                }
                            });

                            try {
                                Thread.sleep((long)time);
                            } catch (InterruptedException var3) {
                                var3.printStackTrace();
                            }

                            if (time > 20) {
                                time -= 20;
                            }
                        }
                    }

                    return;
                }
            }
        }).start();
    }

    protected void setBounds(Rectangle bounds) {
        super.setBounds(bounds);
        this.control.setBounds(new Rectangle(bounds.x + 1, bounds.y + 1, bounds.width - 14, bounds.height - 2));
    }

    protected void drawWidget(GC gc) {
        boolean isHover = this == this.parent.getHovered();
        boolean isClick = this == this.parent.getClicked();
        gc.setForeground(RUtils.LINE_COLOR);
        gc.drawRectangle(this.bounds.x, this.bounds.y, this.bounds.width - 1, this.bounds.height - 1);
        gc.setBackground(this.control.getBackground());
        gc.fillRectangle(this.bounds.x + this.bounds.width - 13, this.bounds.y + 1, 12, this.bounds.height - 2);
        if (isHover) {
            gc.setBackground(RUtils.HOVER_COLOR);
            gc.fillRectangle(this.bounds.x + this.bounds.width - 12, this.bounds.y + 1, 12, this.bounds.height - 2);
            if (this.status == 1) {
                gc.setBackground(isClick && this.upClicked ? RUtils.CLICK_COLOR : RUtils.HOVER_DARK_LIGHT);
                gc.fillRectangle(this.bounds.x + this.bounds.width - 12, this.bounds.y + 1, 12, 10);
                gc.setForeground(isClick && this.upClicked ? RUtils.CLICK_BORDER : RUtils.HOVER_BORDER);
                gc.drawRectangle(this.bounds.x + this.bounds.width - 13, this.bounds.y, 12, 9);
                gc.setForeground(RUtils.HOVER_BORDER);
                gc.drawRectangle(this.bounds.x + this.bounds.width - 13, this.bounds.y + 10, 12, 9);
                gc.setForeground(RUtils.HOVER_INSIDE);
                gc.drawRectangle(this.bounds.x + this.bounds.width - 12, this.bounds.y + 11, 10, 7);
                if (!isClick || !this.upClicked) {
                    gc.drawRectangle(this.bounds.x + this.bounds.width - 12, this.bounds.y + 1, 10, 7);
                }
            } else if (this.status == -1) {
                gc.setBackground(isClick && !this.upClicked ? RUtils.CLICK_COLOR : RUtils.HOVER_DARK_LIGHT);
                gc.fillRectangle(this.bounds.x + this.bounds.width - 12, this.bounds.y + 11, 12, 10);
                gc.setForeground(RUtils.HOVER_BORDER);
                gc.drawRectangle(this.bounds.x + this.bounds.width - 13, this.bounds.y, 12, 9);
                gc.setForeground(isClick && !this.upClicked ? RUtils.CLICK_BORDER : RUtils.HOVER_BORDER);
                gc.drawRectangle(this.bounds.x + this.bounds.width - 13, this.bounds.y + 10, 12, 9);
                gc.setForeground(RUtils.HOVER_INSIDE);
                gc.drawRectangle(this.bounds.x + this.bounds.width - 12, this.bounds.y + 1, 10, 7);
                if (!isClick || this.upClicked) {
                    gc.drawRectangle(this.bounds.x + this.bounds.width - 12, this.bounds.y + 11, 10, 7);
                }
            } else {
                gc.setForeground(RUtils.HOVER_BORDER);
                gc.drawRectangle(this.bounds.x + this.bounds.width - 13, this.bounds.y, 12, 9);
                gc.drawRectangle(this.bounds.x + this.bounds.width - 13, this.bounds.y + 10, 12, 9);
                gc.setForeground(RUtils.HOVER_INSIDE);
                gc.drawRectangle(this.bounds.x + this.bounds.width - 12, this.bounds.y + 1, 10, 7);
                gc.drawRectangle(this.bounds.x + this.bounds.width - 12, this.bounds.y + 11, 10, 7);
            }
        }

        gc.setForeground(this.enabled ? RUtils.FONT_COLOR : RUtils.FONT_DISABLE);
        this.drawArrow(gc, this.bounds.x + this.bounds.width - 9, this.bounds.y + 6, -1);
        this.drawArrow(gc, this.bounds.x + this.bounds.width - 9, this.bounds.y + 13, 1);
    }

    private void drawArrow(GC gc, int x, int y, int z) {
        gc.drawLine(x, y, x + 4, y);
        gc.drawLine(x + 1, y + z, x + 3, y + z);
        gc.drawLine(x + 2, y + 2 * z, x + 2, y + 2 * z);
    }

    private void updateValue() {
        this.selection = Math.max(this.selection, this.minimum);
        this.selection = Math.min(this.selection, this.maximum);
        String text = (new BigDecimal(this.selection)).movePointLeft(this.digits).toString();
        if (!this.getText().equals(text)) {
            this.control.setText(text);
        }

    }
}

