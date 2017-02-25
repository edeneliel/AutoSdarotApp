package eliel.eden.autosdarot;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * Created by Eden on 2/25/2017.
 */
public class TCPRequest {
    private static Socket clientSocket;
    private static PrintWriter s_out;
    private static BufferedReader s_in;

    public static String sendTCPRequest(String message) throws IOException {
        clientSocket = new Socket();

        clientSocket.connect(new InetSocketAddress("79.182.55.53" , 1000));

        s_out = new PrintWriter( clientSocket.getOutputStream(), true);
        s_in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        s_out.println(message);
        String response;
        while ((response = s_in.readLine()) != null)
        {
            return response;
        }

        return "";
    }
}
