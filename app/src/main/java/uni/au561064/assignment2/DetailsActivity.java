package uni.au561064.assignment2;

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

import com.android.volley.RequestQueue;
import com.bumptech.glide.Glide;

import uni.au561064.assignment2.Models.Word;

import static uni.au561064.assignment2.Constants.LA_LOG;
import static uni.au561064.assignment2.Constants.WORD_TAG;

/*
Heavily inspired by:
Networking demos from lecture
*/

public class DetailsActivity extends AppCompatActivity {
    private TextView title, pronounce, description, definition, note, rating;
    private ImageView image;
    private Button cancelButton, editButton, deleteButton;
    private Word theWord;

    private WordLearnerService wordLearnerService;
    private RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        title = findViewById(R.id.details_titel);
        pronounce = findViewById(R.id.details_pronunciation);
        description = findViewById(R.id.details_description);
        definition = findViewById(R.id.details_definition);
        note = findViewById(R.id.details_notes);
        rating = findViewById(R.id.details_rating);
        image = findViewById(R.id.details_photo);
        cancelButton = findViewById(R.id.details_cancel_button);
        editButton = findViewById(R.id.details_edit_button);
        deleteButton = findViewById(R.id.details_delete_button);

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

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                wordLearnerService.deleteWord(theWord);
                unbindService(wordLearnerServiceConnection);
                finish();
            }
        });
    }

    ServiceConnection wordLearnerServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            wordLearnerService = ((WordLearnerService.WordLearnerServiceBinder) service).getService();
            Log.d(LA_LOG, "WordLearner Service Connected");

            theWord = wordLearnerService.getWord(getIntent().getStringExtra(WORD_TAG));
            title.setText(theWord.getWord());
            pronounce.setText(theWord.getPronunciation());
            description.setText(theWord.getDetails());
            definition.setText(theWord.getDefinition());
            note.setText(theWord.getNote());
            rating.setText(((theWord.getRating() == 10.0) ? String.valueOf((int)theWord.getRating()) : String.valueOf(theWord.getRating())));
            Glide.with(image.getContext()).load(theWord.getImageUrl()).placeholder(R.drawable.howboutno).into(image);
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
            unbindService(wordLearnerServiceConnection);
            finish();
        }
    }
}
