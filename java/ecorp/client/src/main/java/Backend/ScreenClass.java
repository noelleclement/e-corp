package Backend;
import GUI.*;

/**
 * Created by Hans de Rooij on 09/05/2017.
 */
public abstract class ScreenClass {
    private GUI gui;
    public ScreenClass(GUI _gui) {
        this.gui = _gui;
    }

    public abstract void keyPressed(char key);
}
