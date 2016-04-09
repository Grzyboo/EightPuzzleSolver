package Base;

import Base.solver.Solver;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainWindow extends JFrame {

    AnimationPanel animationPanel;
    JButton[] numberButtons;
    JButton solvingButton;

    int[] currentBoard;

    int[] startBoard;
    int[] endBoard;

    public MainWindow() {
        setTitle("8-Puzzle Solver");

        startBoard = new int[9];
        endBoard = new int[9];

        for(int i = 0; i < 9; ++i) {
            startBoard[i] = -1;
            endBoard[i] = -1;
        }

        currentBoard = startBoard;

        createMainPanel();

        pack();
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

        solvingButton = new JButton("Solve");
        solvingButton.addActionListener(e -> solve());
        solvingButton.setEnabled(false);
        menuPanel.add(solvingButton);

        return menuPanel;
    }

    private JPanel createMapSettingPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(3, 3));

        numberButtons = new JButton[ImagesBank.IMAGE_COUNT];

        for(int i = 0; i < ImagesBank.IMAGE_COUNT; ++i) {
            String name;

            if(i == 0)
                name = "|   |";
            else
                name = "| " + i + " |";

            numberButtons[i] = new JButton(name);
            numberButtons[i].addActionListener(new NumberChangingButtonsListener(i));
            panel.add(numberButtons[i]);
        }

        Border border = BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Maps setting");
        panel.setBorder(border);

        return panel;
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

        return panel;
    }

    private void solve() {
        Solver solver = new Solver(startBoard, endBoard);
        System.out.println(solver.getSolutionString());

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
            try {
                currentBoard[animationPanel.getCurrentlySelectedSquare()] = number;
                animationPanel.moveCurrentlySelectedSquare();
            } catch (AllFieldsFilledException ex) {

            }
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
