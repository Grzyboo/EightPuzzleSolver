package Base;

import javax.swing.*;
import java.awt.*;

public class ImagePanel extends JPanel {

    private Image image;

    public ImagePanel(Image image) {
        this.image = image;
    }

    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if(image != null)
            g.drawImage(image, 0, 0, null);
    }

    public Dimension getPreferredSize() {
        if(image != null) {
            int w = image.getWidth(null);
            int h = image.getHeight(null);
            return new Dimension(w, h);
        }
        else
            return super.getPreferredSize();
    }
}
