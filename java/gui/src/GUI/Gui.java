package GUI;

import javax.swing.*;

/**
 * Created by Hans de Rooij on 21/02/2017.
 */
public class Gui {
    private JFrame mainScreen;
    public Gui() {
        this.mainScreen = new JFrame("ATM E corp");
        mainScreen.setSize(400, 400);
        mainScreen.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        mainScreen.setVisible(true);
    }
}
