package GUI;

import javax.swing.*;
import java.awt.*;

/**
 * Created by Hans de Rooij on 06/03/2017.
 */
public abstract class ButtonScreen extends DefaultScreen {
    protected JLabel mainTextLabel = new JLabel("Loading...");
    protected JLabel[] rightButtons = new JLabel[4];
    public ButtonScreen() {
        super();
        GridBagConstraints mainTextConstraints = new GridBagConstraints();
        mainTextConstraints.weightx = 0.5;
        mainTextConstraints.weighty = 1;
        mainTextConstraints.gridx = 0;
        mainTextConstraints.gridy = 1;
        mainTextConstraints.gridwidth = 1;
        mainTextConstraints.gridheight = 4;
        mainTextConstraints.anchor = GridBagConstraints.CENTER;
        mainTextConstraints.fill = GridBagConstraints.BOTH;

        mainTextLabel.setFont(new Font("Ariel",Font.PLAIN,70));
        mainTextLabel.setAlignmentX(JLabel.CENTER);
        add(mainTextLabel,mainTextConstraints);
        initRightButtons();
    }

    private void initRightButtons() {
        GridBagConstraints buttonConstraints = new GridBagConstraints();
        buttonConstraints.weightx = 0.4;
        buttonConstraints.weighty = 0.9;
        buttonConstraints.gridx = 1;
        buttonConstraints.gridwidth = 1;
        buttonConstraints.anchor = GridBagConstraints.CENTER;
        buttonConstraints.fill = GridBagConstraints.BOTH;
        for(int i = 0; i<this.rightButtons.length; i++) {
            this.rightButtons[i] = new JLabel("Loading...");
            buttonConstraints.gridy = i+1;
            add(rightButtons[i], buttonConstraints);
        }
    }
}
