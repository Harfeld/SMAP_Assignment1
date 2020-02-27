package uni.harfeld.assignment1;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

/*
Heavily inspired by:
https://www.youtube.com/watch?v=jO0RkS-Ag3A
*/

public class DetailsActivity extends AppCompatActivity {
    TextView title;
    TextView pronounce;
    TextView description;
    TextView note;
    TextView rating;
    ImageView image;

    Button cancelButton;
    Button editButton;

    Word theWord;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        title = findViewById(R.id.details_titel);
        pronounce = findViewById(R.id.details_pronunciation);
        description = findViewById(R.id.details_description);
        note = findViewById(R.id.details_notes);
        rating = findViewById(R.id.details_rating);
        image = findViewById(R.id.details_photo);
        cancelButton = findViewById(R.id.details_cancel_button);
        editButton = findViewById(R.id.details_edit_button);

        Intent entryIntent = getIntent();
        theWord = (Word) entryIntent.getSerializableExtra("DATA");
        title.setText(theWord.getWord());
        pronounce.setText(theWord.getPronounciation());
        description.setText(theWord.getDetails());
        note.setText(theWord.getNote());
        rating.setText(String.valueOf(theWord.getRating()));
        image.setImageResource(getResources().getIdentifier(theWord.getWord().toLowerCase(),"drawable", getPackageName()));

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent listIntent = new Intent(DetailsActivity.this, ListActivity.class);
                setResult(RESULT_CANCELED, listIntent);
                finish();
            }
        });

        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent editIntent = new Intent(DetailsActivity.this, EditActivity.class);
                editIntent.putExtra("DATA", theWord);
                startActivityForResult(editIntent, 1);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        System.out.println("!!__DETAILS-onActivityResult__!!");

        Word word = (Word) data.getSerializableExtra("DATA");
        title.setText(word.getWord());
        pronounce.setText(word.getPronounciation());
        description.setText(word.getDetails());
        note.setText(word.getNote());
        rating.setText(String.valueOf(word.getRating()));

        System.out.println("!!__Data should be rewritten__!!");
        if (resultCode == RESULT_OK) {
            Intent listIntent = new Intent(DetailsActivity.this, ListActivity.class);
            listIntent.putExtra("DATA", word);
            setResult(RESULT_OK, listIntent);
            finish();
        }
    }
}
