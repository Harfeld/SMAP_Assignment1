package uni.harfeld.assignment1;

import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.SearchView;
import android.widget.Toast;

import com.facebook.stetho.Stetho;

import java.util.ArrayList;
import java.util.List;

import uni.harfeld.assignment1.Models.Word;

import static uni.harfeld.assignment1.Constants.DELETE_BROADCAST_ACTION;
import static uni.harfeld.assignment1.Constants.LA_LOG;
import static uni.harfeld.assignment1.Constants.SEARCH_RESULT_BROADCAST_ACTION;
import static uni.harfeld.assignment1.Constants.SEARCH_RESULT_EXTRA;
import static uni.harfeld.assignment1.Constants.SEARCH_SUCCESS;
import static uni.harfeld.assignment1.Constants.UPDATE_BROADCAST_ACTION;
import static uni.harfeld.assignment1.Constants.WL_LOG;
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
    private Button exitButton, addButton;
    private List<Word> data;
    private SearchView searchBar;

    private WordLearnerService wordLearnerService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        enableStethos();//TODO TEMPORARY - Can be removed before submition
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        startService(new Intent(ListActivity.this, WordLearnerService.class));
        bindService(new Intent(ListActivity.this, WordLearnerService.class), wordLearnerServiceConnection, BIND_AUTO_CREATE);

        wordRecyclerView = findViewById(R.id.word_recycler_view);
        exitButton = findViewById(R.id.exit_button);
        addButton = findViewById(R.id.add_button);
        searchBar = findViewById(R.id.search_bar);

        wordRecyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this.getParent());
        wordRecyclerView.setLayoutManager(layoutManager);

        data = new ArrayList<Word>();

        wordCardAdapter = new WordCardAdapter(data, ListActivity.this);
        wordRecyclerView.setAdapter(wordCardAdapter);

        exitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                unbindService(wordLearnerServiceConnection);
            }
        });

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(LA_LOG, "Searching for word " + searchBar.getQuery().toString());
                wordLearnerService.addWord(searchBar.getQuery().toString());
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
                data.addAll(wordLearnerService.seedDatabaseFromFile());
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
    public void onCardClick(String word) {
        Intent detailsIntent = new Intent(ListActivity.this, DetailsActivity.class);
        detailsIntent.putExtra(WORD_TAG, word);
        startActivityForResult(detailsIntent, 1);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(LA_LOG, "onStart: Registering broadcast Receivers");
        IntentFilter searchResultFilter = new IntentFilter();
        searchResultFilter.addAction(SEARCH_RESULT_BROADCAST_ACTION);
        LocalBroadcastManager.getInstance(this).registerReceiver(onServiceResultBroadcast, searchResultFilter);

        IntentFilter updateFilter = new IntentFilter();
        updateFilter.addAction(UPDATE_BROADCAST_ACTION);
        LocalBroadcastManager.getInstance(this).registerReceiver(onServiceResultBroadcast, updateFilter);

        IntentFilter deleteFilter = new IntentFilter();
        deleteFilter.addAction(DELETE_BROADCAST_ACTION);
        LocalBroadcastManager.getInstance(this).registerReceiver(onServiceResultBroadcast, deleteFilter);
    }

    private BroadcastReceiver onServiceResultBroadcast = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            switch (intent.getAction()){
                case SEARCH_RESULT_BROADCAST_ACTION:
                    if (intent.getStringExtra(SEARCH_RESULT_EXTRA).equals(SEARCH_SUCCESS)){
                        Toast.makeText(ListActivity.this, "Word Added", Toast.LENGTH_SHORT).show();
                        Log.d(LA_LOG, "onReceive: Successful search result broadcast received");

                        data.clear();
                        data.addAll(wordLearnerService.getAllWords());
                        wordCardAdapter.notifyDataSetChanged();
                    } else {
                        Toast.makeText(ListActivity.this, "No Word Found", Toast.LENGTH_SHORT).show();
                        Log.d(LA_LOG, "onReceive: Unsuccessful search result broadcast received");
                    }
                    break;
                case UPDATE_BROADCAST_ACTION:
                    Log.d(LA_LOG, "onReceive: Word updated");

                    data.clear();
                    data.addAll(wordLearnerService.getAllWords());
                    wordCardAdapter.notifyDataSetChanged();
                    break;
                case DELETE_BROADCAST_ACTION:
                    Log.d(LA_LOG, "onReceive: Word deleted");

                    data.clear();
                    data.addAll(wordLearnerService.getAllWords());
                    wordCardAdapter.notifyDataSetChanged();
                    break;
                default:
                    Log.d(WL_LOG, "onReceive: No such action has been accounted for");
                    break;
            }
        }
    };

    //enable stethos tool for inspecting database on device / emulator through chrome
    //Directly copied from demo during during persistance lecture.
    private void enableStethos(){//TODO TEMPORARY - Can be removed before submition

           /* Stetho initialization - allows for debugging features in Chrome browser
           See http://facebook.github.io/stetho/ for details
           1) Open chrome://inspect/ in a Chrome browse
           2) select 'inspect' on your app under the specific device/emulator
           3) select resources tab
           4) browse database tables under Web SQL
         */
        Stetho.initialize(Stetho.newInitializerBuilder(this)
                .enableDumpapp(
                        Stetho.defaultDumperPluginsProvider(this))
                .enableWebKitInspector(
                        Stetho.defaultInspectorModulesProvider(this))
                .build());
        /* end Stethos */
    }
}
