package Base;

import javax.swing.*;
import java.awt.*;

public class Main {
    public static void main(String[] args) {
        Runnable thread = () -> {
            MainWindow frame = new MainWindow();
            frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
            frame.setVisible(true);

            setLookAndFeel(frame);
        };

        EventQueue.invokeLater(thread);
    }

    private static void setLookAndFeel(Frame frame) {
        try {
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
            SwingUtilities.updateComponentTreeUI(frame);
        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }

}
