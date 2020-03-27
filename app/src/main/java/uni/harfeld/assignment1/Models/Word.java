package uni.harfeld.assignment1.Models;

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
    private String imageUrl;

    public Word(@NonNull String word, String pronunciation, String details, String definition, Double rating, String note, String imageUrl) {
        this.word = word;
        this.pronunciation = (pronunciation == null || pronunciation.isEmpty()) ? "No pronunciation found": pronunciation;
        this.details = (details == null || details.isEmpty()) ? "No details found": details;
        this.definition = (definition == null || definition.isEmpty()) ? "No definition found" : definition;
        this.rating = (rating == null) ? 0.0 : rating;
        this.note = (note == null || note.isEmpty()) ? "No notes" : note;
        this.imageUrl = (imageUrl == null || imageUrl.isEmpty()) ? "N/A" : imageUrl;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
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
        this.definition = definition;
    }
}
