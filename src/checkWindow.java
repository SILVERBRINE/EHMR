package src;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class checkWindow extends WindowAdapter {
    @Override
    public void windowClosing(WindowEvent e) {
        // Code to execute when window is being closed
        System.out.println("Window is closed...");

        GUI.setVisible(true);
    }
}