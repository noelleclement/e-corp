package GUI;

import javax.swing.*;
import java.awt.*;

/**
 * Created by Hans de Rooij on 27/02/2017.
 */
public abstract class ButtonlessScreen extends DefaultScreen {
    protected JLabel mainTextLabel = new JLabel("Loading...", JLabel.CENTER);
    public ButtonlessScreen() {
        super();
        GridBagConstraints mainTextConstraints = new GridBagConstraints();
        mainTextConstraints.weightx = 0.5;
        mainTextConstraints.weighty = 1;
        mainTextConstraints.gridx = 0;
        mainTextConstraints.gridy = 1;
        mainTextConstraints.gridwidth = 2;
        mainTextConstraints.anchor = GridBagConstraints.CENTER;
        mainTextConstraints.fill = GridBagConstraints.BOTH;

        mainTextLabel.setFont(new Font("Ariel",Font.PLAIN,70));
        mainTextLabel.setAlignmentX(JLabel.CENTER);
        add(mainTextLabel,mainTextConstraints);
    }
}
