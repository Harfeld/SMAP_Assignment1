package uni.au561064.assignment2.Database;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import uni.au561064.assignment2.Models.Word;

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
