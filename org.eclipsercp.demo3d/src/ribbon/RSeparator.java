package ribbon;

import org.eclipse.swt.graphics.GC;

public final class RSeparator extends RWidget {
    public RSeparator(RGroup parent) {
        super(parent, 0);
        parent.addWidget(this);
    }

    public RSeparator(RMerger parent) {
        super(parent, 0);
        parent.addWidget(this);
    }

    protected void drawWidget(GC gc) {
        int x = this.bounds.x;
        int y = this.bounds.y;
        int height = this.bounds.height;
        gc.setForeground(RUtils.LINE_COLOR);
        gc.drawLine(x + 2, y, x + 2, y + height);
        gc.setForeground(RUtils.TAB_CLICKED);
        gc.drawLine(x + 1, y, x + 1, y + height);
        gc.drawLine(x + 3, y, x + 3, y + height);
    }
}

