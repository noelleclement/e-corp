package GUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

/**
 * Created by Hans de Rooij on 24/02/2017.
 */
public class MainScreen extends ButtonlessScreen {
    public JLabel tempLabelAccountNumber = new JLabel("-");//TODO remove after testing
    public MainScreen() {
        super();
        //HTML seems to be the easiest way to introduce line breaks
        mainTextLabel.setText("<html><p align='center'>Welkom bij de E-Corp bank,<br />" +
                              "Voer uw pas in</p></html>");
        addTempLabel();;
    }
    private void addTempLabel() { //TODO remove after testing
        GridBagConstraints tempAccountLabelConstraints = new GridBagConstraints();
        tempAccountLabelConstraints.fill = GridBagConstraints.HORIZONTAL;
        tempAccountLabelConstraints.weightx = 0.5;
        tempAccountLabelConstraints.weighty = 1;
        tempAccountLabelConstraints.gridx = 0;
        tempAccountLabelConstraints.gridy = 2;
        tempAccountLabelConstraints.gridwidth = 2;
        tempAccountLabelConstraints.anchor = GridBagConstraints.PAGE_END;
        tempAccountLabelConstraints.ipadx = 10;

        add(tempLabelAccountNumber, tempAccountLabelConstraints);
    }
    public void incorrectRekeningnummer() {
        this.mainTextLabel.setText("<html><p align='center'>Deze kaart wordt niet herkend</p></html>");
        this.tempLabelAccountNumber.setText("");
    }
    public void pasGeblokkeerd() {
        this.mainTextLabel.setText("<html><p align='center'>Deze kaart is geblokkeerd<br>Neem contact op met uw bank</p></html>");
        this.tempLabelAccountNumber.setText("");
    }
}
