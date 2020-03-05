package uni.harfeld.assignment1;

import android.os.Parcel;
import android.os.Parcelable;

/*
Heavily inspired by:
https://www.youtube.com/watch?v=WBbsvqSu0is&t=263s
*/

public class Word implements Parcelable {
    private String word;
    private String pronounciation;
    private String details;
    private String definition;
    private double rating;
    private String note;

    public Word(String _word, String _pronunciation, String _details) {
        word = _word;
        pronounciation = _pronunciation;
        details = _details;
        definition = _word + "-dummy-definition";
        rating = 0.0;
        note = _word + "-dummy-note";
    }

    protected Word(Parcel in) {
        word = in.readString();
        pronounciation = in.readString();
        details = in.readString();
        rating = in.readDouble();
        note = in.readString();
    }

    public static final Creator<Word> CREATOR = new Creator<Word>() {
        @Override
        public Word createFromParcel(Parcel in) {
            return new Word(in);
        }

        @Override
        public Word[] newArray(int size) {
            return new Word[size];
        }
    };

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

    public String getDefinition() {
        return definition;
    }

    public void setDefinition(String definition) {
        definition = definition;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(word);
        dest.writeString(pronounciation);
        dest.writeString(details);
        dest.writeDouble(rating);
        dest.writeString(note);
    }
}
