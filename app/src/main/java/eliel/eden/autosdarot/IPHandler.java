package eliel.eden.autosdarot;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by 2xEsh on 5/2/2017.
 */

public class IPHandler {
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    private static AppCompatActivity activity;
    private static String ip;
    private static File ipFile;

    public static void initResources(AppCompatActivity appActivity) {
        activity = appActivity;

        File path = new File(Environment.getExternalStorageDirectory()+"/Android/data/eliel.eden.autosdarot");
        path.mkdirs();
        ipFile = new File(Environment.getExternalStorageDirectory()+"/Android/data/eliel.eden.autosdarot",
                activity.getResources().getString(R.string.ip_file));

        System.out.println(ipFile.getAbsolutePath());
        verifyStoragePermissions(appActivity);

        if (!ipFile.exists())
            makeIpFileDialog();
        else {
            try {
                readAndSetIP();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static String getIp() {
        return ip;
    }
    public static void makeIpFileDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle("IP file not found, please enter IP : ");

        final EditText input = new EditText(activity);
        builder.setView(input);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                try {
                    ipFile.createNewFile();
                    FileOutputStream fileOutputStream = new FileOutputStream(ipFile, true);
                    fileOutputStream.write(input.getText().toString().getBytes());
                    readAndSetIP();
                    fileOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }
    public static void readAndSetIP() throws IOException {
        InputStreamReader inputStreamReader = new InputStreamReader(new FileInputStream(ipFile));
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
        String line;

        while ((line = bufferedReader.readLine()) != null) {
            ip = line;
        }
        bufferedReader.close();
        inputStreamReader.close();
    }
    public static void verifyStoragePermissions(Activity activity) {
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
    }
}
