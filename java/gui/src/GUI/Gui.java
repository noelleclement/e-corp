package GUI;


/**
 * Created by Hans de Rooij on 21/02/2017.
 *
 * This class is for handling all the different gui components. It'll
 * also initialize the different handlers
 *
 * basically all the front end peasantery
 */
public class GUI {
    private MainScreen mainScreen;
    private KeyboardHandler k;
    public GUI() {
        k = new KeyboardHandler(this);
        this.mainScreen = new MainScreen();
        mainScreen.addKeyListener(k);
    }

    public void keyPressed(char key) {
        System.out.println(key);
    }
}
