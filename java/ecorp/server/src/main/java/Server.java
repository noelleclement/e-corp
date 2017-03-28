import SSL.SSLServer;

/**
 * Created by Hans de Rooij on 21/03/2017.
 */
public class Server {

    private static Logger logger = LoggerFactory.getLogger(Database.class);
    private static DatabaseInf DB_INSTANCE;


    public Server() {
        while (true) {
            SSLServer server = new SSLServer();
            server.start();
            while (server.isAlive()) {

            }
        }
    }

    public static void main(String[] args) {

        DB_INSTANCE = (args.length == 1 && args[0].equals("--mock-db"))
                ? new MockDB()
                : new Database();

        logger.info("Database inst is: "+DB_INSTANCE.getClass().toString());


    }
}
