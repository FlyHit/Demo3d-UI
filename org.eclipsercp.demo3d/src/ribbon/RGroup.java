package ribbon;

import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public final class RGroup extends Composite {
    private RTabItem parent;
    private String text;
    private RGroupButton button;
    private List<RWidget> widgets;
    private RWidget hovered;
    private RWidget clicked;
    private RWidget popuped;

    public RGroup(RTabItem parent, String text) {
        super(parent.getControl(), 536870912);
        this.parent = parent;
        this.text = text;
        this.widgets = new ArrayList();
        this.forceFocus();
        parent.addGroup(this);
        this.addPaintListener(new PaintListener() {
            public void paintControl(PaintEvent event) {
                Rectangle bounds = RGroup.this.getBounds();
                int x = bounds.x;
                int y = bounds.y;
                int width = bounds.width;
                int height = bounds.height;
                GC gc = event.gc;
                gc.setFont(RUtils.initFont);
                gc.setForeground(RUtils.TAB_CLICKED);
                gc.setBackground(RUtils.BACK_COLOR);
                gc.fillGradientRectangle(x, y, width, 40, true);
                gc.fillRectangle(x, y + 40, width, 51);
                gc.setBackground(RUtils.LINE_COLOR);
                gc.fillRectangle(x, y + 91, width, 1);
                gc.setForeground(RUtils.LINE_COLOR);
                gc.drawLine(x + width - 2, y + 7, x + width - 2, y + height - 8);
                gc.setForeground(RUtils.TAB_CLICKED);
                gc.drawLine(x + width - 3, y + 7, x + width - 3, y + height - 8);
                gc.drawLine(x + width - 1, y + 7, x + width - 1, y + height - 8);
                if (RGroup.this.getText() != null) {
                    Point point = gc.stringExtent(RGroup.this.getText());
                    x = (RGroup.this.button == null ? width : width - 14) / 2 - point.x / 2 - 1;
                    y = height - 28 + point.y / 2;
                    gc.setForeground(RUtils.FONT_GROUP);
                    gc.drawString(RGroup.this.getText(), x, y, true);
                }

                if (RGroup.this.button != null) {
                    RGroup.this.button.setBounds(new Rectangle(bounds.x + width - 18, bounds.y + height - 19, 15, 14));
                    RGroup.this.button.drawWidget(gc);
                }

                Iterator var9 = RGroup.this.widgets.iterator();

                while(var9.hasNext()) {
                    RWidget widget = (RWidget)var9.next();
                    if (widget.getVisible()) {
                        widget.drawWidget(gc);
                    }
                }

            }
        });
        this.addMouseListener(new MouseAdapter() {
            public void mouseDown(MouseEvent event) {
                if (RUtils.toolTip != null) {
                    RUtils.toolTip.kill();
                }

                RWidget widget;
                Iterator var3;
                Rectangle bounds;
                RMerger part;
                List list;
                RWidget obj;
                Iterator var8;
                if (event.button == 3) {
                    var3 = RGroup.this.widgets.iterator();

                    label145:
                    while(true) {
                        while(true) {
                            do {
                                do {
                                    if (!var3.hasNext()) {
                                        break label145;
                                    }

                                    widget = (RWidget)var3.next();
                                } while(!widget.getVisible());
                            } while(!widget.getEnabled());

                            bounds = widget.getBounds();
                            if (widget instanceof RButton) {
                                if (bounds.contains(event.x, event.y)) {
                                    RGroup.this.popuped = widget;
                                }
                            } else if (widget instanceof RMerger) {
                                part = (RMerger)widget;
                                list = part.getWidgets();
                                var8 = list.iterator();

                                while(var8.hasNext()) {
                                    obj = (RWidget)var8.next();
                                    if (obj.getVisible() && obj.getEnabled() && !(obj instanceof RSeparator) && !(obj instanceof RBlank) && !(obj instanceof RMerger)) {
                                        bounds = obj.getBounds();
                                        if (bounds.contains(event.x, event.y)) {
                                            RGroup.this.popuped = obj;
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                if (event.button == 1) {
                    RGroup.this.forceFocus();
                    if (RGroup.this.button != null) {
                        Rectangle boundsx = RGroup.this.button.getBounds();
                        if (boundsx.contains(event.x, event.y)) {
                            RGroup.this.button.setClicked(true);
                            RGroup.this.redraw(boundsx.x, boundsx.y, boundsx.width, boundsx.height, false);
                        }
                    }

                    var3 = RGroup.this.widgets.iterator();

                    while(true) {
                        while(true) {
                            do {
                                do {
                                    if (!var3.hasNext()) {
                                        return;
                                    }

                                    widget = (RWidget)var3.next();
                                } while(!widget.getVisible());
                            } while(!widget.getEnabled());

                            bounds = widget.getBounds();
                            if (widget instanceof RButton) {
                                if (bounds.contains(event.x, event.y)) {
                                    RButton button = (RButton)widget;
                                    RGroup.this.clicked = button;
                                    if (button.isSplit()) {
                                        button.checkClicked(event.x, event.y);
                                    }

                                    RGroup.this.redraw(bounds.x, bounds.y, bounds.width, bounds.height, false);
                                }
                            } else if (widget instanceof RMerger) {
                                part = (RMerger)widget;
                                list = part.getWidgets();
                                var8 = list.iterator();

                                while(var8.hasNext()) {
                                    obj = (RWidget)var8.next();
                                    if (obj.getVisible() && obj.getEnabled()) {
                                        bounds = obj.getBounds();
                                        if (obj instanceof RButton) {
                                            if (bounds.contains(event.x, event.y)) {
                                                RButton buttonx = (RButton)obj;
                                                RGroup.this.clicked = buttonx;
                                                if (buttonx.isSplit()) {
                                                    buttonx.checkClicked(event.x, event.y);
                                                }

                                                RGroup.this.redraw(bounds.x, bounds.y, bounds.width, bounds.height, false);
                                            }
                                        } else if (obj instanceof RCheckBox) {
                                            if (bounds.contains(event.x, event.y)) {
                                                RGroup.this.clicked = obj;
                                                RGroup.this.redraw(bounds.x, bounds.y, 13, bounds.height, false);
                                            }
                                        } else if (obj instanceof RRadioBox) {
                                            if (bounds.contains(event.x, event.y)) {
                                                RGroup.this.clicked = obj;
                                                RGroup.this.redraw(bounds.x, bounds.y, 12, bounds.height, false);
                                            }
                                        } else if (obj instanceof RCombo) {
                                            if (bounds.contains(event.x, event.y)) {
                                                RCombo combo = (RCombo)obj;
                                                RGroup.this.clicked = combo;
                                                RGroup.this.redraw(bounds.x + bounds.width - 13, bounds.y, 13, bounds.height, false);
                                                combo.showDropDown();
                                            }
                                        } else if (obj instanceof RSpinner) {
                                            if ((new Rectangle(bounds.x + bounds.width - 13, bounds.y, 13, bounds.height)).contains(event.x, event.y)) {
                                                RSpinner spinner = (RSpinner)obj;
                                                RGroup.this.clicked = spinner;
                                                spinner.checkClicked(event.x, event.y);
                                                spinner.autoIncrease();
                                                RGroup.this.redraw(bounds.x + bounds.width - 13, bounds.y, 13, bounds.height, false);
                                            }
                                        } else if (obj instanceof RLink) {
                                            RLink link = (RLink)obj;
                                            if (link.getArea().contains(event.x, event.y)) {
                                                RGroup.this.clicked = link;
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

            public void mouseUp(MouseEvent event) {
                if (event.button == 3) {
                    if (RGroup.this.popuped == null || RGroup.this.popuped.getMenu() == null) {
                        return;
                    }

                    if (RGroup.this.popuped.getBounds().contains(event.x, event.y)) {
                        RGroup.this.popuped.getMenu().setLocation(RGroup.this.toDisplay(event.x, event.y));
                        RGroup.this.popuped.getMenu().setVisible(true);
                    }

                    RGroup.this.popuped = null;
                }

                if (event.button == 1) {
                    Rectangle bounds;
                    if (RGroup.this.button != null && RGroup.this.button.getClicked()) {
                        bounds = RGroup.this.button.getBounds();
                        if (bounds.contains(event.x, event.y)) {
                            RGroup.this.redraw(bounds.x, bounds.y, bounds.width, bounds.height, false);
                            RGroup.this.button.onClick(event);
                        }

                        RGroup.this.button.setClicked(false);
                    }

                    if (RGroup.this.clicked != null && !(RGroup.this.clicked instanceof RCombo)) {
                        bounds = RGroup.this.clicked.getBounds();
                        if (RGroup.this.clicked instanceof RButton) {
                            if (bounds.contains(event.x, event.y)) {
                                RButton button = (RButton) RGroup.this.clicked;
                                button.setSelection(!button.getSelection());
                                button.onClick(event);
                            }
                        } else if (RGroup.this.clicked instanceof RCheckBox) {
                            if (bounds.contains(event.x, event.y)) {
                                RCheckBox check = (RCheckBox) RGroup.this.clicked;
                                check.setSelection(!check.getSelection());
                                check.onClick(event);
                            }
                        } else if (RGroup.this.clicked instanceof RRadioBox) {
                            if (bounds.contains(event.x, event.y)) {
                                RRadioBox radio = (RRadioBox) RGroup.this.clicked;
                                radio.setSelection(true);
                                radio.onClick(event);
                            }
                        } else if (RGroup.this.clicked instanceof RSpinner) {
                            if (bounds.contains(event.x, event.y)) {
                                RGroup.this.redraw(bounds.x + bounds.width - 13, bounds.y, 13, bounds.height, false);
                                RSpinner spinner = (RSpinner) RGroup.this.clicked;
                                spinner.onClick(event);
                            }
                        } else if (RGroup.this.clicked instanceof RLink) {
                            RLink link = (RLink) RGroup.this.clicked;
                            if (link.getArea().contains(event.x, event.y)) {
                                link.onClick(event);
                            }
                        }

                        RGroup.this.clicked = null;
                    }
                }
            }
        });
        this.addMouseMoveListener(new MouseMoveListener() {
            public void mouseMove(MouseEvent event) {
                if (RGroup.this.button != null) {
                    Rectangle bounds = RGroup.this.button.getBounds();
                    if (bounds.contains(event.x, event.y)) {
                        if (!RGroup.this.button.getHovered()) {
                            RGroup.this.redraw(bounds.x, bounds.y, bounds.width, bounds.height, false);
                            RGroup.this.button.setHovered(true);
                        }
                    } else if (RGroup.this.button.getHovered()) {
                        RGroup.this.redraw(bounds.x, bounds.y, bounds.width, bounds.height, false);
                        RGroup.this.button.setHovered(false);
                        if (RUtils.toolTip != null) {
                            RUtils.toolTip.kill();
                        }
                    }
                }

                Iterator var3 = RGroup.this.widgets.iterator();

                while(true) {
                    label186:
                    while(true) {
                        RWidget widget;
                        do {
                            do {
                                if (!var3.hasNext()) {
                                    return;
                                }

                                widget = (RWidget)var3.next();
                            } while(!widget.getVisible());
                        } while(!widget.getEnabled());

                        Rectangle boundsx = widget.getBounds();
                        if (widget instanceof RButton) {
                            RButton button = (RButton)widget;
                            if (boundsx.contains(event.x, event.y)) {
                                if (button != RGroup.this.hovered) {
                                    if (RGroup.this.hovered != null) {
                                        Rectangle origin = RGroup.this.hovered.getBounds();
                                        RGroup.this.redraw(origin.x, origin.y, origin.width, origin.height, false);
                                        RGroup.this.hovered = null;
                                        if (RUtils.toolTip != null) {
                                            RUtils.toolTip.kill();
                                        }
                                    }

                                    if (button.isSplit()) {
                                        button.checkHovered(event.x, event.y);
                                    }

                                    RGroup.this.redraw(boundsx.x, boundsx.y, boundsx.width, boundsx.height, false);
                                    RGroup.this.hovered = button;
                                } else if (button.isSplit() && button.checkHovered(event.x, event.y)) {
                                    RGroup.this.redraw(boundsx.x, boundsx.y, boundsx.width, boundsx.height, false);
                                }
                            } else if (button == RGroup.this.hovered) {
                                RGroup.this.redraw(boundsx.x, boundsx.y, boundsx.width, boundsx.height, false);
                                RGroup.this.hovered = null;
                                if (RUtils.toolTip != null) {
                                    RUtils.toolTip.kill();
                                }
                            }
                        } else if (widget instanceof RMerger) {
                            RMerger part = (RMerger)widget;
                            List<RWidget> list = part.getWidgets();
                            Iterator var8 = list.iterator();

                            while(true) {
                                while(true) {
                                    RWidget obj;
                                    do {
                                        do {
                                            if (!var8.hasNext()) {
                                                continue label186;
                                            }

                                            obj = (RWidget)var8.next();
                                        } while(!obj.getVisible());
                                    } while(!obj.getEnabled());

                                    boundsx = obj.getBounds();
                                    Rectangle originxx;
                                    if (obj instanceof RButton) {
                                        RButton buttonx = (RButton)obj;
                                        if (boundsx.contains(event.x, event.y)) {
                                            if (buttonx != RGroup.this.hovered) {
                                                if (RGroup.this.hovered != null) {
                                                    originxx = RGroup.this.hovered.getBounds();
                                                    RGroup.this.redraw(originxx.x, originxx.y, originxx.width, originxx.height, false);
                                                    RGroup.this.hovered = null;
                                                    if (RUtils.toolTip != null) {
                                                        RUtils.toolTip.kill();
                                                    }
                                                }

                                                if (buttonx.isSplit()) {
                                                    buttonx.checkHovered(event.x, event.y);
                                                }

                                                RGroup.this.redraw(boundsx.x, boundsx.y, boundsx.width, boundsx.height, false);
                                                RGroup.this.hovered = buttonx;
                                            } else if (buttonx.isSplit() && buttonx.checkHovered(event.x, event.y)) {
                                                RGroup.this.redraw(boundsx.x, boundsx.y, boundsx.width, boundsx.height, false);
                                            }
                                        } else if (buttonx == RGroup.this.hovered) {
                                            RGroup.this.redraw(boundsx.x, boundsx.y, boundsx.width, boundsx.height, false);
                                            RGroup.this.hovered = null;
                                            if (RUtils.toolTip != null) {
                                                RUtils.toolTip.kill();
                                            }
                                        }
                                    } else {
                                        Rectangle originx;
                                        if (!(obj instanceof RCheckBox) && !(obj instanceof RRadioBox) && !(obj instanceof RCombo)) {
                                            if (obj instanceof RSpinner) {
                                                RSpinner spinner = (RSpinner)obj;
                                                if (boundsx.contains(event.x, event.y)) {
                                                    if (spinner != RGroup.this.hovered) {
                                                        if (RGroup.this.hovered != null) {
                                                            originxx = RGroup.this.hovered.getBounds();
                                                            RGroup.this.redraw(originxx.x, originxx.y, originxx.width, originxx.height, false);
                                                            RGroup.this.hovered = null;
                                                            if (RUtils.toolTip != null) {
                                                                RUtils.toolTip.kill();
                                                            }
                                                        }

                                                        spinner.checkHovered(event.x, event.y);
                                                        RGroup.this.redraw(boundsx.x, boundsx.y, boundsx.width, boundsx.height, false);
                                                        RGroup.this.hovered = spinner;
                                                    } else if (spinner.checkHovered(event.x, event.y)) {
                                                        RGroup.this.redraw(boundsx.x, boundsx.y, boundsx.width, boundsx.height, false);
                                                    }
                                                } else if (spinner == RGroup.this.hovered) {
                                                    RGroup.this.redraw(boundsx.x, boundsx.y, boundsx.width, boundsx.height, false);
                                                    RGroup.this.hovered = null;
                                                    if (RUtils.toolTip != null) {
                                                        RUtils.toolTip.kill();
                                                    }
                                                }
                                            } else if (obj instanceof RLabel || obj instanceof RLink) {
                                                if (boundsx.contains(event.x, event.y)) {
                                                    if (obj != RGroup.this.hovered) {
                                                        if (RGroup.this.hovered != null) {
                                                            originx = RGroup.this.hovered.getBounds();
                                                            RGroup.this.redraw(originx.x, originx.y, originx.width, originx.height, false);
                                                            RGroup.this.hovered = null;
                                                            if (RUtils.toolTip != null) {
                                                                RUtils.toolTip.kill();
                                                            }
                                                        }

                                                        RGroup.this.hovered = obj;
                                                    }
                                                } else if (obj == RGroup.this.hovered) {
                                                    RGroup.this.hovered = null;
                                                    if (RUtils.toolTip != null) {
                                                        RUtils.toolTip.kill();
                                                    }
                                                }

                                                if (obj instanceof RLink) {
                                                    RLink link = (RLink)obj;
                                                    if (link.getArea().contains(event.x, event.y)) {
                                                        RGroup.this.setCursor(Display.getCurrent().getSystemCursor(21));
                                                    } else {
                                                        RGroup.this.setCursor(Display.getCurrent().getSystemCursor(0));
                                                    }
                                                }
                                            }
                                        } else if (boundsx.contains(event.x, event.y)) {
                                            if (obj != RGroup.this.hovered) {
                                                if (RGroup.this.hovered != null) {
                                                    originx = RGroup.this.hovered.getBounds();
                                                    RGroup.this.redraw(originx.x, originx.y, originx.width, originx.height, false);
                                                    RGroup.this.hovered = null;
                                                    if (RUtils.toolTip != null) {
                                                        RUtils.toolTip.kill();
                                                    }
                                                }

                                                RGroup.this.redraw(boundsx.x, boundsx.y, boundsx.width, boundsx.height, false);
                                                RGroup.this.hovered = obj;
                                            }
                                        } else if (obj == RGroup.this.hovered) {
                                            RGroup.this.redraw(boundsx.x, boundsx.y, boundsx.width, boundsx.height, false);
                                            RGroup.this.hovered = null;
                                            if (RUtils.toolTip != null) {
                                                RUtils.toolTip.kill();
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        });
        this.addMouseTrackListener(new MouseTrackAdapter() {
            public void mouseExit(MouseEvent event) {
                if (RUtils.toolTip != null) {
                    RUtils.toolTip.kill();
                }

                Rectangle bounds;
                if (RGroup.this.button != null && RGroup.this.button.getHovered()) {
                    bounds = RGroup.this.button.getBounds();
                    RGroup.this.redraw(bounds.x, bounds.y, bounds.width, bounds.height, false);
                    RGroup.this.button.setHovered(false);
                }

                if (RGroup.this.hovered != null) {
                    bounds = RGroup.this.hovered.getBounds();
                    RGroup.this.redraw(bounds.x, bounds.y, bounds.width, bounds.height, false);
                    RGroup.this.hovered = null;
                }

            }

            public void mouseHover(MouseEvent event) {
                if (RGroup.this.button != null) {
                    Rectangle bounds = RGroup.this.button.getBounds();
                    if (bounds.contains(event.x, event.y) && RGroup.this.button.getToolTip() != null) {
                        RGroup.this.button.getToolTip().show(RGroup.this.toDisplay(bounds.x, 97));
                    }
                }

                Iterator var3 = RGroup.this.widgets.iterator();

                while(true) {
                    while(true) {
                        RWidget widget;
                        do {
                            if (!var3.hasNext()) {
                                return;
                            }

                            widget = (RWidget)var3.next();
                        } while(!widget.getVisible());

                        Rectangle boundsx = widget.getBounds();
                        if (widget instanceof RButton) {
                            RButton button = (RButton)widget;
                            if (boundsx.contains(event.x, event.y) && button.getToolTip() != null) {
                                button.getToolTip().show(RGroup.this.toDisplay(boundsx.x, 97));
                            }
                        } else if (widget instanceof RMerger) {
                            RMerger part = (RMerger)widget;
                            List<RWidget> list = part.getWidgets();
                            Iterator var8 = list.iterator();

                            while(var8.hasNext()) {
                                RWidget obj = (RWidget)var8.next();
                                if (obj.getVisible() && !(obj instanceof RSeparator) && !(obj instanceof RBlank) && !(obj instanceof RMerger)) {
                                    boundsx = obj.getBounds();
                                    if (boundsx.contains(event.x, event.y) && obj.getToolTip() != null) {
                                        obj.getToolTip().show(RGroup.this.toDisplay(boundsx.x, 97));
                                    }
                                }
                            }
                        }
                    }
                }
            }
        });
    }

    public RTabItem getParentTab() {
        return this.parent;
    }

    public String getText() {
        return this.text;
    }

    public void setText(String text) {
        this.text = text;
        this.parent.getControl().layout(false);
    }

    public RGroupButton getGroupButton() {
        return this.button;
    }

    public List<RWidget> getWidgets() {
        return this.widgets;
    }

    public void setVisible(boolean visible) {
        super.setVisible(visible);
        this.parent.getControl().layout(false);
    }

    public Rectangle getBounds() {
        int width = 3;
        Iterator var3 = this.widgets.iterator();

        while(var3.hasNext()) {
            RWidget widget = (RWidget)var3.next();
            if (widget.getVisible()) {
                width += widget.getIndent() + widget.getBounds().width + 3;
            }
        }

        if (this.text != null) {
            GC gc = new GC(this);
            gc.setFont(RUtils.initFont);
            width = Math.max(width, gc.stringExtent(this.text).x + 6);
            gc.dispose();
        }

        return new Rectangle(0, 0, width + 5, 92);
    }

    public void dispose() {
        this.widgets.clear();
        super.dispose();
    }

    protected RWidget getHovered() {
        return this.hovered;
    }

    protected void setHovered(RWidget hovered) {
        this.hovered = hovered;
    }

    protected RWidget getClicked() {
        return this.clicked;
    }

    protected void setClicked(RWidget clicked) {
        this.clicked = clicked;
    }

    protected RWidget getPopuped() {
        return this.popuped;
    }

    protected void setPopuped(RWidget popuped) {
        this.popuped = popuped;
    }

    protected void setGroupButton(RGroupButton button) {
        this.button = button;
    }

    protected void addWidget(RWidget widget) {
        if (!this.widgets.contains(widget)) {
            this.widgets.add(widget);
            this.updateBounds();
        }
    }

    protected void updateBounds() {
        GC gc = new GC(this);
        gc.setFont(RUtils.initFont);
        int x = 4;
        Iterator var4 = this.widgets.iterator();

        while(true) {
            while(true) {
                RWidget widget;
                do {
                    if (!var4.hasNext()) {
                        gc.dispose();
                        return;
                    }

                    widget = (RWidget)var4.next();
                } while(!widget.getVisible());

                int indent = widget.getIndent();
                if (widget instanceof RSeparator) {
                    widget.setBounds(new Rectangle(x + indent, 7, 5, 58));
                    x += indent + 5 + 3;
                } else {
                    int row;
                    int count;
                    int width;
                    int limit;
                    if (widget instanceof RButton) {
                        RButton button = (RButton)widget;
                        int imageWidth = 8;
                        if (button.getImage() != null) {
                            imageWidth += button.getImage().getBounds().width;
                        }

                        row = !button.isArrow() ? 0 : 11;
                        count = 0;
                        if (button.getText() != null) {
                            String text = button.getText();
                            String[] strs = text.split("\n", 2);

                            for(limit = 0; limit < strs.length; ++limit) {
                                int lineWidth = gc.stringExtent(strs[limit].trim()).x;
                                if (limit == 1) {
                                    lineWidth += row;
                                }

                                count = Math.max(lineWidth, count);
                            }

                            count += 8;
                        }

                        width = Math.max(imageWidth, count);
                        button.setBounds(new Rectangle(x + indent, 5, width, 63));
                        x += indent + width + 3;
                    } else if (widget instanceof RMerger) {
                        Rectangle bounds = new Rectangle(x + indent, 4, 0, 65);
                        x += indent;
                        RMerger part = (RMerger)widget;
                        row = 0;
                        count = 0;
                        width = 0;
                        int max = 0;
                        limit = part.getLimit();
                        List<RWidget> list = part.getWidgets();

                        for(int i = 0; i < list.size(); ++i) {
                            RWidget obj = (RWidget)list.get(i);
                            if (obj.getVisible()) {
                                indent = obj.getIndent();
                                if (obj instanceof RSeparator) {
                                    obj.setBounds(new Rectangle(x + width + indent, 7 + 21 * (row % 3), 5, 16));
                                    width += indent + 5 + 3;
                                } else {
                                    Image image;
                                    String text;
                                    int imageWidth;
                                    int textWidth;
                                    int fullWidth;
                                    if (obj instanceof RButton) {
                                        RButton button = (RButton)obj;
                                        image = button.getImage();
                                        text = button.getText();
                                        imageWidth = image == null ? 0 : image.getBounds().width + 1;
                                        textWidth = text == null ? 0 : gc.stringExtent(text).x + 4;
                                        fullWidth = !button.isArrow() ? 0 : 11;
                                        fullWidth = imageWidth + textWidth + fullWidth + 4;
                                        button.setBounds(new Rectangle(x + width + indent, 5 + 21 * (row % 3), fullWidth, 21));
                                        width += indent + fullWidth + 3;
                                    } else {
                                        if (obj instanceof RCheckBox) {
                                            RCheckBox check = (RCheckBox)obj;
                                            text = check.getText();
                                            textWidth = text == null ? 0 : gc.stringExtent(text).x + 6;
                                            imageWidth = 13 + textWidth;
                                            check.setBounds(new Rectangle(x + width + indent, 5 + 21 * (row % 3), imageWidth, 21));
                                            width += indent + imageWidth + 3;
                                        } else if (obj instanceof RRadioBox) {
                                            RRadioBox radio = (RRadioBox)obj;
                                            text = radio.getText();
                                            textWidth = text == null ? 0 : gc.stringExtent(text).x + 6;
                                            imageWidth = 13 + textWidth;
                                            radio.setBounds(new Rectangle(x + width + indent, 5 + 21 * (row % 3), imageWidth, 21));
                                            width += indent + imageWidth + 3;
                                        } else if (obj instanceof RLabel) {
                                            RLabel label = (RLabel)obj;
                                            image = label.getImage();
                                            text = label.getText();
                                            imageWidth = image == null ? 0 : image.getBounds().width + 1;
                                            textWidth = text == null ? 0 : gc.stringExtent(text).x;
                                            fullWidth = imageWidth + textWidth;
                                            label.setBounds(new Rectangle(x + width + indent, 5 + 21 * (row % 3), fullWidth, 21));
                                            width += indent + fullWidth + 3;
                                        } else if (obj instanceof RText) {
                                            RText rText = (RText)obj;
                                            rText.setBounds(new Rectangle(x + width + indent, 6 + 21 * (row % 3),
                                                    rText.getWidth(), 20));
                                            width += indent + rText.getWidth() + 3;
                                        } else if (obj instanceof RCombo) {
                                            RCombo combo = (RCombo)obj;
                                            combo.setBounds(new Rectangle(x + width + indent, 6 + 21 * (row % 3), combo.getWidth(), 20));
                                            width += indent + combo.getWidth() + 3;
                                        } else if (obj instanceof RSpinner) {
                                            RSpinner spinner = (RSpinner)obj;
                                            spinner.setBounds(new Rectangle(x + width + indent, 6 + 21 * (row % 3), spinner.getWidth(), 20));
                                            width += indent + spinner.getWidth() + 3;
                                        } else if (obj instanceof RLink) {
                                            RLink link = (RLink)obj;
                                            link.setBounds(new Rectangle(x + width + indent, 5 + 21 * (row % 3), link.getWidth(), 21));
                                            width += indent + link.getWidth() + 3;
                                        }
                                    }
                                }

                                ++count;
                            }

                            if (i == list.size() - 1) {
                                max = Math.max(max, width);
                                x += max;
                                bounds.width += max;
                            } else if (count == limit) {
                                max = Math.max(max, width);
                                if ((row + 1) % 3 == 0) {
                                    x += max;
                                    bounds.width += max;
                                    max = 0;
                                }

                                ++row;
                                count = 0;
                                width = 0;
                            }
                        }

                        if (width > 0) {
                            bounds.width -= 3;
                        }

                        part.setBounds(bounds);
                    }
                }
            }
        }
    }
}
