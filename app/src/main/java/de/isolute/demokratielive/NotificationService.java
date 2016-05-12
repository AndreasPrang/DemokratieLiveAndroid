package de.isolute.demokratielive;

import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.v7.app.NotificationCompat;

/**
 * Created by Nico on 11.05.2016.
 */
public class NotificationService extends Service {

    private final int NOTIFICATION_ID = 435760283;
    private final int CHECK_STREAM_RUNNING_INTERVAL = 3000;
    private static boolean running;
    private boolean sentNotificationForThisStream;

    @Override
    public void onCreate() {
        System.out.println("starting service");
        running = true;

        new Thread(new Runnable() {
            @Override
            public void run() {

                while (isRunning()) {

                    if (!sentNotificationForThisStream && streamRunning())
                        buildNotification();

                    System.out.println("service running");

                    try {
                        Thread.sleep(CHECK_STREAM_RUNNING_INTERVAL);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

            }
        }).start();
    }

    private boolean streamRunning() {


        return true;
    }

    private void buildNotification() {

        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext());
        builder.setContentText("Test content text").setContentTitle("Test content title").setSmallIcon(R.mipmap.ic_launcher);

        notificationManager.notify(NOTIFICATION_ID, builder.build());

        System.out.println("sent notification");

        sentNotificationForThisStream = true;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        running = false;
        System.out.println("service destroyed");
    }

    public static boolean isRunning() {
        return running;
    }

    public static void stop() {
        running = false;
    }

}
