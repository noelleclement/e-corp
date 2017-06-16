package SSL;


import java.io.*;
import java.security.Security;
import java.security.PrivilegedActionException;


import javax.net.ssl.*;

import apis.API;
import apis.DatabaseInf;
import apis.LandApi;
import com.sun.net.ssl.*;
import com.sun.net.ssl.internal.ssl.Provider;

public class SSLServer extends Thread {

    private static DatabaseInf database;
    public SSLServer(DatabaseInf database) {
        super();
        this.database = database;
    }
    public void run(){

        int intSSLport = 4443; // Port where the SSL server.Server needs to listen for new requests from the client

        {
            // Registering the JSSE provider
            Security.addProvider(new Provider());

            //Specifying the Keystore details
            System.setProperty("javax.net.ssl.keyStore","eCorpKey");
            System.setProperty("javax.net.ssl.keyStorePassword","CertificaatWachtwoord");
            //System.out.println("We luisteren op de poort "+intSSLport);
            // Enable debugging to view the handshake and communication which happens between the SSLClient and the SSLServer
            //System.setProperty("javax.net.debug","all");
        }

        try {
            // Initialize the server.Server Socket
            SSLServerSocketFactory sslServerSocketfactory = (SSLServerSocketFactory)SSLServerSocketFactory.getDefault();
            SSLServerSocket sslServerSocket = (SSLServerSocket)sslServerSocketfactory.createServerSocket(intSSLport);
            SSLSocket sslSocket = (SSLSocket)sslServerSocket.accept();

            LandApi api = new LandApi(database);

            // Create Input / Output Streams for communication with the client
            while(true)
            {
                PrintWriter out = new PrintWriter(sslSocket.getOutputStream(), true);
                BufferedReader in = new BufferedReader(
                        new InputStreamReader(
                                sslSocket.getInputStream()));
                String inputLine, outputLine;

                while ((inputLine = in.readLine()) != null) {
                    //out.write("You said: "+inputLine+"\n");
                    //out.write(inputLine+"\n");
                    System.out.println("Binnengekomen:\n"+inputLine);
                    out.write(api.parse(inputLine)+"\n");
                    //out.write("dit is een reactie"+"\n");
                    out.flush();
                    //System.out.println(inputLine);
                }

                // Close the streams and the socket
                out.close();
                in.close();
                sslSocket.close();
                sslServerSocket.close();

            }
        }


        catch(Exception exp)
        {
            /*PrivilegedActionException priexp = new PrivilegedActionException(exp);
            System.out.println(" Priv exp --- " + priexp.getMessage());

            System.out.println(" Exception occurred .... " +exp);
            exp.printStackTrace();*/
            return;
        }

    }

}