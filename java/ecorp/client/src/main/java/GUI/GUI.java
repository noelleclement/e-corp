package GUI;


import Backend.*;
import arduinio.Arduino;
import arduinio.ArduinoNew;
import Printer.*;
import java.net.PasswordAuthentication;
import java.text.SimpleDateFormat;
import java.util.Date;

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
    private InteruptScreen interuptScreen;
    private BiljetkeuzeScreen biljetkeuzeScreen;
    private PincodePasBlockedScreen pincodePasBlockedScreen;
    private pasGeblokkeerdScreen pasGeblokkeerdScreen;
    private API api;
    private Transaction transaction;
    private JsonResponse response;
    private ArduinoNew arduino;

    private KeyboardHandler k;
    private PasBestaatNietScreen pasBestaatNietScreen;

    public GUI() {
        this.api = new API();
        this.mainScreen = new MainScreen();
        k = new KeyboardHandler(this);
        try {
            this.arduino = new ArduinoNew();
            arduino.startup(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
        mainScreen.addKeyListener(k);//TODO remove after buttons
        this.activeScreen = ActiveScreen.MAINSCREEN;
    }

    private String rekeningNummer;
    private String pasNummer;

    public void arduinoInput(String inputLine) {
        System.out.println(inputLine.length());
        if(inputLine.length()==11) {
            //Een rekeningnummer
            this.rekeningNummer = inputLine;
            this.mainScreenInput();
        } else if(inputLine.length() >15) {
            this.pasNummer = inputLine;
            this.mainScreenInput();
        } else if(inputLine.length() == 1 ) {
            this.keyPressed(Character.toLowerCase(inputLine.charAt(0)));
        }
    }

    private void mainScreenInput() {
        if(this.activeScreen==ActiveScreen.MAINSCREEN) {
            this.mainScreen.tempLabelAccountNumber.setText(rekeningNummer + "  " + pasNummer);
            if (this.rekeningNummer.length() == 11 && this.pasNummer.length() == 16) {
                JsonResponses.ControleerRekeningnummer response = api.isCorrectCard(rekeningNummer, pasNummer);
                if (response.type.equals("CORRECT_REKENINGNUMMER")) {
                    this.transaction = new Transaction(response.IBAN,
                            response.transaction_id,
                            response.card_uid);
                    this.activeScreen = ActiveScreen.PINSCREEN;
                    this.mainScreen.setVisible(false);
                    this.mainScreen = null;
                    this.pinScreen = new PinScreen();
                    pinScreen.addKeyListener(k);//TODO remove after keypad
                } else if (response.type.equals("INCORRECT_REKENINGNUMMER")) { //TODO maak scherm voor
                    this.activeScreen = ActiveScreen.PASBESTAATNIETSCREEN;
                    this.mainScreen.setVisible(false);
                    this.mainScreen = null;
                    this.pasBestaatNietScreen = new PasBestaatNietScreen();
                    this.pasBestaatNietScreen.addKeyListener(k);
                    try {
                        Thread.sleep(3000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    this.activeScreen = ActiveScreen.MAINSCREEN;
                    this.pasBestaatNietScreen.setVisible(false);
                    this.pasBestaatNietScreen = null;
                    this.mainScreen = new MainScreen();
                    this.mainScreen.addKeyListener(k);
                } else if (response.type.equals("PAS_GEBLOKKEERD")) {
                    this.activeScreen = ActiveScreen.PASGEBLOKKEERDSCHERM;
                    this.mainScreen.setVisible(false);
                    this.mainScreen = null;
                    this.pasGeblokkeerdScreen = new pasGeblokkeerdScreen();
                    this.pasGeblokkeerdScreen.addKeyListener(k);
                    try {
                        Thread.sleep(3000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    this.activeScreen = ActiveScreen.MAINSCREEN;
                    this.pasGeblokkeerdScreen.setVisible(false);
                    this.pasGeblokkeerdScreen = null;
                    this.mainScreen = new MainScreen();
                    this.mainScreen.addKeyListener(k);
                }

                this.rekeningNummer = "";
                this.pasNummer = "";
            }

        }
        else {

            this.rekeningNummer = "";
            this.pasNummer = "";
        }
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
                }else if(Character.isLowerCase(key)) {
                    accountNumber = "";
                    mainScreen.tempLabelAccountNumber.setText("-");
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
                    } else if(response.type.equals("INCORRECT_REKENINGNUMMER")) { //TODO maak scherm voor
                        this.mainScreen.incorrectRekeningnummer();
                    } else if(response.type.equals("PAS_GEBLOKKEERD")) {
                        this.activeScreen = ActiveScreen.PASGEBLOKKEERDSCHERM;
                        this.mainScreen.setVisible(false);
                        this.mainScreen = null;
                        this.pasGeblokkeerdScreen = new pasGeblokkeerdScreen();
                        this.pasGeblokkeerdScreen.addKeyListener(k);

                        this.activeScreen = ActiveScreen.MAINSCREEN;
                        this.pasGeblokkeerdScreen.setVisible(false);
                        this.pasGeblokkeerdScreen = null;
                        this.mainScreen = new MainScreen();
                        this.mainScreen.addKeyListener(k);
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

                            }else if(response.type.equals("MAXIMAAL_AANTAL_POGINGEN")) {
                                this.activeScreen = ActiveScreen.WRONGPINTOOOFTENSCREEN;
                                this.pinScreen.setVisible(false);
                                this.pinScreen = null;
                                this.pincodePasBlockedScreen = new PincodePasBlockedScreen();
                                this.pincodePasBlockedScreen.addKeyListener(k);
                                try {
                                    Thread.sleep(3000);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                this.activeScreen = ActiveScreen.MAINSCREEN;
                                this.pincodePasBlockedScreen.setVisible(false);
                                this.pincodePasBlockedScreen = null;
                                this.mainScreen = new MainScreen();
                                this.mainScreen.addKeyListener(k);
                            }
                            break;
                        case 'd':
                            this.activeScreen = ActiveScreen.INTERUPTEDSCREEN;
                            this.pinScreen.setVisible(false);
                            this.pinScreen = null;
                            this.interuptScreen = new InteruptScreen();
                            interuptScreen.addKeyListener(k);
                            try {
                                Thread.sleep(3000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            this.activeScreen = ActiveScreen.MAINSCREEN;
                            this.interuptScreen.setVisible(false);
                            this.interuptScreen = null;
                            this.mainScreen = new MainScreen();
                            this.mainScreen.addKeyListener(k);
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
                            try {
                                Thread.sleep(3000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            this.activeScreen = ActiveScreen.MAINSCREEN;
                            this.interuptScreen.setVisible(false);
                            this.interuptScreen = null;
                            this.mainScreen = new MainScreen();
                            this.mainScreen.addKeyListener(k);
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
                        case 'b':
                            this.activeScreen = ActiveScreen.BILJETKEUZESCREEN;
                            this.checkBalanceScreen.setVisible(false);
                            this.checkBalanceScreen = null;
                            this.biljetkeuzeScreen = new BiljetkeuzeScreen();
                            biljetkeuzeScreen.addKeyListener(k);
                            break;
                        case 'd':
                            this.activeScreen = ActiveScreen.INTERUPTEDSCREEN;
                            this.checkBalanceScreen.setVisible(false);
                            this.checkBalanceScreen = null;
                            this.interuptScreen = new InteruptScreen();
                            interuptScreen.addKeyListener(k);
                            try {
                                Thread.sleep(3000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            this.activeScreen = ActiveScreen.MAINSCREEN;
                            this.interuptScreen.setVisible(false);
                            this.interuptScreen = null;
                            this.mainScreen = new MainScreen();
                            this.mainScreen.addKeyListener(k);
                            break;
                    }
                }
                break;
            case WRONGPINTOOOFTENSCREEN:
                this.activeScreen=ActiveScreen.MAINSCREEN;
                this.pincodePasBlockedScreen.setVisible(false);
                this.pincodePasBlockedScreen = null;
                this.mainScreen = new MainScreen();
                mainScreen.addKeyListener(k);
                break;
            case WITHDRAWMONEYSCREEN:
                if(Character.isLowerCase(key)) {
                    switch (key) {
                        case 'a':
                            this.withdrawMoneyScreen.backSpace();
                            break;
                        case 'b':
                            int amount = withdrawMoneyScreen.getEnteredAmount();
                            if (amount % 10 == 0) {
                                this.lastScreen = ActiveScreen.WITHDRAWMONEYSCREEN;
                                this.activeScreen = ActiveScreen.WITHDRAWAMOUNTCONFIRMSCREEN;
                                this.withdrawMoneyScreen.setVisible(false);
                                this.withdrawMoneyScreen = null;
                                this.withdrawAmountConfirmScreen = new WithdrawAmountConfirmScreen(amount);
                                withdrawAmountConfirmScreen.addKeyListener(k);
                            } else {
                                withdrawMoneyScreen.mainTextLabel.setText("<html>This can not be withdrawn<br>enter a multiple of 10</html>");
                            }
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
                            try {
                                Thread.sleep(3000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            this.activeScreen = ActiveScreen.MAINSCREEN;
                            this.interuptScreen.setVisible(false);
                            this.interuptScreen = null;
                            this.mainScreen = new MainScreen();
                            this.mainScreen.addKeyListener(k);
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
                            try {
                                Thread.sleep(3000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            this.activeScreen = ActiveScreen.MAINSCREEN;
                            this.interuptScreen.setVisible(false);
                            this.interuptScreen = null;
                            this.mainScreen = new MainScreen();
                            this.mainScreen.addKeyListener(k);
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
                        case 'b':

                            JsonResponse result = api.gewensteOpnameHoeveelheid(transaction.getTransactionId(),
                                    transaction.getIBAN(),
                                    withdrawAmountConfirmScreen.getDesiredAmount(),
                                    transaction.getCARD_UID());
                            if(result.type.equals("OPNAME_IS_MOGELIJK")) {
                                if(lastScreen == ActiveScreen.BILJETKEUZESCREEN) {
                                    Printer printer = new Printer(123, //TODO right transactionID
                                            transaction.getIBAN(),
                                            new SimpleDateFormat("dd-MM-yyyyG HH:mm:s").format(new Date()),
                                            withdrawAmountConfirmScreen.getDesiredAmount(),
                                            biljetkeuzeScreen.biljetten[0],
                                            biljetkeuzeScreen.biljetten[1],
                                            biljetkeuzeScreen.biljetten[2]);
                                    printer.print(false);
                                } else {
                                    int bedrag = withdrawAmountConfirmScreen.getDesiredAmount();
                                    int tien = 0, twintig = 0, vijftig = 0;
                                    while (bedrag>0) {
                                        int tempBedrag = bedrag-50;
                                        if(tempBedrag>=0) {
                                            vijftig++;
                                            bedrag -= 50;
                                        } else {
                                            tempBedrag  = bedrag-20;
                                            if(tempBedrag>=0) {
                                                twintig++;
                                                bedrag -= 20;
                                            } else {
                                                tempBedrag = bedrag - 10;
                                                if (tempBedrag >= 0) {
                                                    tien++;
                                                    bedrag -= 10;
                                                }
                                            }
                                        }
                                    }
                                    Printer printer = new Printer(123, //TODO right transactionID
                                            transaction.getIBAN(),
                                            new SimpleDateFormat("d-MM-yyyyG HH:mm:s").format(new Date()),
                                            withdrawAmountConfirmScreen.getDesiredAmount(),
                                            tien,
                                            twintig,
                                            vijftig);
                                    printer.print(false);
                                }
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
                                try {
                                    Thread.sleep(3000);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                this.activeScreen = ActiveScreen.MAINSCREEN;
                                this.endScreen.setVisible(false);
                                this.endScreen = null;
                                this.mainScreen = new MainScreen();
                                this.mainScreen.addKeyListener(k);
                            } else if(result.type.equals("HOGER_DAN_DAGLIMIET")) {
                                //result = (JsonResponses.OpnameHogerDanDaglimiet) result;
                                withdrawAmountConfirmScreen.hogerDanDaglimiet();
                            } else if(result.type.equals("ONTOEREIKEND_SALDO")) {
                                withdrawAmountConfirmScreen.ontoereikendSaldo();
                            }
                            break;
                        case 'c':
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
                            try {
                                Thread.sleep(3000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            this.activeScreen = ActiveScreen.MAINSCREEN;
                            this.interuptScreen.setVisible(false);
                            this.interuptScreen = null;
                            this.mainScreen = new MainScreen();
                            this.mainScreen.addKeyListener(k);
                            break;
                    }
                }
                break;
            case ENDSCREEN:
                this.activeScreen=ActiveScreen.MAINSCREEN;
                this.endScreen.setVisible(false);
                this.endScreen = null;
                this.mainScreen = new MainScreen();
                break;
            case INTERUPTEDSCREEN:
                this.activeScreen=ActiveScreen.MAINSCREEN;
                this.interuptScreen.setVisible(false);
                this.interuptScreen = null;
                this.mainScreen = new MainScreen();
                mainScreen.addKeyListener(k);
                break;
            case PASGEBLOKKEERDSCHERM:
                this.activeScreen=ActiveScreen.MAINSCREEN;
                this.pasGeblokkeerdScreen.setVisible(false);
                this.pasGeblokkeerdScreen = null;
                this.mainScreen = new MainScreen();
                mainScreen.addKeyListener(k);
                break;
        }
    }
    private enum ActiveScreen {
        MAINSCREEN, PINSCREEN, CHOOSE_ACTION_SCREEN, CHECK_BALANCE_SCREEN, WITHDRAWMONEYSCREEN, WITHDRAWAMOUNTCONFIRMSCREEN,
        ENDSCREEN, BILJETKEUZESCREEN, WRONGPINTOOOFTENSCREEN, PASGEBLOKKEERDSCHERM, PASBESTAATNIETSCREEN, INTERUPTEDSCREEN
    }
}
