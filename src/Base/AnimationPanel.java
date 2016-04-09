package Base;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;

public class AnimationPanel extends JPanel {
    private static int NO_SQUARE_TO_BE_FRAMED = -1;

    private State state;
    private ImagesBank imagesBank;

    private int currentlySelectedSquare;
    private int stateBoard[];

    public AnimationPanel(int startBoard[]) {
        state = State.STATE_START;
        stateBoard = startBoard;
        currentlySelectedSquare = 0;

        imagesBank = new ImagesBank();


        addMouseListener(new MouseHandler());
    }

    private int getSquareToBeFramed() {
        for(int i = 0; i < ImagesBank.IMAGE_COUNT; ++i) {
            if(stateBoard[i] == -1)
                return i;
        }

        return NO_SQUARE_TO_BE_FRAMED;
    }

    public int getCurrentlySelectedSquare() throws AllFieldsFilledException {
        if(currentlySelectedSquare == -1) {
            moveCurrentlySelectedSquare();
            return currentlySelectedSquare;
        }

        if(stateBoard[currentlySelectedSquare] == -1)
            return currentlySelectedSquare;

        moveCurrentlySelectedSquare();
        return currentlySelectedSquare;
    }

    public void moveCurrentlySelectedSquare() throws AllFieldsFilledException {
        for(int i = 0; i < ImagesBank.IMAGE_COUNT; ++i) {
            if(stateBoard[i] == -1) {
                currentlySelectedSquare = i;
                return;
            }
        }

        currentlySelectedSquare = -1;
        throw new AllFieldsFilledException();
    }

    public void setState(State state, int[] board) {
        this.state = state;
        stateBoard = board;
        try {
            moveCurrentlySelectedSquare();
        } catch(AllFieldsFilledException ex) {
            System.out.println(ex.toString());
        } finally {
            repaint();
        }
    }

    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if(state == State.STATE_ANIMATION)
            paintAnimation(g);
        else
            paintInputButtons(g);
    }

    private void paintInputButtons(Graphics g) {
        Image images[] = imagesBank.images;

        for(int i = 0; i < ImagesBank.IMAGE_COUNT; ++i) {
            int num = stateBoard[i];

            if(num == -1)
                num = 0;

            int x = (i % 3) * images[num].getWidth(null);
            int y = (i / 3) * images[num].getHeight(null);
            g.drawImage(images[num], x, y, null);
        }

        drawPointingRectangle(g);
    }

    private void drawPointingRectangle(Graphics g) {
        if(currentlySelectedSquare == -1)
            return;

        Image images[] = imagesBank.images;
        int width = images[currentlySelectedSquare].getWidth(null);
        int height = images[currentlySelectedSquare].getHeight(null);
        int x = (currentlySelectedSquare % 3) * width;
        int y = (currentlySelectedSquare / 3) * height;

        g.setColor(Color.RED);
        g.drawRect(x, y, width - 1, height - 1);
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


class AllFieldsFilledException extends Exception {
    @Override
    public String toString() {
        return super.toString();
    }
}