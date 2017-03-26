package GUI;

/**
 * Created by Hans de Rooij on 10/03/2017.
 */
public class WithdrawAmountConfirmScreen extends ButtonScreen {
    public WithdrawAmountConfirmScreen(int amount) {
        this.rightButtons[0].setIdentifier("A");
        this.rightButtons[0].setText("yes");
        this.rightButtons[1].setIdentifier("B");
        this.rightButtons[1].setText("no");
        this.mainTextLabel.setText("<html>Do you want to take<br>"+amount+"?");
    }
}
