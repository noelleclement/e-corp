package SSL;

import java.io.*;

import javax.net.ssl.*;
import com.sun.net.ssl.*;
import com.sun.net.ssl.internal.ssl.Provider;
import java.security.Security;

public class SSLClient extends Thread{
    private String message = "ping";
    public SSLClient(String message) {
        this.message = message;
    }
    public void run() {
        {
            // Registering the JSSE provider
            Security.addProvider(new Provider());

            //Specifying the Keystore details
            System.setProperty("javax.net.ssl.trustStore","eCorpKey");
            System.setProperty("javax.net.ssl.trustStorePassword","CertificaatWachtwoord");

            // Enable debugging to view the handshake and communication which happens between the SSLClient and the SSLServer
            //System.setProperty("javax.net.debug","all");
        }
        try {

            SSLSocketFactory sslsocketfactory = (SSLSocketFactory) SSLSocketFactory.getDefault();
            SSLSocket sslsocket = (SSLSocket) sslsocketfactory.createSocket("localhost", 4443);

            OutputStream outputstream = sslsocket.getOutputStream();
            OutputStreamWriter outputstreamwriter = new OutputStreamWriter(outputstream);
            BufferedWriter bufferedwriter = new BufferedWriter(outputstreamwriter);

            BufferedReader in = new BufferedReader(
                    new InputStreamReader(
                            sslsocket.getInputStream()));

            String string, inputLine = null;
            while (true) {
                bufferedwriter.write(this.message + '\n');
                bufferedwriter.flush();
                if ((inputLine = in.readLine()) != null) {

                    System.out.println(inputLine);
                    sslsocket.close();
                    break;
                }
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }
}