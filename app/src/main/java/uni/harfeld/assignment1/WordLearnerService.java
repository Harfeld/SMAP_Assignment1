package uni.harfeld.assignment1;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static uni.harfeld.assignment1.Constants.WL_LOG;
import static uni.harfeld.assignment1.Constants.WL_RUNNING_NOTIFICATION_ID;

/*
Heavily inspired by:
service demos
https://developer.android.com/reference/java/util/concurrent/ExecutorService
https://developer.android.com/reference/java/util/concurrent/Executor
*/

public class WordLearnerService extends Service {
    private boolean running = false;
    ExecutorService executorService;

    private int secsRunning;

    public class WordLearnerServiceBinder extends Binder {
        WordLearnerService getService(){
            return WordLearnerService.this;
        }
    }

    private final IBinder binder = new WordLearnerServiceBinder();

    public WordLearnerService(){
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(WL_LOG, "WordLearnerService OnCreate");
        executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

        secsRunning = 0;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (!running && intent != null) {
            running = true;
            Log.d(WL_LOG, "WordLearnerService OnStart called");
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                NotificationChannel notificationChannel = new NotificationChannel("wordLearnerChannel", "WordLearnerChannel", NotificationManager.IMPORTANCE_LOW);
                NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                notificationManager.createNotificationChannel(notificationChannel);
            }
            Notification wordLearnerNotification = new NotificationCompat.Builder(this, "wordLearnerChannel")
                    .setContentTitle("wordLearner Service")
                    .setContentText("wordLearner Service is Running")
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setTicker("wordLearner Service is Running")
                    .setChannelId("wordLearnerChannel")
                    .build();
            startForeground(WL_RUNNING_NOTIFICATION_ID, wordLearnerNotification);
        } else {
            Log.d(WL_LOG, "WordLearnerService OnStart called - Service Already running");
        }

        Runnable r = new Runnable() {
            @Override
            public void run() {
                while(running) {
                    secsRunning++;
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        executorService.submit(r);

        return START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }


    //Business logic
    public void addWord(String wordToAdd){
        Word newWord = new Word(wordToAdd, null, null, null, null, null);
        ((WordApplication) getApplicationContext()).getWordDatabase().WordDao().insertAll(newWord);
    }

    public List<Word> getAllWords(){
        return ((WordApplication) getApplicationContext()).getWordDatabase().WordDao().getAll();
    }

    public Word getWord(String wordToGet){
        return ((WordApplication) getApplicationContext()).getWordDatabase().WordDao().findByWord(wordToGet);
    }

    public void updateWord(Word wordToUpdate){

    }

    public void deleteWord(Word wordToDelete){

    }

    public int getRunTime(){
        return  secsRunning;
    }
}
