package uni.harfeld.assignment1;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

/*
Heavily inspired by:
https://www.youtube.com/watch?v=WBbsvqSu0is&t=263s
*/

@Entity
public class Word {
    @PrimaryKey
    @NonNull String word;

    private String pronunciation;
    private String details;
    private String definition;
    private double rating;
    private String note;

    public Word(@NonNull String word, String pronunciation, String details, String definition, Double rating, String note) {
        this.word = word;
        this.pronunciation = pronunciation;
        this.details = details;
        this.definition = word + "-dummy-definition";
        this.rating = 0.0;
        this.note = word + "-dummy-note";
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public String getPronunciation() {
        return pronunciation;
    }

    public void setPronunciation(String pronounciation) {
        this.pronunciation = pronounciation;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getDefinition() {
        return definition;
    }

    public void setDefinition(String definition) {
        definition = definition;
    }
}
