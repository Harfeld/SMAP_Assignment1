package uni.harfeld.assignment1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

public class EditActivity extends AppCompatActivity {
    TextView title;
    TextView note;
    TextView rating;

    SeekBar ratingBar;
    Button cancelButton;
    Button applyButton;

    Word theWord;

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

        Intent entryIntent = getIntent();
        theWord = (Word) entryIntent.getSerializableExtra("DATA");
        title.setText(theWord.getWord());
        note.setText(theWord.getNote());
        rating.setText(String.valueOf(theWord.getRating()));

        ratingBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                rating.setText(String.valueOf((progress/10.0)));
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
                Intent detailsIntent = new Intent(EditActivity.this, DetailsActivity.class);
                setResult(RESULT_CANCELED, detailsIntent);
                finish();
            }
        });

        applyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                theWord.setRating((ratingBar.getProgress()/10.0));
                theWord.setNote(note.getText().toString());
                Intent listIntent = new Intent(EditActivity.this, ListActivity.class);
                listIntent.putExtra("DATA", theWord);
                setResult(RESULT_OK, listIntent);
                finish();
            }
        });
    }
}
