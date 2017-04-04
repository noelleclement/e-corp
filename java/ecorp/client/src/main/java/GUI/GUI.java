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
public class GUI {//TODO add exit on every screen
    private MainScreen mainScreen;
    private PinScreen pinScreen;
    private ActiveScreen activeScreen;
    private ChooseActionScreen chooseActionScreen;
    private CheckBalanceScreen checkBalanceScreen;
    private WithdrawMoneyScreen withdrawMoneyScreen;
    private WithdrawAmountConfirmScreen withdrawAmountConfirmScreen;
    private EndScreen endScreen;
    private InteruptScreen interuptScreen;
    private BiljetkeuzeScreen biljetkeuzeScreen;
    private API api;
    private Transaction transaction;
    private JsonResponse response;

    private KeyboardHandler k;
    public GUI() {
        this.api = new API();
        this.mainScreen = new MainScreen();
        k = new KeyboardHandler(this);
        mainScreen.addKeyListener(k);//TODO remove after buttons
        this.activeScreen = ActiveScreen.MAINSCREEN;
    }

    //TODO only temporary I SWEAR TESTING ONLY
    private String accountNumber="";
    private ActiveScreen lastScreen;
    public void keyPressed(char key) {
        System.out.println(key);
        switch(activeScreen) {
            case MAINSCREEN:
                if(Character.isDigit(key)) {
                    accountNumber = accountNumber+key;
                    mainScreen.tempLabelAccountNumber.setText(accountNumber);
                }
                if(Character.isSpaceChar(key)) {
                    System.out.println(accountNumber);
                    JsonResponses.ControleerRekeningnummer response = api.isCorrectCard(accountNumber, "111222");
                    if(response.type.equals("CORRECT_REKENINGNUMMER")) {
                        this.transaction = new Transaction(response.IBAN,
                                response.transaction_id,
                                response.card_uid);
                        this.activeScreen = ActiveScreen.PINSCREEN;
                        this.mainScreen.setVisible(false);
                        this.mainScreen = null;
                        this.pinScreen = new PinScreen();
                        pinScreen.addKeyListener(k);//TODO remove after keypad
                    } else if(response.type.equals("INCORRECT_REKENINGNUMMER")) {
                        this.mainScreen.incorrectRekeningnummer();
                    } else if(response.type.equals("PAS_GEBLOKKEERD")) {
                        this.mainScreen.pasGeblokkeerd();
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
                            if(pinScreen.getPin().length()<4)
                                break;
                            response = api.checkPinCode(transaction.getTransactionId(),
                                                        transaction.getIBAN(),
                                                        pinScreen.getPin(),
                                                        transaction.getCARD_UID());
                            if(response.type.equals("CORRECTE_PINCODE")) {
                                this.activeScreen = ActiveScreen.CHOOSE_ACTION_SCREEN;
                                this.pinScreen.setVisible(false);
                                this.pinScreen = null;
                                this.chooseActionScreen = new ChooseActionScreen();
                                chooseActionScreen.addKeyListener(k);
                            }else if(response.type.equals("INCORRECTE_PINCODE")) {
                                JsonResponses.IncorrectePincode wrongPinResponse = (JsonResponses.IncorrectePincode) response;
                                pinScreen.wrongPin(wrongPinResponse.pogingen);

                            }
                            break;
                        case 'd':
                            this.activeScreen = ActiveScreen.INTERUPTEDSCREEN;
                            this.pinScreen.setVisible(false);
                            this.pinScreen = null;
                            this.interuptScreen = new InteruptScreen();
                            interuptScreen.addKeyListener(k);
                            break;
                    }
                }
                break;
            case CHOOSE_ACTION_SCREEN:
                if(Character.isLowerCase(key)) {
                    switch (key) {
                        case 'a':
                            JsonResponses.SaldoInformatie response = api.saldoOpvragen(transaction.getTransactionId(),
                                    transaction.getIBAN());
                            this.activeScreen = ActiveScreen.CHECK_BALANCE_SCREEN;
                            this.chooseActionScreen.setVisible(false);
                            this.chooseActionScreen = null;
                            this.checkBalanceScreen = new CheckBalanceScreen(response.saldo);
                            checkBalanceScreen.addKeyListener(k);
                            break;
                        case 'b':
                            this.activeScreen = ActiveScreen.WITHDRAWMONEYSCREEN;
                            this.chooseActionScreen.setVisible(false);
                            this.chooseActionScreen = null;
                            this.withdrawMoneyScreen = new WithdrawMoneyScreen();
                            withdrawMoneyScreen.addKeyListener(k);
                            break;
                        case 'c':
                            this.activeScreen = ActiveScreen.BILJETKEUZESCREEN;
                            this.chooseActionScreen.setVisible(false);
                            this.chooseActionScreen = null;
                            this.biljetkeuzeScreen = new BiljetkeuzeScreen();
                            biljetkeuzeScreen.addKeyListener(k);
                            break;
                        case 'd':
                            this.activeScreen = ActiveScreen.INTERUPTEDSCREEN;
                            this.chooseActionScreen.setVisible(false);
                            this.chooseActionScreen = null;
                            this.interuptScreen = new InteruptScreen();
                            interuptScreen.addKeyListener(k);
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
                        case 'd':
                            this.activeScreen = ActiveScreen.INTERUPTEDSCREEN;
                            this.checkBalanceScreen.setVisible(false);
                            this.checkBalanceScreen = null;
                            this.interuptScreen = new InteruptScreen();
                            interuptScreen.addKeyListener(k);
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
                            this.lastScreen = ActiveScreen.WITHDRAWMONEYSCREEN;
                            this.activeScreen = ActiveScreen.WITHDRAWAMOUNTCONFIRMSCREEN;
                            this.withdrawMoneyScreen.setVisible(false);
                            this.withdrawMoneyScreen = null;
                            this.withdrawAmountConfirmScreen = new WithdrawAmountConfirmScreen(amount);
                            withdrawAmountConfirmScreen.addKeyListener(k);
                            break;
                        case 'c':
                            JsonResponses.SaldoInformatie response = api.saldoOpvragen(transaction.getTransactionId(),
                                    transaction.getIBAN());
                            this.activeScreen = ActiveScreen.CHECK_BALANCE_SCREEN;
                            this.withdrawMoneyScreen.setVisible(false);
                            this.withdrawMoneyScreen = null;
                            this.checkBalanceScreen = new CheckBalanceScreen(response.saldo);
                            checkBalanceScreen.addKeyListener(k);
                            break;
                        case 'd':
                            this.activeScreen = ActiveScreen.INTERUPTEDSCREEN;
                            this.withdrawMoneyScreen.setVisible(false);
                            this.withdrawMoneyScreen = null;
                            this.interuptScreen = new InteruptScreen();
                            interuptScreen.addKeyListener(k);
                            break;
                    }
                } else if(Character.isDigit(key)) {
                    this.withdrawMoneyScreen.newNumber(Character.getNumericValue(key));
                }
                break;
            case BILJETKEUZESCREEN:
                if(Character.isLowerCase(key)) {
                    switch (key) {
                        case 'c':
                        case 'b':
                            biljetkeuzeScreen.addSubstract(key);
                            break;
                        case 'a':
                            int amount = biljetkeuzeScreen.getEnteredAmount();
                            this.lastScreen = ActiveScreen.BILJETKEUZESCREEN;
                            this.activeScreen = ActiveScreen.WITHDRAWAMOUNTCONFIRMSCREEN;
                            this.biljetkeuzeScreen.setVisible(false);
                            this.biljetkeuzeScreen.removeKeyListener(k);
                            this.withdrawAmountConfirmScreen = new WithdrawAmountConfirmScreen(amount);
                            withdrawAmountConfirmScreen.addKeyListener(k);
                            break;
                        case 'd':
                            this.activeScreen = ActiveScreen.INTERUPTEDSCREEN;
                            this.biljetkeuzeScreen.setVisible(false);
                            this.biljetkeuzeScreen = null;
                            this.interuptScreen = new InteruptScreen();
                            interuptScreen.addKeyListener(k);
                            break;
                    }
                } else if(Character.isDigit(key)) {
                    biljetkeuzeScreen.newNumber(Character.getNumericValue(key));
                }
                break;
            case WITHDRAWAMOUNTCONFIRMSCREEN:
                if(Character.isLowerCase(key)) {
                    switch (key) {
                        case 'a':
                            JsonResponse result = api.gewensteOpnameHoeveelheid(transaction.getTransactionId(),
                                    transaction.getIBAN(),
                                    withdrawAmountConfirmScreen.getDesiredAmount(),
                                    transaction.getCARD_UID());
                            if(result.type.equals("OPNAME_IS_MOGELIJK")) {
                                api.geldOpnemen(transaction.getTransactionId(),
                                        transaction.getIBAN(),
                                        withdrawAmountConfirmScreen.getDesiredAmount(),
                                        transaction.getCARD_UID());
                                this.activeScreen = ActiveScreen.ENDSCREEN;
                                this.withdrawAmountConfirmScreen.setVisible(false);
                                this.withdrawAmountConfirmScreen = null;
                                this.biljetkeuzeScreen = null;
                                this.endScreen = new EndScreen();
                                endScreen.addKeyListener(k);
                            } else if(result.type.equals("HOGER_DAN_DAGLIMIET")) {
                                //result = (JsonResponses.OpnameHogerDanDaglimiet) result;
                                withdrawAmountConfirmScreen.hogerDanDaglimiet();
                            } else if(result.type.equals("ONTOEREIKEND_SALDO")) {
                                withdrawAmountConfirmScreen.ontoereikendSaldo();
                            }
                            break;
                        case 'b':
                            this.activeScreen = lastScreen;
                            this.withdrawAmountConfirmScreen.setVisible(false);
                            this.withdrawAmountConfirmScreen = null;
                            if(lastScreen==ActiveScreen.WITHDRAWMONEYSCREEN) {
                                this.withdrawMoneyScreen = new WithdrawMoneyScreen();
                                withdrawMoneyScreen.addKeyListener(k);
                            } else {
                                this.biljetkeuzeScreen.setVisible(true);
                            }
                            break;
                        case 'd':
                            this.activeScreen = ActiveScreen.INTERUPTEDSCREEN;
                            this.withdrawAmountConfirmScreen.setVisible(false);
                            this.withdrawAmountConfirmScreen = null;
                            this.interuptScreen = new InteruptScreen();
                            interuptScreen.addKeyListener(k);
                            break;
                    }
                }
                break;
            case ENDSCREEN:
                this.activeScreen=ActiveScreen.MAINSCREEN;
                this.endScreen.setVisible(false);
                this.endScreen = null;
                this.mainScreen = new MainScreen();
                mainScreen.addKeyListener(k);
                break;
            case INTERUPTEDSCREEN:
                this.activeScreen=ActiveScreen.MAINSCREEN;
                this.interuptScreen.setVisible(false);
                this.interuptScreen = null;
                this.mainScreen = new MainScreen();
                mainScreen.addKeyListener(k);
        }
    }
    private enum ActiveScreen {
        MAINSCREEN, PINSCREEN, CHOOSE_ACTION_SCREEN, CHECK_BALANCE_SCREEN, WITHDRAWMONEYSCREEN, WITHDRAWAMOUNTCONFIRMSCREEN,
        ENDSCREEN, BILJETKEUZESCREEN, INTERUPTEDSCREEN
    }
}
