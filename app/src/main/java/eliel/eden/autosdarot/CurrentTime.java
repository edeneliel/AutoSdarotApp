package eliel.eden.autosdarot;

import java.io.IOException;

/**
 * Created by Eden on 2/25/2017.
 */
public class CurrentTime extends Thread implements Runnable {
    private double duration;
    private double currentTime;
    private MainActivity mainActivity;
    private boolean running;

    public CurrentTime(final MainActivity mainActivity){
        this.mainActivity = mainActivity;
        try {
            duration = Double.parseDouble(TCPRequest.sendTCPRequest("duration"));
            currentTime = Double.parseDouble(TCPRequest.sendTCPRequest("current-time"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        setCurrentTime(currentTime,duration);
        running = true;
    }

    private String timeDoubleToString(double time){
        int min = (int) (time/60);
        int sec = (int) (time%60);
        return min+":"+sec;
    }

    private void setCurrentTime(final double time, final double durationDouble){
        mainActivity.runOnUiThread(new Runnable() {
            public void run() {
                mainActivity.setTimeText(timeDoubleToString(time) +
                        "/" + timeDoubleToString(durationDouble));
                mainActivity.setSeekBarPosition(time,durationDouble);
            }
        });
    }
    public void stopThread(){
        running = false;
    }
    public void startThread(){
        running = true;
    }

    @Override
    public void run() {
        while (true) {
            while (running) {
                try {
                    currentTime = Double.parseDouble(TCPRequest.sendTCPRequest("current-time"));
                    System.out.println(currentTime);
                    duration = Double.parseDouble(TCPRequest.sendTCPRequest("duration"));
                    setCurrentTime(currentTime, duration);
                    Thread.sleep(1000);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
