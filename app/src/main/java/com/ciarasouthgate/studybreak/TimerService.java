package com.ciarasouthgate.studybreak;

import android.app.Notification;
import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.os.CountDownTimer;
import android.util.Log;

import static com.ciarasouthgate.studybreak.App.CHANNEL_2_ID;

public class TimerService extends Service {
    private static final int SIX_HOURS = 6 * 60 * 60000;
    private static final int MILLI_IN_MINUTE = 60000;
    CountDownTimer countdowntimer;
    private StudySession session;
    public static final String DISPLAY_TIMER_BR = "com.ciarasouthgate.studybreak.DisplayTimer";
    Intent bi = new Intent(DISPLAY_TIMER_BR);

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i("TimerService","Start Timer Service");
        countdowntimer = new CountDownTimer(SIX_HOURS, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                Interruption[] tasks = session.getInterruptions();
                for (Interruption task : tasks) {
                    long remainingTime = millisUntilFinished % task.getInterval();
                    String timeString = Long.toString(remainingTime / MILLI_IN_MINUTE) + " ";
                    System.out.println(task.getName());
                    switch (task.getName()) {
                        case ("Water"):
                            System.out.println("water remainder: " + remainingTime);
                            bi.putExtra("time", timeString);
                            bi.putExtra("name", "water");
                            break;
                        case ("Stretch"):
                            System.out.println("stretch remainder: " + remainingTime);
                            bi.putExtra("time", timeString);
                            bi.putExtra("name", "stretch");
                            break;
                        case("Food"):
                            System.out.println("food remainder: " + remainingTime);
                            bi.putExtra("time", timeString);
                            bi.putExtra("name", "food");
                            break;
                        case("Other"):
                            System.out.println("other remainder: " + remainingTime);
                            bi.putExtra("time", timeString);
                            bi.putExtra("name", "other");
                            break;
                        default:
                            break;
                    }
                    sendBroadcast(bi);
                    System.out.println("send");
                }
            }
            @Override
            public void onFinish() {

            }
        };
        countdowntimer.start();
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        Bundle extras = intent.getExtras();

        if (!(extras == null)) {
            this.session = intent.getParcelableExtra("session");
        }
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        countdowntimer.cancel();
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
