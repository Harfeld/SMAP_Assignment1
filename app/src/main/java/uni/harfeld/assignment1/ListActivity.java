package uni.harfeld.assignment1;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/*
Heavily inspired by:
https://developer.android.com/guide/topics/ui/layout/recyclerview
https://www.youtube.com/watch?v=jO0RkS-Ag3A
*/

public class ListActivity extends AppCompatActivity {
    private RecyclerView wordRecyclerView;
    private RecyclerView.Adapter wordCardAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private Button exitButton;

    private List<Word> data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        wordRecyclerView = findViewById(R.id.word_recycler_view);
        exitButton = findViewById(R.id.exit_button);

        wordRecyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(this.getParent());
        wordRecyclerView.setLayoutManager(layoutManager);

        data = testData(15);
        wordCardAdapter = new WordCardAdapter(data);
        wordRecyclerView.setAdapter(wordCardAdapter);

        exitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent intentData) {
//        super.onActivityResult(requestCode, resultCode, intentData);
        System.out.println("!!__LIST-onActivityResult__!!");
//        wordCardAdapter.onActivityResult
//        if (resultCode == RESULT_OK){
//            Word returnedWord = (Word) intentData.getSerializableExtra("DATA");
//            for (Word word:this.data) {
//                if (word.getWord().equals(returnedWord.getWord())){
//                    word = returnedWord;
//                }
//            }
//            wordCardAdapter = new WordCardAdapter(this.data);
//            wordRecyclerView.setAdapter(wordCardAdapter);
//        }
    }

    public void onCLick(View view){

    }

    private List<Word> testData(int amount){
        List<Word> wordList = new ArrayList<Word>();
        for (int i = 1; i<=amount; i++ ){
            Word word = new Word(("Lion"),("Pronounciation" + String.valueOf(i)),("Details" + String.valueOf(i)));
            Random random = new Random();
            word.setRating(Math.round(random.nextDouble()*100)/10.0);
            wordList.add(word);
        }
        return wordList;
    }
}
