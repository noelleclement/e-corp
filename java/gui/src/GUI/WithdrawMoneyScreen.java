package GUI;

import javax.swing.*;
import java.awt.*;

/**
 * Created by Hans de Rooij on 07/03/2017.
 */
public class WithdrawMoneyScreen extends ButtonScreen {
    private int currentNumber = 0;
    private String amount = "";
    private JLabel amountLabel = new JLabel("");

    public WithdrawMoneyScreen() {
        super();

        rightButtons[0].setIdentifier("A");
        rightButtons[0].setText("Backspace");


    }

    private void addPinField() {
        GridBagConstraints amountLabelConstraints = new GridBagConstraints();
        amountLabelConstraints.gridx = 0;
        amountLabelConstraints.gridy = 2;
        amountLabelConstraints.gridheight = 2;
        amountLabelConstraints.weightx = 1;
        amountLabelConstraints.fill = GridBagConstraints.BOTH;

        add(amountLabel, amountLabelConstraints);
    }

    public void backSpace() {
        if(currentNumber>0) {
            currentNumber--;

            amount = amount.substring(0, amount.length()-1);
            amountLabel.setText(amount);
        }
        System.out.println(amount);
    }

    public void newNumber(int number) {
        this.amount = amount + number;
    }
}
