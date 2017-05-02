package eliel.eden.autosdarot;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.widget.EditText;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
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
    private static PrintWriter stringOut;
    private static BufferedReader stringIn;

    public static String sendTCPRequest(String message) throws IOException {
        clientSocket = new Socket();

        clientSocket.connect(new InetSocketAddress(IPHandler.getIp() , 1000));

        stringOut = new PrintWriter( clientSocket.getOutputStream(), true);
        stringIn = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        stringOut.println(message);
        String response;
        while ((response = stringIn.readLine()) != null)
        {
            return response;
        }

        return "";
    }
}
