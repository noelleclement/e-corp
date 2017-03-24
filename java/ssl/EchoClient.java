/*import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import java.io.*;
*/
import java.io.*;

import javax.net.ssl.*;
import com.sun.net.ssl.*;
import com.sun.net.ssl.internal.ssl.Provider;
import java.security.Security;

public
class EchoClient {
    public
            static
    void
            main(String[] arstring) {
                {
            // Registering the JSSE provider
            Security.addProvider(new Provider());

            //Specifying the Keystore details
            System.setProperty("javax.net.ssl.keyStore","mySrvKeystore");
            System.setProperty("javax.net.ssl.keyStorePassword","123456");

            // Enable debugging to view the handshake and communication which happens between the SSLClient and the SSLServer
            System.setProperty("javax.net.debug","all");
        }
        try {

            SSLSocketFactory sslsocketfactory = (SSLSocketFactory) SSLSocketFactory.getDefault();
            SSLSocket sslsocket = (SSLSocket) sslsocketfactory.createSocket("localhost", 4443);

            InputStream inputstream = System.in;
            InputStreamReader inputstreamreader = new InputStreamReader(inputstream);
            BufferedReader bufferedreader = new BufferedReader(inputstreamreader);

            OutputStream outputstream = sslsocket.getOutputStream();
            OutputStreamWriter outputstreamwriter = new OutputStreamWriter(outputstream);
            BufferedWriter bufferedwriter = new BufferedWriter(outputstreamwriter);

             BufferedReader in = new BufferedReader(
                            new InputStreamReader(
                                    sslsocket.getInputStream()));

            String string, inputLine = null;
            //while (true) {
                if ((string = bufferedreader.readLine()) != null) {
                    bufferedwriter.write("HOI" + '\n');
                    bufferedwriter.flush();
                }
                //System.out.println("ho9");
                if ((inputLine = in.readLine()) != null) {

                    System.out.println(inputLine);
                    sslsocket.close();
                    //break;
                }
            //}
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }
}