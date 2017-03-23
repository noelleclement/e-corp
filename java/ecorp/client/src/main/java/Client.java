import SSL.SSLClient;

/**
 * Created by Hans de Rooij on 21/03/2017.
 */
public class Client {
    public static void main(String[] args) {
        SSLClient client = new SSLClient("moi");
        client.start();
    }
}
