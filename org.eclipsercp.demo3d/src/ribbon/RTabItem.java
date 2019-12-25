package ribbon;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Layout;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public final class RTabItem {
    private RTabFolder parent;
    private String text;
    private Rectangle bounds;
    private Composite control;
    private List<RGroup> groups;

    public RTabItem(RTabFolder parent, String text, int index) {
        this.parent = parent;
        this.text = text;
        this.groups = new ArrayList();
        this.parent.addNewTab(this, index);
        this.control = new Composite(parent.getControl(), SWT.NONE);
        this.control.setLayout(new ItemLayout());
        this.control.addPaintListener(event -> {
            Rectangle bounds = RTabItem.this.getParent().getControl().getClientArea();
            int x = bounds.x;
            int y = bounds.y;
            int width = bounds.width;
            GC gc = event.gc;
            gc.setForeground(RUtils.TAB_CLICKED);
            gc.setBackground(RUtils.BACK_COLOR);
            gc.fillGradientRectangle(x, y, width, 40, true);
            gc.fillRectangle(x, y + 40, width, 51);
            gc.setBackground(RUtils.LINE_COLOR);
            gc.fillRectangle(x, y + 91, width, 1);
        });
        this.control.addMouseListener(new MouseAdapter() {
            public void mouseDown(MouseEvent event) {
                if (event.button == 1) {
                    RTabItem.this.control.forceFocus();
                }
            }
        });
    }

    public RTabItem(RTabFolder parent, String text) {
        this(parent, text, parent.getItemCount());
    }

    public RTabFolder getParent() {
        return this.parent;
    }

    public String getText() {
        return this.text;
    }

    public void setText(String text) {
        this.text = text;
        this.parent.updateBounds();
        this.parent.getControl().redraw(0, 0, this.parent.getBounds().width, 27, false);
    }

    public List<RGroup> getGroups() {
        return this.groups;
    }

    public void dispose() {
        Iterator var2 = this.groups.iterator();

        while(var2.hasNext()) {
            RGroup group = (RGroup)var2.next();
            group.dispose();
        }

        this.groups.clear();
        this.control.dispose();
    }

    protected Composite getControl() {
        return this.control;
    }

    protected Rectangle getBounds() {
        return this.bounds;
    }

    protected void setBounds(Rectangle bounds) {
        this.bounds = bounds;
    }

    protected void addGroup(RGroup group) {
        if (!this.groups.contains(group)) {
            this.groups.add(group);
        }
    }

    private class ItemLayout extends Layout {
        private ItemLayout() {
        }

        protected Point computeSize(Composite composite, int hint, int hint2, boolean flushCache) {
            return new Point(RTabItem.this.parent.getControl().getClientArea().width, 92);
        }

        protected void layout(Composite composite, boolean flushCache) {
            int x = 1;
            Iterator var5 = RTabItem.this.groups.iterator();

            while(var5.hasNext()) {
                RGroup group = (RGroup)var5.next();
                if (group.getVisible()) {
                    Rectangle bounds = group.getBounds();
                    group.setBounds(x, 0, bounds.width, 92);
                    x += bounds.width;
                }
            }

        }
    }
}

