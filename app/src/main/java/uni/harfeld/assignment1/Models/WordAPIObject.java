package uni.harfeld.assignment1.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class WordAPIObject {
    @SerializedName("definitions")
    @Expose private List<DefinitionAPIObject> definitions;

    @SerializedName("word")
    @Expose private String word;

    @SerializedName("pronunciation")
    @Expose private String pronunciation;

    public List<DefinitionAPIObject> getDefinitions() {
        return definitions;
    }

    public void setDefinitions(List<DefinitionAPIObject> definitions) {
        this.definitions = definitions;
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

    public void setPronunciation(String pronunciation) {
        this.pronunciation = pronunciation;
    }
}
