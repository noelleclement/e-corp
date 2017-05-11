package server;

import SSL.*;
import apis.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

//import java.ecorp.server.src.main.java.apis.Database;

/**
 * Created by Hans de Rooij on 21/03/2017.
 */
public class Server {

//    private static Logger logger = LoggerFactory.getLogger(Database.class);
    private static DatabaseInf DB_INSTANCE;


    public static void Server() {
        while (true) {
            SSLServer server = new SSLServer(DB_INSTANCE);
            server.start();
            while (server.isAlive()) {

            }
        }
    }

    public static void main(String[] args) {

        DB_INSTANCE = (args.length == 1 && args[0].equals("--mock-db"))
                ? new MockDB()
                : new Database();

        //logger.info("Database inst is: "+DB_INSTANCE.getClass().toString());
        System.out.println("Db inst:"+DB_INSTANCE.getClass().toString());
        Server();
    }
}
