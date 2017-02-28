package GUI;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

/**
 * Created by Hans de Rooij on 24/02/2017.
 */
public class MainScreen extends ButtonlessScreen {
    public MainScreen() {
        super();
        //HTML seems to be the easiest way to introduce line breaks
        mainTextLabel.setText("<html><p align='center'>Welcome to the Bank of E - Corp,<br />" +
                              "Please insert your card</p></html>");
    }
}
