package GUI;

/**
 * Created by Hans de Rooij on 07/03/2017.
 */
public class ChooseActionScreen extends ButtonScreen {
    public ChooseActionScreen() {
        super();
        rightButtons[0].setIdentifier("A");
        rightButtons[0].setText("Controleer balans");
        rightButtons[1].setIdentifier("B");
        rightButtons[1].setText("Geld opnemen zonder biljetkeuze");
        rightButtons[2].setText("Geld opnemen met biljetkeuze");
        rightButtons[2].setIdentifier("C");
        this.mainTextLabel.setText("Kies een optie om te doen");
    }
}
