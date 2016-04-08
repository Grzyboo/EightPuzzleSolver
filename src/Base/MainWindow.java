package Base;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainWindow extends JFrame {

    public MainWindow() {
        setTitle("8-Puzzle Solver");

        createMainPanel();

        pack();
    }

    private void createMainPanel() {
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new FlowLayout());
        add(mainPanel);

        mainPanel.add(createPuzzlePanel());
        mainPanel.add(createMenuPanel());
    }

    private JPanel createPuzzlePanel() {
        return new ImagesAnimationPanel();
    }

    private JPanel createMenuPanel() {
        JPanel menuPanel = new JPanel();
        menuPanel.setLayout(new BoxLayout(menuPanel, BoxLayout.PAGE_AXIS));

        JPanel stateChangingPanel = createStateChangingPanel();
        menuPanel.add(stateChangingPanel);

        JPanel mapSettingPanel = createMapSettingPanel();
        menuPanel.add(mapSettingPanel);

        JButton buttonSolve = new JButton("Solve");
        buttonSolve.addActionListener(e -> solve());
        buttonSolve.setEnabled(false);
        menuPanel.add(buttonSolve);

        return menuPanel;
    }

    private JPanel createMapSettingPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(3, 3));

        panel.add(new JButton("[   ]"));

        for(int i = 1; i < ImagesBank.IMAGE_COUNT; ++i)
            panel.add(new JButton("[ " + i + " ]"));

        Border border = BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Maps setting");
        panel.setBorder(border);

        return panel;
    }

    private JPanel createStateChangingPanel() {
        JPanel panel = new JPanel();

        ButtonGroup group = new ButtonGroup();

        JRadioButton buttonStart = new JRadioButton("Start", true);
        buttonStart.addActionListener();
        group.add(buttonStart);
        panel.add(buttonStart);

        JRadioButton buttonFinish = new JRadioButton("Finish", false);
        group.add(buttonFinish);
        panel.add(buttonFinish);

        JRadioButton buttonAnimation = new JRadioButton("Animation", false);
        group.add(buttonAnimation);
        panel.add(buttonAnimation);

        return panel;
    }

    private void solve() {

    }

    private class StateChangingButtonsListener implements ActionListener {
        State state;

        public StateChangingButtonsListener(State state) {
            this.state = state;
        }

        public void actionPerformed(ActionEvent e) {
            switch(state) {
                case INPUT_START:
                    break;
                case INPUT_FINISH:
                    break;
                case ANIMATION:
                    break;
            }
            ...

        }
    }


}
