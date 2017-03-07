package GUI;

import Backend.Backend;

import javax.swing.*;
import javax.swing.border.MatteBorder;
import java.awt.*;

/**
 * Created by Hans de Rooij on 06/03/2017.
 */
public class PinScreen extends ButtonScreen {
    private PinField pinField = new PinField();
    private String pin = "";
    private Backend backend;
    private int currentNumber = 0;
    private JLabel errorField = new JLabel();
    public PinScreen(Backend _backend) {
        super();
        this.backend = _backend;
        this.mainTextLabel.setText("Please insert your PIN");

        addPinField();
        addErrorField();

// make right buttons right :)
        rightButtons[0].setIdentifier("A");
        rightButtons[0].setText("Backspace");
    }

    public void numberEntered(int number) {
        if(number<0)
            return;
        if(currentNumber<=3) {
            pin = pin + number;
            pinField.setNumber(currentNumber, true);
            currentNumber++;
        }
        System.out.println(pin);
        if(currentNumber>3) {
            rightButtons[2].setIdentifier("B");
            rightButtons[2].setText("Continue");
        }
    }

    public void wrongPin(int tries) {
        this.errorField.setText("<html>You've entered the <br>wrong PIN <br>"+tries+" times</html>");
    }

    public void backSpace() {
        if(currentNumber>0) {
            currentNumber--;
            pinField.setNumber(currentNumber,false);

            pin = pin.substring(0, pin.length()-1);
        }
        System.out.println(pin);
    }

    private void addPinField() {
        GridBagConstraints pinFieldConstraints = new GridBagConstraints();
        pinFieldConstraints.gridx = 0;
        pinFieldConstraints.gridy = 2;
        pinFieldConstraints.gridheight = 2;
        pinFieldConstraints.weightx = 1;
        pinFieldConstraints.fill = GridBagConstraints.BOTH;

        add(pinField, pinFieldConstraints);
    }

    private void addErrorField() {
        GridBagConstraints errorFieldConstraints = new GridBagConstraints();
        errorFieldConstraints.gridx = 0;
        errorFieldConstraints.gridy = 4;
        errorFieldConstraints.gridheight = 2;
        errorFieldConstraints.weightx = 1;
        errorFieldConstraints.fill = GridBagConstraints.BOTH;

        errorField.setFont(new Font("Ariel",Font.PLAIN,70));

        add(errorField, errorFieldConstraints);
    }

    private class PinField extends JPanel {
        private JLabel[] pinNumbers = new JLabel[4];
        public PinField() {
            this.setLayout(new FlowLayout());
            addPinFields();
        }

        public void setNumber(int number, boolean state) {
            if(state) {
                this.pinNumbers[number].setText("X");
            }
            else {
                this.pinNumbers[number].setText(".");
            }
        }

        private void addPinFields() {
            for(int i=0; i<pinNumbers.length; i++) {
                pinNumbers[i] = new JLabel(".");
                pinNumbers[i].setFont(new Font("Ariel", Font.BOLD, 100));
                pinNumbers[i].setBorder(new MatteBorder(1,1,1,1,Color.BLACK));
                pinNumbers[i].setPreferredSize(new Dimension(100,100));
                pinNumbers[i].setHorizontalAlignment(JLabel.CENTER);
                pinNumbers[i].setVerticalTextPosition(JLabel.CENTER);
                this.add(pinNumbers[i]);
            }
        }
    }

    public String getPin() {
        return pin;
    }
}
