package GUI;

import javax.swing.*;
import javax.swing.border.MatteBorder;
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
        mainTextLabel.setText("Enter the desired amount:");
        rightButtons[0].setIdentifier("A");
        rightButtons[0].setText("Backspace");
        rightButtons[1].setIdentifier("B");
        rightButtons[1].setText("Confirm");
        addPinField();
    }

    private void addPinField() {
        GridBagConstraints amountLabelConstraints = new GridBagConstraints();
        amountLabelConstraints.gridx = 0;
        amountLabelConstraints.gridy = 2;
        amountLabelConstraints.gridheight = 2;
        amountLabelConstraints.weightx = 1;
        amountLabelConstraints.fill = GridBagConstraints.HORIZONTAL;

        amountLabel.setBorder(new MatteBorder(1,1,1,1,Color.BLACK));
        amountLabel.setFont(new Font("Ariel", Font.PLAIN, 60));
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
        currentNumber++;
        amountLabel.setText(amount);
    }

    public int getEnteredAmount() {
        return Integer.parseInt(this.amount);
    }
}
