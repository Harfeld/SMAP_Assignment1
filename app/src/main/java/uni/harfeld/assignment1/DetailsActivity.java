package uni.harfeld.assignment1;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

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
    Intent initialIntent;

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

        initialIntent = getIntent();
        theWord = initialIntent.getParcelableExtra("DATA");
        title.setText(theWord.getWord());
        pronounce.setText(theWord.getPronounciation());
        description.setText(theWord.getDetails());
        note.setText(theWord.getNote());
        rating.setText(String.valueOf(((theWord.getRating() == 10.0) ? (int)theWord.getRating(): theWord.getRating())));
        image.setImageResource(getResources().getIdentifier(theWord.getWord().toLowerCase(),"drawable", getPackageName()));

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(RESULT_CANCELED, initialIntent);
                finish();
            }
        });

        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent editIntent = new Intent(DetailsActivity.this, EditActivity.class);
                editIntent.putExtra("DATA", initialIntent.getParcelableExtra("DATA"));
                editIntent.putExtra("INDEX", initialIntent.getIntExtra("INDEX", 0));
                startActivityForResult(editIntent, 1);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent intentData) {
        super.onActivityResult(requestCode, resultCode, intentData);
        if (resultCode == RESULT_OK) {
            setResult(RESULT_OK, intentData);
            finish();
        }
    }
}
