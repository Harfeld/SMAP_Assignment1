package uni.au561064.assignment2;

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

import java.util.ArrayList;
import java.util.List;

import uni.au561064.assignment2.Models.Word;

import static uni.au561064.assignment2.Constants.DELETE_BROADCAST_ACTION;
import static uni.au561064.assignment2.Constants.LA_LOG;
import static uni.au561064.assignment2.Constants.SEARCH_RESULT_BROADCAST_ACTION;
import static uni.au561064.assignment2.Constants.SEARCH_RESULT_EXTRA;
import static uni.au561064.assignment2.Constants.SEARCH_SUCCESS;
import static uni.au561064.assignment2.Constants.UPDATE_BROADCAST_ACTION;
import static uni.au561064.assignment2.Constants.WL_LOG;
import static uni.au561064.assignment2.Constants.WORD_TAG;

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
                unbindService(wordLearnerServiceConnection);
                unregisterBroadcastReceivers();
                finish();
            }
        });

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(LA_LOG, "Searching for word " + searchBar.getQuery().toString());
                wordLearnerService.addWord(searchBar.getQuery().toString());
            }
        });

        searchBar.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Log.d(LA_LOG, "Searching for word " + searchBar.getQuery().toString());
                wordLearnerService.addWord(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }

    ServiceConnection wordLearnerServiceConnection = new ServiceConnection() {
        /*
            Called when service is bound - Will set the data for recycler view
        */
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

        /*
            called when the service is unbound
        */
        @Override
        public void onServiceDisconnected(ComponentName name) {
            wordLearnerService = null;
            Log.d(LA_LOG, "WordLearner Service Disconnected");
        }
    };

    /*
        When a card in the recyclerview is clicked
    */
    @Override
    public void onCardClick(String word) {
        Intent detailsIntent = new Intent(ListActivity.this, DetailsActivity.class);
        detailsIntent.putExtra(WORD_TAG, word);
        startActivityForResult(detailsIntent, 1);
    }

    @Override
    protected void onStart() {
        super.onStart();
        registerBroadcastReceivers();
    }

    /*
        When the activity receives a broadcast from the service
    */
    private BroadcastReceiver onServiceResultBroadcast = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            switch (intent.getAction()){
                case SEARCH_RESULT_BROADCAST_ACTION:
                    if (intent.getStringExtra(SEARCH_RESULT_EXTRA).equals(SEARCH_SUCCESS)){
                        Toast.makeText(ListActivity.this, "Word Is Added", Toast.LENGTH_SHORT).show();
                        Log.d(LA_LOG, "onReceive: Successful search result broadcast received");

                        data.clear();
                        data.addAll(wordLearnerService.getAllWords());
                        wordCardAdapter.notifyDataSetChanged();
                    } else {
                        Toast.makeText(ListActivity.this, "Word Not Found", Toast.LENGTH_SHORT).show();
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

    /*
        For registering to receive the services broadcasts
    */
    private void registerBroadcastReceivers(){
        Log.d(LA_LOG, "Registering broadcast Receivers");
        IntentFilter resultFilter = new IntentFilter();
        resultFilter.addAction(SEARCH_RESULT_BROADCAST_ACTION);
        resultFilter.addAction(UPDATE_BROADCAST_ACTION);
        resultFilter.addAction(DELETE_BROADCAST_ACTION);
        LocalBroadcastManager.getInstance(this).registerReceiver(onServiceResultBroadcast, resultFilter);
    }

    /*
        For unregistering from receiving the services broadcasts
    */
    private void unregisterBroadcastReceivers(){
        Log.d(LA_LOG, "Unregistering broadcast Receivers");
        LocalBroadcastManager.getInstance(this).unregisterReceiver(onServiceResultBroadcast);
    }
}
