package uni.harfeld.assignment1;

import android.app.Application;
import androidx.room.Room;

/*
Heavily inspired by:
Lecture demo on Room implementation
https://developer.android.com/training/data-storage/room/index.html
https://www.youtube.com/watch?v=Ta4pw2nUUE4
*/

public class WordApplication extends Application {
    private WordDatabase wordDatabase;

    public WordDatabase getWordDatabase(){
        if (wordDatabase == null){
            wordDatabase = Room.databaseBuilder(this, WordDatabase.class, "my_words").build();
        }
        return wordDatabase;
    }
}
