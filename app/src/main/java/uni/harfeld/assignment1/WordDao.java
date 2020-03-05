package uni.harfeld.assignment1;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import java.util.List;

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

    @Delete
    void delete(Word word);
}
