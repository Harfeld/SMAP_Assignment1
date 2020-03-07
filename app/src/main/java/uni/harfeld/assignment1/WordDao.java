package uni.harfeld.assignment1;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

/*
Heavily inspired by:
Lecture demo on Room implementation
https://developer.android.com/training/data-storage/room/accessing-data
https://developer.android.com/training/data-storage/room/index.html
https://www.youtube.com/watch?v=Ta4pw2nUUE4
*/

@Dao
public interface WordDao {
    @Query("SELECT * FROM word")
    List<Word> getAll();

    @Query("SELECT * FROM word WHERE word LIKE :word")
    Word findByWord(String word);

    @Insert
    void insertAll(Word... words);

    @Insert
    void insert(Word word);

    @Update
    void update(Word word);

    @Delete
    void delete(Word word);
}
