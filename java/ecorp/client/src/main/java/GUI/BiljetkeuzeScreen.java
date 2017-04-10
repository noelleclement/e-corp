package GUI;

import javax.swing.*;
import java.awt.*;

/**
 * Created by Hans de Rooij on 07/03/2017.
 */
public class BiljetkeuzeScreen extends ButtonScreen {
    private int currentNumber = 0;
    private int amount = 0;
    private JPanel jpanel = new JPanel();
    private JLabel amountLabel = new JLabel("");
    private JLabel amount10 = new JLabel("1: 10=0<-");
    private JLabel amount20 = new JLabel("2: 20=0");
    private JLabel amount50 = new JLabel("3: 50=0");

    public BiljetkeuzeScreen() {
        super();
        mainTextLabel.setText("<html>Use numeric keys to select biljet<br>use B, C to select amount");
        rightButtons[0].setIdentifier("A");
        rightButtons[0].setText("Confirm");
        rightButtons[1].setIdentifier("B");
        rightButtons[1].setText("Add");
        rightButtons[2].setText("Subtract");
        rightButtons[2].setIdentifier("C");
        addPinField();
    }

    public int[] biljetten = {0,0,0};
    private int activeBiljet = 10;


    private void addPinField() {
        GridBagConstraints panelConstraints = new GridBagConstraints();
        panelConstraints.gridx = 0;
        panelConstraints.gridy = 4;
        panelConstraints.gridheight = 4;
        panelConstraints.weightx = 1;
        panelConstraints.fill = GridBagConstraints.BOTH;
        panelConstraints.anchor = GridBagConstraints.CENTER;
        jpanel.setLayout(new BoxLayout(jpanel, BoxLayout.Y_AXIS));
        amountLabel.setText("0");
        add(this.jpanel, panelConstraints);
        Font labels = new Font("Arial", Font.BOLD, 70);
        amount10.setFont(labels);
        amount20.setFont(labels);
        amount50.setFont(labels);
        amountLabel.setFont(labels);
        jpanel.add(amount50);
        jpanel.add(amount20);
        jpanel.add(amount10);
        jpanel.add(amountLabel);
    }

    public void addSubstract(char key) {
        if(key=='b') {
            switch(activeBiljet) {
                case 10:
                    this.biljetten[0]++;
                    this.amount+=10;
                    break;
                case 20:
                    this.amount+=20;
                    this.biljetten[1]++;
                    break;
                case 50:
                    this.amount+=50;
                    this.biljetten[2]++;
                    break;
            }
        } else if(key=='c') {
            switch(activeBiljet) {
                case 10:
                    if(this.biljetten[0]>0) {
                        this.biljetten[0]--;
                        this.amount-=10;
                    }
                    break;
                case 20:
                    if(this.biljetten[1]>0) {
                        this.biljetten[1]--;
                        this.amount-=20;
                    }
                    break;
                case 50:
                    if(this.biljetten[2]>0) {
                        this.biljetten[2]--;
                        this.amount-=50;
                    }
                    break;
            }
        }
        updateLabels();
    }

    public void newNumber(int number) {
        if(number == 1){
            this.activeBiljet = 10;
        } else if(number == 2) {
            this.activeBiljet = 20;
        } else if(number == 3) {
            this.activeBiljet = 50;
        }
        updateLabels();
    }

    private void updateLabels() {
        this.amountLabel.setText(Integer.toString(amount));
        this.amount10.setText("1: 10="+biljetten[0]+(activeBiljet==10?"<-":""));
        this.amount20.setText("2: 20="+biljetten[1]+(activeBiljet==20?"<-":""));
        this.amount50.setText("3: 50="+biljetten[2]+(activeBiljet==50?"<-":""));
    }

    public int getEnteredAmount() {
        return this.amount;
    }
}
