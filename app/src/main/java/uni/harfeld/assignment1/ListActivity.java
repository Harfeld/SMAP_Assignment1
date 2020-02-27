package uni.harfeld.assignment1;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/*
Heavily inspired by:
https://developer.android.com/guide/topics/ui/layout/recyclerview
https://www.youtube.com/watch?v=69C1ljfDvl0&fbclid=IwAR058KRAQ9kCmCVYiuci7klZoTyVDAYdbV4dnynhApVIEc47bTAz0JcgPsk
*/

public class ListActivity extends AppCompatActivity implements WordCardAdapter.OnCardClickListener {
    private RecyclerView wordRecyclerView;
    private RecyclerView.Adapter wordCardAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private Button exitButton;

    private ArrayList<Word> data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        wordRecyclerView = findViewById(R.id.word_recycler_view);
        exitButton = findViewById(R.id.exit_button);

        wordRecyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(this.getParent());
        wordRecyclerView.setLayoutManager(layoutManager);

        if (savedInstanceState == null) {
            data = seedDataFromFile();
        } else {
            data = savedInstanceState.getParcelableArrayList("DATA");
        }
        wordCardAdapter = new WordCardAdapter(data, this);
        wordRecyclerView.setAdapter(wordCardAdapter);

        exitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    public ArrayList<Word> seedDataFromFile(){
        ArrayList<Word> seededWordData = new ArrayList<Word>();

        InputStreamReader dataFromFile = new InputStreamReader(getResources().openRawResource(R.raw.animal_list));
        BufferedReader fileReader = new BufferedReader(dataFromFile);
        try {
            String dataLine;
            while ((dataLine = fileReader.readLine()) != null){
                String[] wordData = dataLine.replace("\uFEFF","").split(";");
                seededWordData.add(new Word(wordData[0], wordData[1], wordData[2]));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return seededWordData;
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

    @Override
    public void onCardClick(int position) {
        Intent detailsIntent = new Intent(ListActivity.this, DetailsActivity.class);
        detailsIntent.putExtra("DATA", data.get(position));
        detailsIntent.putExtra("INDEX", position);
        startActivityForResult(detailsIntent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent intentData) {
        super.onActivityResult(requestCode, resultCode, intentData);
        System.out.println("!!__LIST-onActivityResult__!!");
        if (resultCode == RESULT_OK) {
            Word editedWord = intentData.getParcelableExtra("DATA");
            int index = intentData.getIntExtra("INDEX", 0);
            data.get(index).setRating(editedWord.getRating());
            data.get(index).setNote(editedWord.getNote());
            wordCardAdapter.notifyItemChanged(index);
        }
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putParcelableArrayList("DATA", data);
        super.onSaveInstanceState(outState);

    }
}
