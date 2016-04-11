package Base;

import Base.solver.Movement;
import sun.awt.image.ImageWatched;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;
import java.util.LinkedList;

public class AnimationPanel extends JPanel {
    private static int NO_SQUARE_TO_BE_FRAMED = -1;

    private State state;
    private ImagesBank imagesBank;

    private int stateBoard[];

    private Timer timer;
    private LinkedList<Movement> solutionMovesSequence = null;
    private int tickCounter = 0;

    private static final int MOVE_SPEED = 4;

    public AnimationPanel(int startBoard[]) {
        state = State.STATE_START;
        stateBoard = startBoard;

        //
        solutionMovesSequence = new LinkedList<>();
        solutionMovesSequence.add(Movement.UP);
        solutionMovesSequence.add(Movement.UP);
        solutionMovesSequence.add(Movement.LEFT);
        solutionMovesSequence.add(Movement.DOWN);
        solutionMovesSequence.add(Movement.LEFT);
        solutionMovesSequence.add(Movement.UP);
        solutionMovesSequence.add(Movement.RIGHT);
        solutionMovesSequence.add(Movement.DOWN);
        solutionMovesSequence.add(Movement.RIGHT);
        solutionMovesSequence.add(Movement.DOWN);
        solutionMovesSequence.add(Movement.LEFT);
        solutionMovesSequence.add(Movement.LEFT);
        solutionMovesSequence.add(Movement.UP);
        solutionMovesSequence.add(Movement.UP);
        solutionMovesSequence.add(Movement.RIGHT);
        solutionMovesSequence.add(Movement.DOWN);
        //

        timer = new Timer(100, new TimerListener());

        imagesBank = new ImagesBank();

        addMouseListener(new MouseHandler());
    }

    public int getSquareToBeFramed() {
        for(int i = 0; i < ImagesBank.IMAGE_COUNT; ++i) {
            if(stateBoard[i] == -1)
                return i;
        }

        return NO_SQUARE_TO_BE_FRAMED;
    }

    public void setState(State state, int[] board) {
        this.state = state;
        stateBoard = board;
        repaint();

        if(state == State.STATE_ANIMATION) {
            tickCounter = 0;
            timer.start();
        }
        else {
            timer.stop();
        }

    }

    public void setMovesSequence(LinkedList<Movement> list) {
        solutionMovesSequence = list;
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
        int squarePosition = getSquareToBeFramed();
        if(squarePosition == NO_SQUARE_TO_BE_FRAMED)
            return;

        Image images[] = imagesBank.images;
        int width = images[squarePosition].getWidth(null);
        int height = images[squarePosition].getHeight(null);
        int x = (squarePosition % 3) * width;
        int y = (squarePosition / 3) * height;

        g.setColor(Color.RED);
        g.drawRect(x, y, width - 1, height - 1);
    }

    private Movement nextMove = null;
    private int currentlyMovingPosition;
    private int currentlyMovingX;
    private int currentlyMovingY;

    private void paintAnimation(Graphics g) {
        if(solutionMovesSequence == null)
            return;
        Image images[] = imagesBank.images;

        for(int i = 0; i < ImagesBank.IMAGE_COUNT; ++i) {
            int num = stateBoard[i];

            if(num == -1)
                num = 0;

            if(i == currentlyMovingPosition) {
                g.drawImage(images[num], currentlyMovingX, currentlyMovingY, null);
            }
            else {
                int x = (i % 3) * images[num].getWidth(null);
                int y = (i / 3) * images[num].getHeight(null);
                g.drawImage(images[num], x, y, null);
            }
        }
    }

    private class TimerListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            ++tickCounter;
            System.out.println(tickCounter);

            if(tickCounter % 16 == 0) {

                int zeroPosition = 0;
                for (int i = 0; i < ImagesBank.IMAGE_COUNT; ++i) {
                    if (stateBoard[i] == 0)
                        zeroPosition = i;
                }

                nextMove = solutionMovesSequence.removeFirst();

                currentlyMovingPosition = zeroPosition;
                currentlyMovingX = imagesBank.getImageWidth() * (currentlyMovingPosition % 3);
                currentlyMovingX = imagesBank.getImageHeight() * (currentlyMovingPosition / 3);

                switch (nextMove) {
                    case UP:
                        currentlyMovingPosition -= 3;
                        break;
                    case DOWN:
                        currentlyMovingPosition += 3;
                        break;
                    case LEFT:
                        --currentlyMovingPosition;
                        break;
                    case RIGHT:
                        ++currentlyMovingPosition;
                        break;
                }
                repaint();
            }
            else if(nextMove != null) {
                switch (nextMove) {
                    case UP:
                        currentlyMovingY += MOVE_SPEED;
                        break;
                    case DOWN:
                        currentlyMovingY -= MOVE_SPEED;
                        break;
                    case LEFT:
                        currentlyMovingY += MOVE_SPEED;
                        break;
                    case RIGHT:
                        currentlyMovingY -= MOVE_SPEED;
                        break;
                }

                if(tickCounter % 16 == 15) {

                    int zeroPosition = 0;
                    for (int i = 0; i < ImagesBank.IMAGE_COUNT; ++i) {
                        if (stateBoard[i] == 0)
                            zeroPosition = i;
                    }

                    int currentlyMoving = stateBoard[currentlyMovingPosition];
                    stateBoard[currentlyMovingPosition] = 0;
                    stateBoard[zeroPosition] = currentlyMoving;
                }
                else repaint();
            }

        }
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