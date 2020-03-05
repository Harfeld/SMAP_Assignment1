package uni.harfeld.assignment1;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.facebook.stetho.Stetho;

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

        enableStethos();
//        seedDataFromFile();
        data = ((WordApplication) getApplicationContext()).getWordDatabase().WordDao().getAll();
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

    public void seedDataFromFile(){
        InputStreamReader dataFromFile = new InputStreamReader(getResources().openRawResource(R.raw.animal_list));
        BufferedReader fileReader = new BufferedReader(dataFromFile);
        try {
            String dataLine;
            while ((dataLine = fileReader.readLine()) != null){
                String[] wordData = dataLine.replace("\uFEFF","").split(";");
                ((WordApplication) getApplicationContext()).getWordDatabase().WordDao().insertAll(new Word(wordData[0], wordData[1], wordData[2],"",0.0,""));
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onCardClick(String word) {
        Intent detailsIntent = new Intent(ListActivity.this, DetailsActivity.class);
        detailsIntent.putExtra("WORD", word);
        startActivityForResult(detailsIntent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent intentData) {
        super.onActivityResult(requestCode, resultCode, intentData);
        System.out.println("!!__LIST-onActivityResult__!!");
        if (resultCode == RESULT_OK) {
//            Word editedWord = intentData.getParcelableExtra("DATA");
            int index = intentData.getIntExtra("INDEX", 0);
//            data.get(index).setRating(editedWord.getRating());
//            data.get(index).setNote(editedWord.getNote());
            wordCardAdapter.notifyItemChanged(index);
        }
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
//        outState.putParcelableArrayList("DATA", data);
        super.onSaveInstanceState(outState);

    }

    //enable stethos tool for inspecting database on device / emulator through chrome
    private void enableStethos(){

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
