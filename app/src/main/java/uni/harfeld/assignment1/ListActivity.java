package uni.harfeld.assignment1;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import java.util.ArrayList;

/*
Store dele af setuppet er kraftigt inspirreret af Androids egne guides
*/

public class ListActivity extends AppCompatActivity {
    private RecyclerView wordRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private String[] wordList = {"testWord1", "testWord2", "testWord3", "testWord4", "testWord5","testWord1", "testWord2", "testWord3", "testWord4", "testWord5","testWord1", "testWord2", "testWord3", "testWord4", "testWord5"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        wordRecyclerView = findViewById(R.id.word_recycler_view);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        wordRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        layoutManager = new LinearLayoutManager(this);
        wordRecyclerView.setLayoutManager(layoutManager);

        // specify an adapter (see also next example)
        mAdapter = new MyAdapter(wordList);
        wordRecyclerView.setAdapter(mAdapter);
    }
}
