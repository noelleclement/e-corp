import Backend.Backend;
import SSL.SSLClient;
import GUI.GUI;

/**
 * Created by Hans de Rooij on 21/03/2017.
 */
public class Client {
    public static void main(String[] args) {
        GUI gui = new GUI(new Backend());
        //System.out.println(new Backend.API().isCorrectCard("123", "asdf").IBAN);
    }
}
