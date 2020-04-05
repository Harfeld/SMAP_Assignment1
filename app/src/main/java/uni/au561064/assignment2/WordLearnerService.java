package uni.au561064.assignment2;

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
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import uni.au561064.assignment2.Database.WordApplication;
import uni.au561064.assignment2.Models.DefinitionAPIObject;
import uni.au561064.assignment2.Models.Word;
import uni.au561064.assignment2.Models.WordAPIObject;

import static uni.au561064.assignment2.Constants.DELETE_BROADCAST_ACTION;
import static uni.au561064.assignment2.Constants.SEARCH_FAILURE;
import static uni.au561064.assignment2.Constants.SEARCH_RESULT_BROADCAST_ACTION;
import static uni.au561064.assignment2.Constants.SEARCH_RESULT_EXTRA;
import static uni.au561064.assignment2.Constants.SEARCH_SUCCESS;
import static uni.au561064.assignment2.Constants.UPDATE_BROADCAST_ACTION;
import static uni.au561064.assignment2.Constants.WL_LOG;
import static uni.au561064.assignment2.Constants.WL_REMINDING_NOTIFICATION_ID;
import static uni.au561064.assignment2.Constants.WL_RUNNING_NOTIFICATION_ID;
import static uni.au561064.assignment2.Constants.WORD_API_TOKEN;
import static uni.au561064.assignment2.Constants.WORD_API_URL;

/*
Heavily inspired by:
service demos from lecture
Networking demos from lecture
https://developer.android.com/reference/java/util/concurrent/ExecutorService
https://developer.android.com/reference/java/util/concurrent/Executor
https://androidclarified.com/android-volley-example/
https://www.youtube.com/watch?v=fASThCXLrCc
*/

public class WordLearnerService extends Service {
    private boolean running = false;
    private ExecutorService executorService;
    private RequestQueue requestQueue;

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
    }

    /*
        if the service is not started already:
            Starts the Service as a foreground service which will run in the background and send word-notifications every 60 seconds.
        if service is already running
            Logs that the service is called, but already running.
    */
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

            executorService.submit(new Runnable() {
                @Override
                public void run() {
                    Random random = new Random();
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                        NotificationChannel notificationChannel = new NotificationChannel("wordLearnerReminderChannel", "WordLearnerReminderChannel", NotificationManager.IMPORTANCE_HIGH);
                        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                        notificationManager.createNotificationChannel(notificationChannel);
                    }
                    while (running){
                        try{
                            List<Word> words = getAllWords();
                            Word wordToNotify = words.get(random.nextInt(words.size()));

                            Notification wordLearnerNotification = new NotificationCompat.Builder(WordLearnerService.this, "wordLearnerReminderChannel")
                                    .setContentTitle("Remember to study your favourite words")
                                    .setContentText("Wordsuggestion of the minute: " + wordToNotify.getWord())
                                    .setSmallIcon(R.mipmap.ic_launcher)
                                    .setTicker("Wordsuggestion of the minute: " + wordToNotify.getWord())
                                    .setChannelId("wordLearnerReminderChannel")
                                    .build();
                            startForeground(WL_REMINDING_NOTIFICATION_ID, wordLearnerNotification);
                            Thread.sleep(60000);
                        } catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                }
            });
        } else {
            Log.d(WL_LOG, "WordLearnerService OnStart called - Service Already running");
        }
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    /*
        When an activity binds to the service.
    */
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    public void addWord(final String wordToAdd){
        if (getWord(wordToAdd) != null){
            Log.d(WL_LOG,wordToAdd + " already exists in database");
        } else {
            searchForWordInAPI(wordToAdd);
        }
    }

    /*
        Asynchronously gets all words from the database
    */
    public List<Word> getAllWords() {
        Future<List<Word>> wordsFuture = executorService.submit(new Callable<List<Word>>() {
            @Override
            public List<Word> call() {
                return ((WordApplication) getApplicationContext()).getWordDatabase().WordDao().getAll();
            }
        });
        try {
            return wordsFuture.get();
        } catch (ExecutionException | InterruptedException | CancellationException e) {
            e.printStackTrace();
            return null;
        }
    }

    /*
        Asynchronously gets a word from the database
    */
    public Word getWord(final String wordToGet){
        Future<Word> wordFuture = executorService.submit(new Callable<Word>() {
            @Override
            public Word call() {
                return ((WordApplication) getApplicationContext()).getWordDatabase().WordDao().findByWord(wordToGet);
            }
        });
        try {
            return wordFuture.get();
        } catch (ExecutionException | InterruptedException | CancellationException e) {
            e.printStackTrace();
            return null;
        }
    }

    /*
        Asynchronously updates a word from the database
    */
    public void updateWord(final Word wordToUpdate){
        executorService.submit(new Runnable() {
            @Override
            public void run() {
                ((WordApplication) getApplicationContext()).getWordDatabase().WordDao().update(wordToUpdate);
                broadcastUpdateResult();
            }
        });
    }

    /*
        Asynchronously delete a word in the database
    */
    public void deleteWord(final Word wordToDelete){
        executorService.submit(new Runnable() {
            @Override
            public void run() {
                ((WordApplication) getApplicationContext()).getWordDatabase().WordDao().delete(wordToDelete);
                broadcastDeleteResult();
            }
        });
    }

    /*
        Inserts a list of default words into the database
    */
    public List<Word> seedDatabaseFromFile() {
        List<String> wordsToSeed = getDefaultWordsFromFile();
        for (final String word : wordsToSeed) {
            searchForWordInAPI(word);
        }
        return getAllWords();
    }

    /*
        Broadcasts result of searching a word in the online API
    */
    private void broadcastSearchResult(String searchResult){
        Intent searchResultIntent = new Intent();
        searchResultIntent.setAction(SEARCH_RESULT_BROADCAST_ACTION);
        searchResultIntent.putExtra(SEARCH_RESULT_EXTRA, searchResult);
        Log.d(WL_LOG, "BROADCASTING: " + searchResult);
        LocalBroadcastManager.getInstance(this).sendBroadcast(searchResultIntent);
    }

    /*
        Broadcasts that word in database has been updated
    */
    private void broadcastUpdateResult(){
        Intent updateIntent = new Intent();
        updateIntent.setAction(UPDATE_BROADCAST_ACTION);
        Log.d(WL_LOG, "BROADCASTING: Updated word");
        LocalBroadcastManager.getInstance(this).sendBroadcast(updateIntent);
    }

    /*
        Broadcasts that word in database has been deleted
    */
    private void broadcastDeleteResult(){
        Intent deleteIntent = new Intent();
        deleteIntent.setAction(DELETE_BROADCAST_ACTION);
        Log.d(WL_LOG, "BROADCASTING: Delete word");
        LocalBroadcastManager.getInstance(this).sendBroadcast(deleteIntent);
    }

    private void searchForWordInAPI(String wordToSearch){
        if(requestQueue==null){
            requestQueue = Volley.newRequestQueue(this);
        }
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, WORD_API_URL + wordToSearch, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonResponse) {
                        Log.e(WL_LOG, "JSON Request succeeded");
                        parseJson(jsonResponse);
                        broadcastSearchResult(SEARCH_SUCCESS);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError requestError) {
                Log.e(WL_LOG, "JSON Request failed", requestError);
                broadcastSearchResult(SEARCH_FAILURE);
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Content-Type", "application/json");
                params.put("Authorization", WORD_API_TOKEN);
                return params;
            }
        };
        requestQueue.add(jsonObjectRequest);
    }

    /*
        Converts Json object to WordAPIObject
    */
    private void parseJson(JSONObject json){
        Gson gson = new GsonBuilder().create();
        WordAPIObject wordAPIObject = gson.fromJson(json.toString(), WordAPIObject.class);
        convertAndInsertInDB(wordAPIObject);
    }

    /*
        Converts WordAPIObject to Word and inserts the word into the database.
    */
    private void convertAndInsertInDB(WordAPIObject wordAPIObject) {
        DefinitionAPIObject definitionObject = wordAPIObject.getDefinitions().get(0);
        definitionObject.setDefinition(definitionObject.getDefinition().substring(0,1).toUpperCase() + definitionObject.getDefinition().substring(1));
        String[] definition = definitionObject.getDefinition().split("\\. ");

        final Word theWord = new Word(wordAPIObject.getWord(),
                wordAPIObject.getPronunciation(),
                (definition.length > 1)? definitionObject.getDefinition().replace(definition[0] + ". ",""): (definition[0] + "."),
                (definition[0] + "."),
                null,
                null,
                definitionObject.getImageURL()
        );
        executorService.submit(new Runnable() {
            @Override
            public void run() {
                ((WordApplication) getApplicationContext()).getWordDatabase().WordDao().insertAll(theWord);
            }
        });
    }

    /*
        Returns a list of default words defined for the app
    */
    private List<String> getDefaultWordsFromFile(){
        InputStreamReader dataFromFile = new InputStreamReader(getResources().openRawResource(R.raw.animal_list));
        BufferedReader fileReader = new BufferedReader(dataFromFile);
        List<String> defaultWords = new ArrayList<String>();
        try {
            String dataLine;
            while ((dataLine = fileReader.readLine()) != null){
                defaultWords.add(dataLine.replace("\uFEFF","").split(";")[0]);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return defaultWords;
    }
}