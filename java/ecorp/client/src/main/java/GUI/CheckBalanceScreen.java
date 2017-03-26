package GUI;

import Backend.User;

/**
 * Created by Hans de Rooij on 07/03/2017.
 */
public class CheckBalanceScreen extends ButtonScreen {
    public CheckBalanceScreen(User user) {
        super();
        this.mainTextLabel.setText("<html>Hello "+user.getName()+",<br />you have "+user.getBalance()+"<br />on your account");
        rightButtons[0].setIdentifier("A");
        rightButtons[0].setText("Withdraw money");
    }
}
