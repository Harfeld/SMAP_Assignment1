package uni.harfeld.assignment1.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DefinitionAPIObject {
    @SerializedName("type")
    @Expose private String wordType;

    @SerializedName("definition")
    @Expose private String definition;

    @SerializedName("example")
    @Expose private String example;

    @SerializedName("image_url")
    @Expose private String imageURL;

    @SerializedName("emoji")
    @Expose private String emoji;

    public String getWordType() {
        return wordType;
    }

    public void setWordType(String wordType) {
        this.wordType = wordType;
    }

    public String getDefinition() {
        return definition;
    }

    public void setDefinition(String definition) {
        this.definition = definition;
    }

    public String getExample() {
        return example;
    }

    public void setExample(String ecample) {
        this.example = ecample;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getEmoji() {
        return emoji;
    }

    public void setEmoji(String emoji) {
        this.emoji = emoji;
    }
}
