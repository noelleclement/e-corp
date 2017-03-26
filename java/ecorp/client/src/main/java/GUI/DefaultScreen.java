package GUI;

import javax.swing.*;
import java.awt.*;

/**
 * Created by Hans de Rooij on 24/02/2017.
 */
public abstract class DefaultScreen extends JFrame {
    public DefaultScreen(){
        super("E Corp -  ATM");
        setSize(1200,1000);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);//TODO make app unexitable after testing
        setResizable(true);//TODO remove after testing
        setLayout(new GridBagLayout());
        GridBagConstraints companyLabelConstraints = new GridBagConstraints();
        companyLabelConstraints.fill = GridBagConstraints.HORIZONTAL;
        companyLabelConstraints.weightx = 0.5;
        companyLabelConstraints.gridx = 0;
        companyLabelConstraints.gridy = 0;
        companyLabelConstraints.gridwidth = 2;
        companyLabelConstraints.anchor = GridBagConstraints.PAGE_START;
        companyLabelConstraints.ipadx = 10;



        JLabel companyName = new JLabel("E Corp Banking",JLabel.CENTER);
        companyName.setBackground(new Color(1,1,1));
        companyName.setForeground(new Color(255, 255, 255));
        companyName.setFont(new Font("Calibri", Font.PLAIN, 85));
        companyName.setOpaque(true);

        add(companyName, companyLabelConstraints);
        setVisible(true);
    }
}
