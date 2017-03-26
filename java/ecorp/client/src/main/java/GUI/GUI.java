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
    private ChooseActionScreen chooseActionScreen;
    private CheckBalanceScreen checkBalanceScreen;
    private WithdrawMoneyScreen withdrawMoneyScreen;
    private WithdrawAmountConfirmScreen withdrawAmountConfirmScreen;
    private EndScreen endScreen;
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
                            if(pinScreen.getPin().length()<3)
                                break;
                            if(backend.checkPin(pinScreen.getPin())) {
                                this.activeScreen = ActiveScreen.CHOOSE_ACTION_SCREEN;
                                this.pinScreen.setVisible(false);
                                this.pinScreen = null;
                                this.chooseActionScreen = new ChooseActionScreen();
                                chooseActionScreen.addKeyListener(k);
                                break;
                            } else {
                                pinScreen.wrongPin(backend.getCurrentUser().getPinErrors());
                            }
                                break;
                    }
                }
                break;
            case CHOOSE_ACTION_SCREEN:
                if(Character.isLowerCase(key)) {
                    switch (key) {
                        case 'a':
                            this.activeScreen = ActiveScreen.CHECK_BALANCE_SCREEN;
                            this.chooseActionScreen.setVisible(false);
                            this.chooseActionScreen = null;
                            this.checkBalanceScreen = new CheckBalanceScreen(backend.getCurrentUser());
                            checkBalanceScreen.addKeyListener(k);
                            break;
                        case 'b':
                            this.activeScreen = ActiveScreen.WITHDRAWMONEYSCREEN;
                            this.chooseActionScreen.setVisible(false);
                            this.chooseActionScreen = null;
                            this.withdrawMoneyScreen = new WithdrawMoneyScreen();
                            withdrawMoneyScreen.addKeyListener(k);
                            break;
                    }
                }
                break;
            case CHECK_BALANCE_SCREEN:
                if(Character.isLowerCase(key)) {
                    switch (key) {
                        case 'a':
                            this.activeScreen = ActiveScreen.WITHDRAWMONEYSCREEN;
                            this.checkBalanceScreen.setVisible(false);
                            this.checkBalanceScreen = null;
                            this.withdrawMoneyScreen = new WithdrawMoneyScreen();
                            withdrawMoneyScreen.addKeyListener(k);
                            break;
                    }
                }
                break;
            case WITHDRAWMONEYSCREEN:
                if(Character.isLowerCase(key)) {
                    switch (key) {
                        case 'a':
                            this.withdrawMoneyScreen.backSpace();
                            break;
                        case 'b':
                            int amount = withdrawMoneyScreen.getEnteredAmount();
                            this.activeScreen = ActiveScreen.WITHDRAWAMOUNTCONFIRMSCREEN;
                            this.withdrawMoneyScreen.setVisible(false);
                            this.withdrawMoneyScreen = null;
                            this.withdrawAmountConfirmScreen = new WithdrawAmountConfirmScreen(amount);
                            withdrawAmountConfirmScreen.addKeyListener(k);
                            break;
                    }
                } else if(Character.isDigit(key)) {
                    this.withdrawMoneyScreen.newNumber(Character.getNumericValue(key));
                }
                break;
            case WITHDRAWAMOUNTCONFIRMSCREEN:
                if(Character.isLowerCase(key)) {
                    switch (key) {
                        case 'a':
                            this.activeScreen = ActiveScreen.ENDSCREEN;
                            this.withdrawAmountConfirmScreen.setVisible(false);
                            this.withdrawAmountConfirmScreen = null;
                            this.endScreen = new EndScreen();
                            endScreen.addKeyListener(k);
                            break;
                        case 'b':
                            this.activeScreen = ActiveScreen.WITHDRAWMONEYSCREEN;
                            this.withdrawAmountConfirmScreen.setVisible(false);
                            this.withdrawAmountConfirmScreen = null;
                            this.withdrawMoneyScreen = new WithdrawMoneyScreen();
                            withdrawMoneyScreen.addKeyListener(k);
                            break;
                    }
                }
                break;
        }
    }
    private enum ActiveScreen {
        MAINSCREEN, PINSCREEN, CHOOSE_ACTION_SCREEN, CHECK_BALANCE_SCREEN, WITHDRAWMONEYSCREEN, WITHDRAWAMOUNTCONFIRMSCREEN,
        ENDSCREEN
    }
}
