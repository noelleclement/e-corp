import Backend.*;
import GUI.*;

/**
 * Created by Hans de Rooij on 21/02/2017.
 */
public class App {
    public static void main(String[] args) {
        System.out.println("hoi");
        Backend backend = new Backend();
        GUI gui = new GUI(backend);
    }
}
