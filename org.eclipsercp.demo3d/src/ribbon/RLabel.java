package ribbon;

import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;

public final class RLabel extends RWidget {
    private String text;
    private Image image;
    private Image disImage;

    public RLabel(RMerger parent) {
        super(parent, 0);
        parent.addWidget(this);
    }

    public String getText() {
        return this.text;
    }

    public void setText(String text) {
        this.text = text;
        this.parent.updateBounds();
        this.parent.redraw();
        this.parent.getParent().layout(false);
    }

    public Image getImage() {
        return this.image;
    }

    public void setImage(Image image) {
        this.image = this.reduceImage(image);
        this.parent.updateBounds();
        this.parent.redraw();
        this.parent.getParent().layout(false);
    }

    protected void drawWidget(GC gc) {
        int x;
        int y;
        if (this.image != null) {
            if (this.disImage == null) {
                this.disImage = new Image(Display.getCurrent(), this.image, 2);
            }

            x = this.bounds.x;
            y = this.bounds.y + 3;
            gc.drawImage(this.enabled ? this.image : this.disImage, x, y);
        }

        gc.setForeground(this.enabled ? RUtils.FONT_COLOR : RUtils.FONT_DISABLE);
        if (this.text != null) {
            x = this.bounds.x + (this.image == null ? 0 : this.image.getBounds().width + 1);
            y = this.bounds.y + 2;
            gc.drawString(this.text, x, y, true);
        }

    }

    private Image reduceImage(Image image) {
        if (image != null) {
            int width = image.getBounds().width;
            int height = image.getBounds().height;
            double scale = height > 16 ? 16.0D / (double)height : 1.0D;
            if (scale != 1.0D) {
                width = (int)((double)width * scale);
                height = (int)((double)height * scale);
                return new Image(Display.getCurrent(), image.getImageData().scaledTo(width, height));
            }
        }

        return image;
    }
}

