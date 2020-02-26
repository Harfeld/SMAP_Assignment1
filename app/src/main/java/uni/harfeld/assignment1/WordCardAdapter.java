package uni.harfeld.assignment1;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
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

    public WordCardAdapter(List<Word> data){
        this.data = data;
    }

    public class WordCardViewHolder extends RecyclerView.ViewHolder{
        private TextView titelView;
        private TextView pronounciationView;

        private TextView ratingView;
        public WordCardViewHolder(CardView cardView) {
            super(cardView);
            this.titelView = cardView.findViewById(R.id.word_titel);
            this.pronounciationView = cardView.findViewById(R.id.word_pronounciation);
            this.ratingView = cardView.findViewById(R.id.word_rating);

            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View card) {
                    Intent detailsIntent = new Intent(card.getContext(), DetailsActivity.class);
                    detailsIntent.putExtra("DATA", data.get(getAdapterPosition()));
                    ((Activity) card.getContext()).startActivityForResult(detailsIntent, 1);
                }
            });
        }
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
