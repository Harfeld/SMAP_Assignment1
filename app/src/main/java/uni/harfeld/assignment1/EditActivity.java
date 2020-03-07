package uni.harfeld.assignment1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

public class EditActivity extends AppCompatActivity {
    private TextView title;
    private TextView note;
    private TextView rating;
    private SeekBar ratingBar;
    private Button cancelButton;
    private Button applyButton;
    private Word theWord;
    private Intent initialIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        title = findViewById(R.id.edit_titel);
        note = findViewById(R.id.edit_notes_input);
        rating = findViewById(R.id.edit_rating_number);
        ratingBar = findViewById(R.id.rating_bar);
        cancelButton = findViewById(R.id.edit_cancel_button);
        applyButton = findViewById(R.id.edit_apply_button);

        initialIntent = getIntent();
        theWord = loadWordFromDatabase(initialIntent.getStringExtra("WORD"));
        title.setText(theWord.getWord());
        note.setText(theWord.getNote());
        rating.setText(((theWord.getRating() == 10.0) ? String.valueOf((int)theWord.getRating()) : String.valueOf(theWord.getRating())));

        ratingBar.setProgress(((int)(theWord.getRating()*10)));
        ratingBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (progress < 100)
                    rating.setText(String.valueOf((progress/10.0)));
                else
                    rating.setText(String.valueOf((progress/10)));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }


            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(RESULT_CANCELED, initialIntent);
                finish();
            }
        });

        applyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateWordInDatabase();
                setResult(RESULT_OK);
                finish();
            }
        });
    }

    private Word loadWordFromDatabase(String word){
        return ((WordApplication) getApplicationContext()).getWordDatabase().WordDao().findByWord(word);
    }

    private void updateWordInDatabase(){
        theWord.setRating((ratingBar.getProgress()/10.0));
        theWord.setNote(note.getText().toString());
        ((WordApplication) getApplicationContext()).getWordDatabase().WordDao().update(theWord);
    }
}
