package GUI;

/**
 * Created by Hans de Rooij on 05/04/2017.
 */
public class PincodePasBlockedScreen extends ButtonlessScreen {
    public PincodePasBlockedScreen() {
        super();
        this.mainTextLabel.setText("<html>you have entered the wrong pin<br>" +
                "too often<br>" +
                "your pas is blocked");
    }
}
