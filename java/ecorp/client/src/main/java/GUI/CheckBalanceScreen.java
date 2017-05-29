package GUI;

import Backend.User;

import java.text.DecimalFormat;

/**
 * Created by Hans de Rooij on 07/03/2017.
 */
public class CheckBalanceScreen extends ButtonScreen {
    public CheckBalanceScreen(double balance) {
        super();
        DecimalFormat df = new DecimalFormat("#.00");
        this.mainTextLabel.setText("<html>U hebt <br>"+df.format(balance)+"<br />op uw rekening");
        rightButtons[0].setIdentifier("A");
        rightButtons[0].setText("Bedrag invoeren");
        rightButtons[1].setIdentifier("B");
        rightButtons[1].setText("Biljetkeuze");
    }
}
