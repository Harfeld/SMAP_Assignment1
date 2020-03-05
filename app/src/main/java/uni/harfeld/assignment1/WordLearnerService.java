package uni.harfeld.assignment1;

import android.content.Context;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class WordLearnerService {
    ArrayList<Word> wordList;

    WordLearnerService(Context applicationContext){
        if (dataBaseExists()){
            //TODO: Implement getting all the words from the database.
        }else {
            seedNewDataFromFile(applicationContext);
        }
    }

    private boolean dataBaseExists(){
        return false; //TODO: Implement database and function to check if it is populated.
    }

    private void seedNewDataFromFile(Context applicationContext){
        ArrayList<Word> seededWordData = new ArrayList<Word>();
        InputStreamReader dataFromFile = new InputStreamReader(applicationContext.getResources().openRawResource(R.raw.animal_list));
        BufferedReader fileReader = new BufferedReader(dataFromFile);
        try {
            String dataLine;
            while ((dataLine = fileReader.readLine()) != null){
                String[] wordData = dataLine.replace("\uFEFF","").split(";");
                seededWordData.add(new Word(wordData[0], wordData[1], wordData[2]));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        wordList = seededWordData;
    }

    public ArrayList<Word> getAllWords(){
        return wordList;
    }

    public boolean addWord(Word wordToAdd){
        return wordList.add(wordToAdd);
    }

    public Word getWord(String wordToGet){
        for (Word word: wordList) {
            if (word.getWord().equals(wordToGet)){
                return word;
            }
        }
        return null;
    }

    public boolean deleteWord(Word wordToRemove){
        return wordList.remove(wordToRemove);
    }

    public boolean updateWord(Word wordToUpdate){
        for (Word word: wordList) {
            if (word.getWord().equals(wordToUpdate.getWord())){
                word = wordToUpdate;
                return true;
            }
        }
        return false;
    }
}
