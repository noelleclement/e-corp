package GUI;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class KeyboardHandler extends KeyAdapter {
    private GUI GUI;

    public KeyboardHandler(GUI GUI) {
        super();
        this.GUI = GUI;
    }

    @Override
    public void keyPressed(KeyEvent e) {
        System.out.println(e.getKeyChar());
        GUI.keyPressed(e.getKeyChar());
    }
}