package uni.harfeld.assignment1;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

/*
Heavily inspired by:
https://developer.android.com/guide/topics/ui/layout/recyclerview
*/

public class WordCardAdapter extends RecyclerView.Adapter<WordCardAdapter.WordCardViewHolder> {
    private List<Word> data;

    public static class WordCardViewHolder extends RecyclerView.ViewHolder{
        private TextView titelView;
        private TextView pronounciationView;
        private TextView ratingView;

        public WordCardViewHolder(CardView cardView){
            super(cardView);
            this.titelView = cardView.findViewById(R.id.word_titel);
            this.pronounciationView = cardView.findViewById(R.id.word_pronounciation);
            this.ratingView = cardView.findViewById(R.id.word_rating);
        }
    }

    public WordCardAdapter(List<Word> data){
        this.data = data;
    }

    @NonNull
    @Override
    public WordCardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        CardView cardView = (CardView) LayoutInflater.from(parent.getContext()).inflate(R.layout.card_word_item, parent, false);

        WordCardViewHolder viewHolder = new WordCardViewHolder(cardView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull WordCardViewHolder holder, int position) {
        holder.titelView.setText(data.get(position).getWord());
        holder.pronounciationView.setText(data.get(position).getPronounciation());
        holder.ratingView.setText(String.valueOf(data.get(position).getRating()));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }
}
