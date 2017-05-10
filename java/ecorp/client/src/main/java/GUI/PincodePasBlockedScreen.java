package GUI;

/**
 * Created by Hans de Rooij on 05/04/2017.
 */
public class PincodePasBlockedScreen extends ButtonlessScreen {
    public PincodePasBlockedScreen() {
        super();
        this.mainTextLabel.setText("<html><p style='align:center;'>u hebt de verkeerde pincode<br>" +
                "te vaak ingevoerd<br>" +
                "uw pas is geblokkeerd</p></html>");
    }
}
