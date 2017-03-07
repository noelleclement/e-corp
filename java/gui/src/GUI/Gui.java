package GUI;

import Backend.*;

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
    private ActiveScreen activeScreen;
    private Backend backend;

    private KeyboardHandler k;
    public GUI(Backend backend) {
        this.backend = backend;
        this.mainScreen = new MainScreen();
        k = new KeyboardHandler(this);
        mainScreen.addKeyListener(k);//TODO remove after buttons
        this.activeScreen = ActiveScreen.MAINSCREEN;
    }

    //TODO only temporary I SWEAR TESTING ONLY
    private String accountNumber="";

    public void keyPressed(char key) {
        System.out.println(key);
        switch(activeScreen) {
            case MAINSCREEN:
                if(Character.isDigit(key)) {
                    accountNumber = accountNumber+key;
                    mainScreen.tempLabelAccountNumber.setText(accountNumber);
                }
                if(Character.isSpaceChar(key)) {
                    try {
                        System.out.println(accountNumber);
                        backend.users.findUser(accountNumber);
                        this.activeScreen = ActiveScreen.PINSCREEN;
                        this.mainScreen.setVisible(false);
                        this.mainScreen = null;
                        this.pinScreen = new PinScreen(backend);
                        pinScreen.addKeyListener(k);//TODO remove after keypad

                    } catch (UnexsistingUserException e) {
                        System.out.println("nonexsiting user");
                    }
                }
                break;
            case PINSCREEN:
                if(Character.isDigit(key)) {
                    pinScreen.numberEntered(Character.getNumericValue(key));
                } else if(Character.isLowerCase(key)) {
                    switch(key) {
                        case 'a': //Backspace
                            pinScreen.backSpace();
                            break;
                        case 'b':
                            if(backend.checkPin(pinScreen.getPin())) {
                                break;
                            } else {
                                pinScreen.wrongPin(backend.getCurrentUser().getPinErrors());
                            }
                                break;
                    }
                }
                break;
        }
    }
    private enum ActiveScreen {
        MAINSCREEN, PINSCREEN
    }
}
