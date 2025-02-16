import java.util.Timer;
import java.util.TimerTask;

public class DecrementingTimer {
    private int seconds;
    private Timer timer;

    public DecrementingTimer(int initialSeconds) {
        this.seconds = initialSeconds;
    }

    public void start() {
        timer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                if (seconds > 0) {
                    seconds--;
                } else {
                    timer.cancel();
                }
            }
        };
        timer.scheduleAtFixedRate(task, 0, 1000);
    }

    public void stop() {
        if (timer != null) {
            timer.cancel();
        }
    }

    public int getTimeLeft(){
        return this.seconds;
    }
}
