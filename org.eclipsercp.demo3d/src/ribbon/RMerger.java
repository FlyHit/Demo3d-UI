package ribbon;

import org.eclipse.swt.graphics.GC;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public final class RMerger extends RWidget {
    private int limit;
    private List<RWidget> widgets;

    public RMerger(RGroup parent, int limit) {
        super(parent, 0);
        this.limit = Math.abs(limit);
        this.widgets = new ArrayList();
        parent.addWidget(this);
    }

    public List<RWidget> getWidgets() {
        return this.widgets;
    }

    protected void addWidget(RWidget widget) {
        if (!this.widgets.contains(widget)) {
            this.widgets.add(widget);
            this.parent.updateBounds();
        }
    }

    protected int getLimit() {
        return this.limit;
    }

    protected void drawWidget(GC gc) {
        Iterator var3 = this.widgets.iterator();

        while(var3.hasNext()) {
            RWidget widget = (RWidget)var3.next();
            if (widget.getVisible()) {
                widget.drawWidget(gc);
            }
        }

    }
}

