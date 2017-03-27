package GUI;

/**
 * Created by Hans de Rooij on 07/03/2017.
 */
public class ChooseActionScreen extends ButtonScreen {
    public ChooseActionScreen() {
        super();
        rightButtons[0].setIdentifier("A");
        rightButtons[0].setText("Check balance");
        rightButtons[1].setIdentifier("B");
        rightButtons[1].setText("Withdraw money");
    }
}
