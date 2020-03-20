package uni.harfeld.assignment1;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import static uni.harfeld.assignment1.Constants.LA_LOG;
import static uni.harfeld.assignment1.Constants.WORD_TAG;

public class DetailsActivity extends AppCompatActivity {
    private TextView title;
    private TextView pronounce;
    private TextView description;
    private TextView note;
    private TextView rating;
    private ImageView image;
    private Button cancelButton;
    private Button editButton;
    private Word theWord;

    private WordLearnerService wordLearnerService;

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

        bindService(new Intent(this, WordLearnerService.class), wordLearnerServiceConnection, BIND_AUTO_CREATE);

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(RESULT_CANCELED, getIntent());
                unbindService(wordLearnerServiceConnection);
                finish();
            }
        });

        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent editIntent = new Intent(DetailsActivity.this, EditActivity.class);
                editIntent.putExtra(WORD_TAG, theWord.getWord());
                startActivityForResult(editIntent, 1);
            }
        });
    }

    ServiceConnection wordLearnerServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            wordLearnerService = ((WordLearnerService.WordLearnerServiceBinder) service).getService();
            Log.d(LA_LOG, "WordLearner Service Connected");
            Toast.makeText(DetailsActivity.this, "Time running is " + wordLearnerService.getRunTime() + " seconds", Toast.LENGTH_SHORT).show();

            theWord = wordLearnerService.getWord(getIntent().getStringExtra(WORD_TAG));
            title.setText(theWord.getWord());
            pronounce.setText(theWord.getPronunciation());
            description.setText(theWord.getDetails());
            note.setText(theWord.getNote());
            rating.setText(((theWord.getRating() == 10.0) ? String.valueOf((int)theWord.getRating()) : String.valueOf(theWord.getRating())));
            image.setImageResource(getResources().getIdentifier(theWord.getWord().toLowerCase(),"drawable", getPackageName()));
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            wordLearnerService = null;
            Log.d(LA_LOG, "WordLearner Service Disconnected");
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent intentData) {
        super.onActivityResult(requestCode, resultCode, intentData);
        if (resultCode == RESULT_OK) {
            setResult(RESULT_OK, intentData);
            unbindService(wordLearnerServiceConnection);
            finish();
        }
    }
}
