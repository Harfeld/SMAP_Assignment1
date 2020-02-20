package uni.harfeld.assignment1;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

/*
Store dele af setuppet er kraftigt inspirreret af Androids egne guides
*/

public class ListActivity extends AppCompatActivity {
    private RecyclerView wordRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;

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
        mAdapter = new MyAdapter(testData());
        wordRecyclerView.setAdapter(mAdapter);
    }

    private List<Word> testData(){
        List<Word> wordList = new ArrayList<Word>();
        wordList.add(new Word("lol","lollern","mega lollet"));
        wordList.add(new Word("grin", "grinern", "mega grinern"));
        wordList.add(new Word("græd", "grædern", "tudefjæs"));
        wordList.add(new Word("trip", "trippet", "tripper hårdt"));
        wordList.add(new Word("nice", "nicern", "mega nice"));
        return wordList;
    }
}
