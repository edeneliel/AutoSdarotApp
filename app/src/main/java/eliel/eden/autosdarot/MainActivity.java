package eliel.eden.autosdarot;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.StrictMode;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    private ImageButton playButton;
    private ImageButton powerButton;
    private ImageButton nextButton;
    private ImageButton prevButton;
    private TextView videoTime;
    private boolean played;
    private CurrentTime currentTime;
    private SeekBar seekBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        videoTime = (TextView) findViewById(R.id.videoTime);
        seekBar = (SeekBar) findViewById(R.id.progressBar);
        currentTime = new CurrentTime(this);
        setSeekBar();
        setPausePlay();
        setPowerButton();
        setPrevButton();
        setNextButton();

        currentTime.start();
    }

    public void setTimeText(String text){
        videoTime.setText(text);
    }
    public void setSeekBarPosition(double currentTime, double duration){
        seekBar.setProgress((int) (currentTime*100/duration));
    }

    private void setSeekBar(){
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                currentTime.stopThread();
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                try {
                    TCPRequest.sendTCPRequest("progress: "+seekBar.getProgress());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                currentTime.startThread();
            }
        });
    }
    private void setPausePlay(){
        played = false;
        playButton = (ImageButton) findViewById(R.id.play_button);
        if (played)
            playButton.setBackgroundResource(R.drawable.pause_button);
        else
            playButton.setBackgroundResource(R.drawable.play_button);
        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (played) {
                        playButton.setBackgroundResource(R.drawable.play_button);
                        TCPRequest.sendTCPRequest("pause");
                        played = false;
                    }
                    else {
                        playButton.setBackgroundResource(R.drawable.pause_button);
                        TCPRequest.sendTCPRequest("play");
                        played = true;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }
    private void setPowerButton(){
        powerButton = (ImageButton) findViewById(R.id.power_button);

        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(which == DialogInterface.BUTTON_POSITIVE) {
                    try {
                        TCPRequest.sendTCPRequest("shutdown-pc");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Confirmation")
                .setMessage("Are you sure you want to shutdown PC")
                .setPositiveButton("Yes", dialogClickListener)
                .setNegativeButton("No", dialogClickListener);
        final Dialog dialog = builder.create();

        powerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.show();
            }
        });
    }
    private void setNextButton(){
        nextButton = (ImageButton) findViewById(R.id.next_button);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    TCPRequest.sendTCPRequest("next");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }
    private void setPrevButton(){
        prevButton = (ImageButton) findViewById(R.id.previous_button);
        prevButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    TCPRequest.sendTCPRequest("previous");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
