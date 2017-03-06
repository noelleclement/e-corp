package GUI;


import com.sun.deploy.util.StringUtils;

/**
 * Created by Hans de Rooij on 21/02/2017.
 *
 * This class is for handling all the different gui components. It'll
 * also initialize the different handlers
 *
 * basically all the front end peasantry
 */
public class GUI {
    private MainScreen mainScreen;
    private PinScreen pinScreen;
    private KeyboardHandler k;
    private ActiveScreen activeScreen;
    public GUI() {
        k = new KeyboardHandler(this);
        this.mainScreen = new MainScreen();
        mainScreen.addKeyListener(k);
        this.activeScreen = ActiveScreen.MAINSCREEN;
    }

    //TODO only temporary I SWEAR TESTING ONLY
    private String accountNumber="";
    private boolean isNumeric(String str)
    {
        try
        {
            double d = Double.parseDouble(str);
        }
        catch(NumberFormatException nfe)
        {
            return false;
        }
        return true;
    }
    public void keyPressed(char key) {
        System.out.println(key);
        switch(activeScreen) {
            case MAINSCREEN:
                if(isNumeric(Character.toString(key))) {
                    accountNumber = accountNumber+key;
                    mainScreen.tempLabelAccountNumber.setText(accountNumber);
                }
                if(Character.isSpaceChar(key)) {
                    this.activeScreen = ActiveScreen.PINSCREEN;
                    this.mainScreen.setVisible(false);
                    this.pinScreen = new PinScreen();
                }
                break;
        }
    }
    private enum ActiveScreen {
        MAINSCREEN, PINSCREEN
    }
}
