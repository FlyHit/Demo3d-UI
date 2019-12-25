package ribbon;

import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

public final class RToolTip {
    private Shell shell;
    private String text;
    private String message;
    private Image image;
    private boolean isRecent;

    public RToolTip(String text, String message, Image image) {
        this.text = text;
        this.message = message;
        this.image = image;
    }

    public RToolTip(String text, String message) {
        this(text, message, (Image)null);
    }

    public RToolTip(String message) {
        this((String)null, message, (Image)null);
    }

    public String getText() {
        return this.text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getMessage() {
        return this.message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Image getImage() {
        return this.image;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    protected RToolTip(String message, boolean isRecent) {
        this(message);
        this.isRecent = isRecent;
    }

    protected void show(Point location) {
        if (RUtils.toolTip != this) {
            if (RUtils.toolTip != null) {
                RUtils.toolTip.kill();
            }

            RUtils.toolTip = this;
            if (this.text != null || this.message != null || this.image != null) {
                this.shell = new Shell(540684);
                GC gc = new GC(this.shell);
                gc.setFont(RUtils.initFont);
                int x = 0;
                int y = 0;
                int width = 0;
                int height = 0;
                Point point;
                if (this.text != null) {
                    gc.setFont(RUtils.blodFont);
                    point = gc.stringExtent(this.text);
                    y = point.y + 8;
                    width = point.x + 10;
                    gc.setFont(RUtils.initFont);
                }

                if (this.image != null) {
                    Rectangle bounds = this.image.getBounds();
                    x += bounds.width + 13;
                    height = Math.max(height, y + bounds.height + 16);
                }

                if (this.message != null) {
                    point = gc.textExtent(this.message);
                    x += point.x + 13;
                    height = Math.max(height, y + point.y + 16);
                }

                width = Math.max(width, x + 13);
                height = Math.max(height, y + 8);
                gc.dispose();
                this.shell.setSize(width, height);
                this.shell.addPaintListener(new PaintListener() {
                    public void paintControl(PaintEvent event) {
                        GC gc = event.gc;
                        gc.setFont(RUtils.initFont);
                        int x = 0;
                        int y = 0;
                        int width = RToolTip.this.shell.getSize().x;
                        int height = RToolTip.this.shell.getSize().y;
                        gc.setForeground(RUtils.TAB_CLICKED);
                        gc.setBackground(RUtils.BACK_COLOR);
                        gc.fillGradientRectangle(0, 0, width, height, true);
                        gc.setForeground(RUtils.TOOLTIP_02);
                        if (RToolTip.this.text != null) {
                            gc.setFont(RUtils.blodFont);
                            Point point = gc.stringExtent(RToolTip.this.text);
                            gc.drawString(RToolTip.this.text, 5, 8, true);
                            y += point.y + 8;
                            gc.setFont(RUtils.initFont);
                        }

                        if (RToolTip.this.image != null) {
                            Rectangle bounds = RToolTip.this.image.getBounds();
                            gc.drawImage(RToolTip.this.image, 13, y + 8);
                            x += bounds.width + 13;
                        }

                        if (RToolTip.this.message != null) {
                            gc.drawText(RToolTip.this.message, x + 13, y + 8, true);
                        }

                        gc.setForeground(RUtils.TOOLTIP_01);
                        gc.drawRectangle(0, 0, width - 1, height - 1);
                        gc.drawPoint(1, 1);
                        gc.drawPoint(width - 2, 1);
                        gc.drawPoint(width - 2, height - 2);
                        gc.drawPoint(1, height - 2);
                        Region region = new Region(Display.getCurrent());
                        region.add(0, 0, width, height);
                        region.subtract(0, 0, 1, 1);
                        region.subtract(width - 1, height - 1, 1, 1);
                        region.subtract(0, height - 1, 1, 1);
                        region.subtract(width - 1, 0, 1, 1);
                        RToolTip.this.shell.setRegion(region);
                    }
                });
                int limit = this.shell.getMonitor().getBounds().width;
                if (location.x + width + 4 > limit && !this.isRecent) {
                    this.shell.setLocation(location.x = limit - width - 4, location.y);
                } else {
                    this.shell.setLocation(location);
                }

                this.shell.setVisible(true);
            }
        }
    }

    protected void kill() {
        if (this.shell != null) {
            this.shell.dispose();
        }

        RUtils.toolTip = null;
    }
}
