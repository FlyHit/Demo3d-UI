package ribbon;

import org.eclipse.swt.graphics.Device;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

public class RWindow {
    private Shell parent;
    private Shell shell;
    private RTabFolder folder;
    private Composite contents;
    private RQuickAccessToolBar bar;
    private Composite barpart;

    public RWindow(Shell parent) {
        this.parent = parent;
        this.shell = new Shell(parent, 1264) {
            protected void checkSubclass() {
            }

            public void open() {
                if (RWindow.this.bar != null && RWindow.this.bar.getVisible()) {
                    RWindow.this.bar.setVisible(true);
                } else {
                    ((GridData) RWindow.this.barpart.getLayoutData()).heightHint = 0;
                }

                super.layout(false, true);
                super.open();
            }

            public String getText() {
                return super.getText().trim();
            }

            public void setText(String text) {
                if (RWindow.this.bar != null && RWindow.this.bar.getAbove()) {
                    GC gc = new GC(RWindow.this.shell);
                    FontData data = Display.getCurrent().getSystemFont().getFontData()[0];
                    data.setStyle(1);
                    gc.setFont(new Font((Device)null, data));

                    String blank;
                    for(blank = " "; gc.stringExtent(blank).x < RWindow.this.bar.getTopWidth() + 2; blank = blank + " ") {
                    }

                    text = blank + text;
                    gc.dispose();
                }

                super.setText(text);
            }
        };
        GridLayout layout = new GridLayout();
        layout.marginWidth = 0;
        layout.marginHeight = 0;
        layout.verticalSpacing = 0;
        this.shell.setLayout(layout);
        this.folder = new RTabFolder(this.shell);
        this.barpart = new Composite(this.shell, 0);
        this.contents = new Composite(this.shell, 0);
        this.folder.getControl().setLayoutData(new GridData(768));
        this.barpart.setLayoutData(new GridData(768));
        this.contents.setLayoutData(new GridData(1808));
    }

    public Shell getParent() {
        return this.parent;
    }

    public Shell getShell() {
        return this.shell;
    }

    public RTabFolder getFolder() {
        return this.folder;
    }

    public Composite getContents() {
        return this.contents;
    }

    public RQuickAccessToolBar getQuickAccessToolBar() {
        return this.bar;
    }

    public void open() {
        this.configureShell(this.shell);
        this.createFolder(this.folder);
        this.createContents(this.contents);
        this.shell.open();
        Display display = this.shell.getDisplay();

        while(!this.shell.isDisposed()) {
            if (!display.readAndDispatch()) {
                display.sleep();
            }
        }

    }

    public void close() {
        this.shell.dispose();
    }

    public void configureShell(Shell shell) {
    }

    public void createFolder(RTabFolder folder) {
    }

    public void createContents(Composite contents) {
    }
}

