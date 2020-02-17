package uni.harfeld.assignment1;

public class Word {
    private String word;
    private String pronounciation;
    private String details;
    private double rating;
    private String pictureData;

    public void Word(String _word, String _pronunciation, String _details){
        word = _word;
        pronounciation = _pronunciation;
        details = _details;
        rating = 0.0;
    }
}
