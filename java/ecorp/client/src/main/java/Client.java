import SSL.SSLClient;

/**
 * Created by Hans de Rooij on 21/03/2017.
 */
public class Client {
    public static void main(String[] args) {
        System.out.println(new API().isCorrectCard("123", "asdf").IBAN);
    }
}
