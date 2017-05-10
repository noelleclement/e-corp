package GUI;

import Backend.User;

/**
 * Created by Hans de Rooij on 07/03/2017.
 */
public class CheckBalanceScreen extends ButtonScreen {
    public CheckBalanceScreen(double balance) {
        super();
        this.mainTextLabel.setText("<html>U hebt <br>"+balance+"<br />op uw rekening");
        rightButtons[0].setIdentifier("A");
        rightButtons[0].setText("Bedrag invoeren");
        rightButtons[1].setIdentifier("B");
        rightButtons[1].setText("Biljetkeuze");
    }
}
