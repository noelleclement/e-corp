import SSL.SSLServer;

/**
 * Created by Hans de Rooij on 21/03/2017.
 */
public class Server {
    public static void main(String[] args) {
        while (true) {
            SSLServer server = new SSLServer();
            server.start();
            while(server.isAlive()) {

            }
        }
    }
}
