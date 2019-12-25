package ribbon;

import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Text;

public final class RText extends RWidget {
    private Text control;
    private int width;

    public RText(RMerger parent, int style) {
        super(parent, style & 4194312);
        this.control = new Text(parent.getParent(), 4 | style & 4194312);
        this.control.setFont(RUtils.initFont);
        this.control.setBackground(Display.getCurrent().getSystemColor(25));
        this.control.addMouseListener(new MouseAdapter() {
            public void mouseDown(MouseEvent event) {
                if (event.button == 3) {
                    RText.this.getParent().setPopuped(RText.this);
                }

            }

            public void mouseUp(MouseEvent event) {
                if (event.button == 3) {
                    if (RText.this.control.getBounds().contains(Display.getCurrent().map(RText.this.control, RText.this.getParent(), event.x, event.y)) && RText.this.getParent().getPopuped() == RText.this && RText.this.menu != null) {
                        RText.this.menu.setLocation(RText.this.control.toDisplay(event.x, event.y));
                        RText.this.menu.setVisible(true);
                    }

                    RText.this.getParent().setPopuped((RWidget)null);
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
        this.control.addMouseTrackListener(new MouseTrackAdapter() {
            public void mouseHover(MouseEvent event) {
                if (RText.this.toolTip != null) {
                    RText.this.toolTip.show(RText.this.getParent().toDisplay(RText.this.bounds.x, 97));
                }

            }
        });
        this.control.addFocusListener(new FocusAdapter() {
            public void focusGained(FocusEvent event) {
                if ((RText.this.getStyle() & 8) == 0 && RText.this.control.getEditable()) {
                    Display.getCurrent().asyncExec(new Runnable() {
                        public void run() {
                            RText.this.control.selectAll();
                        }
                    });
                }
            }
        });
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
        super.enabled = enabled;
        this.control.setEnabled(enabled);
    }

    public void setVisible(boolean visible) {
        super.setVisible(visible);
        this.control.setVisible(visible);
    }

    public boolean getEditable() {
        return this.control.getEditable();
    }

    public void setEditable(boolean editable) {
        this.control.setEditable(editable);
    }

    public void append(String text) {
        this.control.append(text);
    }

    public void insert(String text) {
        this.control.insert(text);
    }

    public char getEchoChar() {
        return this.control.getEchoChar();
    }

    public void setEchoChar(char echo) {
        this.control.setEchoChar(echo);
    }

    public String getSelectionText() {
        return this.control.getSelectionText();
    }

    public void setSelection(int start, int end) {
        this.control.setSelection(start, end);
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
        this.control.setBounds(new Rectangle(bounds.x + 1, bounds.y + 1, bounds.width - 2, bounds.height - 2));
    }

    protected void drawWidget(GC gc) {
        gc.setForeground(RUtils.LINE_COLOR);
        gc.drawRectangle(this.bounds.x, this.bounds.y, this.bounds.width - 1, this.bounds.height - 1);
    }
}

