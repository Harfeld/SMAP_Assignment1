package uni.harfeld.assignment1;

import java.io.Serializable;

public class Word implements Serializable {
    private String word;
    private String pronounciation;
    private String details;
    private double rating;
    private String note;

    public Word(String _word, String _pronunciation, String _details) {
        word = _word;
        pronounciation = _pronunciation;
        details = _details;
        rating = 0.0;
        note = "empty";
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public String getPronounciation() {
        return pronounciation;
    }

    public void setPronounciation(String pronounciation) {
        this.pronounciation = pronounciation;
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
}
