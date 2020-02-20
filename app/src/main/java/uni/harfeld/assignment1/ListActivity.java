package uni.harfeld.assignment1;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

/*
Heavily inspired by:
https://developer.android.com/guide/topics/ui/layout/recyclerview
*/

public class ListActivity extends AppCompatActivity {
    private RecyclerView wordRecyclerView;
    private RecyclerView.Adapter wordCardAdapter;
    private RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        wordRecyclerView = findViewById(R.id.word_recycler_view);

        wordRecyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(this);
        wordRecyclerView.setLayoutManager(layoutManager);

        wordCardAdapter = new WordCardAdapter(testData(15));
        wordRecyclerView.setAdapter(wordCardAdapter);
    }

    private List<Word> testData(int amount){
        List<Word> wordList = new ArrayList<Word>();
        for (int i = 1; i<=amount; i++ ){
            wordList.add(new Word(("Word" + String.valueOf(i)),("Pronounciation" + String.valueOf(i)),("Details" + String.valueOf(i))));
        }
        return wordList;
    }
}
