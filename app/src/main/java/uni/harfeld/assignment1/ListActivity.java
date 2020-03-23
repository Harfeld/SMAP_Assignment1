package uni.harfeld.assignment1;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import uni.harfeld.assignment1.Models.Word;
import uni.harfeld.assignment1.Models.WordAPIObject;

import static uni.harfeld.assignment1.Constants.LA_LOG;
import static uni.harfeld.assignment1.Constants.WL_LOG;
import static uni.harfeld.assignment1.Constants.WORD_API_TOKEN;
import static uni.harfeld.assignment1.Constants.WORD_API_URL;
import static uni.harfeld.assignment1.Constants.WORD_TAG;

/*
Heavily inspired by:
https://developer.android.com/guide/topics/ui/layout/recyclerview
https://www.youtube.com/watch?v=69C1ljfDvl0&fbclid=IwAR058KRAQ9kCmCVYiuci7klZoTyVDAYdbV4dnynhApVIEc47bTAz0JcgPsk
*/

public class ListActivity extends AppCompatActivity implements WordCardAdapter.OnCardClickListener {
    private RecyclerView wordRecyclerView;
    private RecyclerView.Adapter wordCardAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private Button exitButton, secsButton;
    private List<Word> data;

    private WordLearnerService wordLearnerService;

    RequestQueue queue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
//        enableStethos();//TODO TEMPORARY - Can be removed before submition
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        startService(new Intent(ListActivity.this, WordLearnerService.class));
        bindService(new Intent(ListActivity.this, WordLearnerService.class), wordLearnerServiceConnection, BIND_AUTO_CREATE);

        wordRecyclerView = findViewById(R.id.word_recycler_view);
        exitButton = findViewById(R.id.exit_button);
        secsButton = findViewById(R.id.add_button);

        wordRecyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this.getParent());
        wordRecyclerView.setLayoutManager(layoutManager);

        data = new ArrayList<Word>();

//        sendRequest(WORD_API_URL + "lion");

        wordCardAdapter = new WordCardAdapter(data, ListActivity.this);
        wordRecyclerView.setAdapter(wordCardAdapter);

        exitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                unbindService(wordLearnerServiceConnection);
            }
        });

        secsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { //TODO TEMPORARY - Can be turned into add button

            }
        });
    }

    ServiceConnection wordLearnerServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            wordLearnerService = ((WordLearnerService.WordLearnerServiceBinder) service).getService();
            Log.d(LA_LOG, "WordLearner Service Connected");

            data.addAll(wordLearnerService.getAllWords());
            if (data.isEmpty()) {
                data.addAll(seedDatabaseFromFile());
            }
            wordCardAdapter.notifyDataSetChanged();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            wordLearnerService = null;
            Log.d(LA_LOG, "WordLearner Service Disconnected");
        }
    };

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    public void onCardClick(String word) {
        Intent detailsIntent = new Intent(ListActivity.this, DetailsActivity.class);
        detailsIntent.putExtra(WORD_TAG, word);
        startActivityForResult(detailsIntent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent intentData) {
        super.onActivityResult(requestCode, resultCode, intentData);
        if (resultCode == RESULT_OK) {
            data.clear();
            data.addAll(wordLearnerService.getAllWords());
            wordCardAdapter.notifyDataSetChanged();
        }
    }

    private List<Word> seedDatabaseFromFile() {
        InputStreamReader dataFromFile = new InputStreamReader(getResources().openRawResource(R.raw.animal_list));
        BufferedReader fileReader = new BufferedReader(dataFromFile);
        try {
            String dataLine;
            while ((dataLine = fileReader.readLine()) != null){
                String[] wordData = dataLine.replace("\uFEFF","").split(";");
                wordLearnerService.addWord(wordData[0]);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return wordLearnerService.getAllWords();
    }

//    //enable stethos tool for inspecting database on device / emulator through chrome
//    //Directly copied from demo during during persistance lecture.
//    private void enableStethos(){//TODO TEMPORARY - Can be removed before submition
//
//           /* Stetho initialization - allows for debugging features in Chrome browser
//           See http://facebook.github.io/stetho/ for details
//           1) Open chrome://inspect/ in a Chrome browse
//           2) select 'inspect' on your app under the specific device/emulator
//           3) select resources tab
//           4) browse database tables under Web SQL
//         */
//        Stetho.initialize(Stetho.newInitializerBuilder(this)
//                .enableDumpapp(
//                        Stetho.defaultDumperPluginsProvider(this))
//                .enableWebKitInspector(
//                        Stetho.defaultInspectorModulesProvider(this))
//                .build());
//        /* end Stethos */
//    }
//
//    private void sendRequest(String url){
//        if(queue==null){
//            queue = Volley.newRequestQueue(this);
//        }
//
//        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
//                new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String jsonResponse) {
//                        parseJson(jsonResponse);
//                    }
//                }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                Log.e(WL_LOG, "JSON Request failed", error);
//            }
//        }){
//            @Override
//            public Map<String, String> getHeaders() throws AuthFailureError {
//                Map<String, String> params = new HashMap<String, String>();
//                params.put("Content-Type", "application/json; charset=UTF-8");
//                params.put("Authorization", WORD_API_TOKEN);
//                return params;
//            }
//        };
//
//        queue.add(stringRequest);
//    }
//
//    private void parseJson(String json){
//        Gson gson = new GsonBuilder().create();
//        WordAPIObject testWord = gson.fromJson(json, WordAPIObject.class);
//    }
}
