package Base;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

public class ImagesAnimationPanel extends JPanel {
    private State state;
    private ImagesBank imagesBank;

    private int stateBoard[];

    public ImagesAnimationPanel() {
        state = State.INPUT_START;

        imagesBank = new ImagesBank();

        smieci();

        addMouseListener(new MouseHandler());
    }

    private void smieci() {
        stateBoard = new int[ImagesBank.IMAGE_COUNT];
        for(int i=0; i<ImagesBank.IMAGE_COUNT; ++i)
            stateBoard[i] = i + 1;
        stateBoard[8] = 0;
    }

    public void setState(State state) {
        this.state = state;
    }

    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if(state == State.ANIMATION)
            paintAnimation(g);
        else
            paintInputButtons(g);
    }

    private void paintInputButtons(Graphics g) {
        Image images[] = imagesBank.images;

        for(int i = 0; i < ImagesBank.IMAGE_COUNT; ++i) {
            int num = stateBoard[i];
            int x = (i % 3) * images[num].getWidth(null);
            int y = (i / 3) * images[num].getHeight(null);
            g.drawImage(images[num], x, y, null);
        }
    }

    private void paintAnimation(Graphics g) {

    }

    public Dimension getPreferredSize() {
        int width = imagesBank.images[0].getWidth(null);
        int height = imagesBank.images[0].getHeight(null);
        return new Dimension(width*3, height*3);
    }

    private class MouseHandler extends MouseAdapter {
        public void mousePressed(MouseEvent event) {
        }

        public void mouseReleased(MouseEvent event) {
        }

        public void mouseClicked(MouseEvent event) {
            Point2D point = event.getPoint();
            System.out.println("x: " + point.getX() + " | y: " + point.getY());
        }
    }
}
