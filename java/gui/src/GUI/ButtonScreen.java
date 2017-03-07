package GUI;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.MatteBorder;
import java.awt.*;

/**
 * Created by Hans de Rooij on 06/03/2017.
 */
public abstract class ButtonScreen extends DefaultScreen {
    protected JLabel mainTextLabel = new JLabel("Loading...", JLabel.CENTER);
    protected RightButton[] rightButtons = new RightButton[4];
    protected JLabel bottomFiller = new JLabel("");
    public ButtonScreen() {
        super();
        GridBagConstraints mainTextConstraints = new GridBagConstraints();
        mainTextConstraints.weightx = 0.9;
        mainTextConstraints.weighty = 0.1;
        mainTextConstraints.gridx = 0;
        mainTextConstraints.gridy = 1;
        mainTextConstraints.gridwidth = 2;
        mainTextConstraints.anchor = GridBagConstraints.CENTER;
        mainTextConstraints.fill = GridBagConstraints.BOTH;

        mainTextLabel.setFont(new Font("Ariel",Font.PLAIN,70));
        mainTextLabel.setAlignmentX(JLabel.CENTER_ALIGNMENT);
        add(mainTextLabel,mainTextConstraints);
        //addTopFiller();
        initRightButtons();

        rightButtons[3].setText("EXIT");
        rightButtons[3].setIdentifier("D");
    }

    private void initRightButtons() {
        GridBagConstraints buttonConstraints = new GridBagConstraints();
        buttonConstraints.weightx = 0.1;
        buttonConstraints.weighty = 0.1;
        buttonConstraints.gridx = 1;
        buttonConstraints.gridwidth = 1;
        buttonConstraints.anchor = GridBagConstraints.LINE_END;
        buttonConstraints.fill = GridBagConstraints.BOTH;
        buttonConstraints.insets = new Insets(10,0,0,0);
        for(int i = 0; i<this.rightButtons.length; i++) {
            rightButtons[i] = new RightButton();
            buttonConstraints.gridy = i+2;
            add(rightButtons[i], buttonConstraints);
        }
    }

    private void addTopFiller() {
        GridBagConstraints buttonConstraints = new GridBagConstraints();
        buttonConstraints.weightx = 0.1;
        buttonConstraints.weighty = 0.9;
        buttonConstraints.gridx = 1;
        buttonConstraints.gridwidth = 1;
        buttonConstraints.anchor = GridBagConstraints.LINE_END;
        buttonConstraints.fill = GridBagConstraints.BOTH;
        buttonConstraints.gridy = 1;
        add(this.bottomFiller, buttonConstraints);
    }

    protected class RightButton extends JPanel {
        private JLabel buttonIdentifier = new JLabel("X");
        private JLabel buttonText = new JLabel("Loading...");
        public RightButton() {
            this("", "");
        }
        public RightButton(String identifier, String text) {
            this.setLayout(new GridBagLayout());
            GridBagConstraints textConstraints = new GridBagConstraints();
            textConstraints.weightx=1;
            textConstraints.fill = GridBagConstraints.HORIZONTAL;
            this.buttonText.setFont(new Font("Ariel",Font.PLAIN,70));
            this.buttonText.setText(text);
            this.buttonText.setHorizontalAlignment(JLabel.RIGHT);
            this.add(buttonText,textConstraints);

            GridBagConstraints identifierConstraints = new GridBagConstraints();
            identifierConstraints.weightx = 0;
            this.buttonIdentifier.setFont(new Font("Ariel",Font.PLAIN,70));
            this.buttonIdentifier.setText(identifier);
            this.buttonIdentifier.setBorder(new MatteBorder(0,1,0,0, Color.BLACK));
            this.add(buttonIdentifier, identifierConstraints);

            this.setBorder(new MatteBorder(1,5,1,0,Color.BLACK));
        }

        public void setIdentifier(String identifier) {
            this.buttonIdentifier.setText(" "+ identifier+" ");
        }

        public void setText(String text){
            this.buttonText.setText(text);
        }
    }
}
