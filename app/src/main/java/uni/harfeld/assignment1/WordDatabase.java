package uni.harfeld.assignment1;

import androidx.room.Database;
import androidx.room.RoomDatabase;

/*
Heavily inspired by:
Lecture demo on Room implementation
https://developer.android.com/training/data-storage/room/index.html
https://www.youtube.com/watch?v=Ta4pw2nUUE4
*/

@Database(entities = {Word.class}, version = 1)
public abstract class WordDatabase extends RoomDatabase {
    public abstract WordDao WordDao();
}
