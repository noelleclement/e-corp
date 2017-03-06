package GUI;

import javax.swing.*;
import javax.swing.border.MatteBorder;
import java.awt.*;

/**
 * Created by Hans de Rooij on 06/03/2017.
 */
public class PinScreen extends ButtonScreen {
    private PinField pinField = new PinField();
    private String pin = "";
    private int currentNumber = 0;
    public PinScreen() {
        super();
        this.mainTextLabel.setText("Please insert your PIN");

        addPinField();
    }

    public void characterEntered(String button) {
        switch(button) {
            case "a": //Backspace
                if(currentNumber>0) {
                    currentNumber--;
                    pinField.setNumber(currentNumber,false);

                    pin = pin.substring(0, pin.length()-1);
                }
                System.out.println(pin);
                break;
        }
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

    private void addPinField() {
        GridBagConstraints pinFieldConstraints = new GridBagConstraints();
        pinFieldConstraints.gridx = 0;
        pinFieldConstraints.gridy = 2;
        pinFieldConstraints.gridheight = 4;
        pinFieldConstraints.weightx = 1;
        pinFieldConstraints.fill = GridBagConstraints.BOTH;

        rightButtons[0].setIdentifier("A");
        rightButtons[0].setText("Backspace");
        rightButtons[3].setText("EXIT");
        rightButtons[3].setIdentifier("D");

        add(pinField, pinFieldConstraints);
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
}
