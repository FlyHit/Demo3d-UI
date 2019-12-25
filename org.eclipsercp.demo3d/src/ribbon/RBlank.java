package ribbon;

import org.eclipse.swt.graphics.GC;

public final class RBlank extends RWidget {
    public RBlank(RMerger parent) {
        super(parent, 0);
        parent.addWidget(this);
    }

    protected void drawWidget(GC gc) {
    }
}
