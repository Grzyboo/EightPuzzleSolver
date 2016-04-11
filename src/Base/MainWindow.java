package Base;

import Base.solver.Movement;
import Base.solver.Solver;
import Base.solver.SolverThread;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedList;

public class MainWindow extends JFrame {

    private AnimationPanel animationPanel;
    private JButton[] numberButtons;
    private JButton solvingButton;

    private LoadingLabel loadingLabel;

    private int[] currentBoard;

    //private int[] startBoard = {5, 1, 7, 4, 8, 0, 2, 3, 6};
    //private int[] endBoard = {1, 4, 7, 2, 5, 8, 3, 6, 0};
    private int[] startBoard = {1, 2, 3, 4, 5, 6, 7, 8, 0};
    private int[] endBoard = {4, 3, 2, 5, 0, 6, 1, 7, 8};

    public MainWindow() {
        setTitle("8-Puzzle Solver");

        currentBoard = startBoard;
        //initBoards();

        createMainPanel();

        pack();
    }

    private void initBoards() {
        startBoard = new int[9];
        endBoard = new int[9];

        for(int i = 0; i < 9; ++i) {
            startBoard[i] = -1;
            endBoard[i] = -1;
        }

    }

    private void createMainPanel() {
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new FlowLayout());
        add(mainPanel);

        animationPanel = createPuzzlePanel();
        mainPanel.add(animationPanel);

        mainPanel.add(createMenuPanel());
    }

    private AnimationPanel createPuzzlePanel() {
        return new AnimationPanel(startBoard);
    }

    private JPanel createMenuPanel() {
        JPanel menuPanel = new JPanel();
        menuPanel.setLayout(new BoxLayout(menuPanel, BoxLayout.PAGE_AXIS));

        JPanel stateChangingPanel = createStateChangingPanel();
        menuPanel.add(stateChangingPanel);

        JPanel mapSettingPanel = createMapSettingPanel();
        menuPanel.add(mapSettingPanel);


        JPanel solvingPanel = new JPanel();

        loadingLabel = new LoadingLabel();
        solvingPanel.add(loadingLabel);
        loadingLabel.setVisible(false);

        solvingButton = new JButton("Solve");
        solvingButton.addActionListener(e -> solve());
        //solvingButton.setEnabled(false);
        solvingButton.setEnabled(true);
        solvingPanel.add(solvingButton);


        menuPanel.add(solvingPanel);

        return menuPanel;
    }

    private JPanel createMapSettingPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(3, 3));

        numberButtons = new JButton[ImagesBank.IMAGE_COUNT];

        for(int i = 1; i < ImagesBank.IMAGE_COUNT; ++i)
            addNumberButton(panel, i, "| " + i + " |");

        addNumberButton(panel, 0, "|   |");


        Border border = BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Maps setting");
        panel.setBorder(border);

        return panel;
    }

    private void addNumberButton(JPanel panel, int i, String label) {
        numberButtons[i] = new JButton(label);
        numberButtons[i].addActionListener(new NumberChangingButtonsListener(i));
        panel.add(numberButtons[i]);
    }

    private JPanel createStateChangingPanel() {
        JPanel panel = new JPanel();

        ButtonGroup group = new ButtonGroup();

        JRadioButton buttonStart = new JRadioButton("Start", true);
        buttonStart.addActionListener(new StateChangingButtonsListener(State.STATE_START));
        group.add(buttonStart);
        panel.add(buttonStart);

        JRadioButton buttonFinish = new JRadioButton("Finish", false);
        buttonFinish.addActionListener(new StateChangingButtonsListener(State.STATE_FINISH));
        group.add(buttonFinish);
        panel.add(buttonFinish);

        JRadioButton buttonAnimation = new JRadioButton("Animation", false);
        buttonAnimation.addActionListener(new StateChangingButtonsListener(State.STATE_ANIMATION));
        group.add(buttonAnimation);
        panel.add(buttonAnimation);

        //buttonAnimation.setEnabled(false);
        buttonAnimation.setEnabled(true);

        return panel;
    }

    private void solve() {
        SolverThread solver = new SolverThread(this, new Solver(startBoard, endBoard));
        solver.activate();

        loadingLabel.setVisible(true);
    }

    public void receiveSolution(LinkedList<Movement> list) {
        System.out.println(list.toString());
        animationPanel.setMovesSequence(list);
        loadingLabel.setVisible(false);
    }

    private class StateChangingButtonsListener implements ActionListener {
        State state;

        public StateChangingButtonsListener(State state) {
            this.state = state;
        }

        public void actionPerformed(ActionEvent e) {
            changeAnimationPanelState();
            enableButtons();
        }

        private void changeAnimationPanelState() {
            if(state == State.STATE_FINISH)
                currentBoard = endBoard;
            else currentBoard = startBoard;

            animationPanel.setState(state, currentBoard);
        }

        private void enableButtons() {
            if(state == State.STATE_ANIMATION) {
                for(JButton button : numberButtons) {
                    button.setEnabled(false);
                }
            }
            else {
                for(int i = 0; i < ImagesBank.IMAGE_COUNT; ++i) {
                    numberButtons[i].setEnabled(true);
                }

                for(int i = 0; i < ImagesBank.IMAGE_COUNT; ++i) {
                    if(currentBoard[i] != -1) {
                        int num = currentBoard[i];
                        numberButtons[num].setEnabled(false);
                    }
                }
            }
        }
    }

    private class NumberChangingButtonsListener implements ActionListener {
        int number;

        public NumberChangingButtonsListener(int number) {
            this.number = number;
        }

        public void actionPerformed(ActionEvent e) {

            int squarePosition = animationPanel.getSquareToBeFramed();

            currentBoard[squarePosition] = number;
            animationPanel.repaint();

            numberButtons[number].setEnabled(false);

            enableSolvingButton();
        }
    }

    private void enableSolvingButton() {

        if(solvingButton.isEnabled())
            return;

        boolean emptyFieldExists = false;

        for(int position : startBoard) {
            if(position == -1) {
                emptyFieldExists = true;
                break;
            }
        }

        for(int position : endBoard) {
            if(position == -1) {
                emptyFieldExists = true;
                break;
            }
        }

        if(!emptyFieldExists)
            solvingButton.setEnabled(true);
    }


}
