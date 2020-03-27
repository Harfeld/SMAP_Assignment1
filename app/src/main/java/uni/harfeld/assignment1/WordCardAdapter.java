package uni.harfeld.assignment1;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

import uni.harfeld.assignment1.Models.Word;

/*
Heavily inspired by:
Networking demos from lecture
https://developer.android.com/guide/topics/ui/layout/recyclerview
https://stackoverflow.com/questions/43010526/get-android-resource-id-from-string-in-an-adapter
https://www.youtube.com/watch?v=69C1ljfDvl0&fbclid=IwAR058KRAQ9kCmCVYiuci7klZoTyVDAYdbV4dnynhApVIEc47bTAz0JcgPsk
*/

public class WordCardAdapter extends RecyclerView.Adapter<WordCardAdapter.WordCardViewHolder> {
    private List<Word> data;
    private OnCardClickListener onCardClickListener;
    private Context context;

    public WordCardAdapter(List<Word> data, OnCardClickListener onCardClickListener){
        this.data = data;
        this.onCardClickListener = onCardClickListener;
    }

    public class WordCardViewHolder extends RecyclerView.ViewHolder{
        private TextView titelView;
        private TextView pronounciationView;
        private TextView ratingView;
        private ImageView wordPicture;
        private OnCardClickListener onCardClickListener;

        public WordCardViewHolder(CardView cardView, final OnCardClickListener onCardClickListener) {
            super(cardView);
            this.titelView = cardView.findViewById(R.id.word_titel);
            this.pronounciationView = cardView.findViewById(R.id.word_pronounciation);
            this.ratingView = cardView.findViewById(R.id.word_rating);
            this.wordPicture = cardView.findViewById(R.id.word_card_photo);
            this.onCardClickListener = onCardClickListener;
            context = cardView.getContext();

            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View card) {
                    onCardClickListener.onCardClick(data.get(getAdapterPosition()).getWord());
                }
            });
        }
    }

    @NonNull
    @Override
    public WordCardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        CardView cardView = (CardView) LayoutInflater.from(parent.getContext()).inflate(R.layout.card_word_item, parent, false);

        WordCardViewHolder viewHolder = new WordCardViewHolder(cardView, onCardClickListener);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull WordCardViewHolder holder, int position) {
        holder.titelView.setText(data.get(position).getWord());
        holder.pronounciationView.setText(data.get(position).getPronunciation());
        holder.ratingView.setText(((data.get(position).getRating() == 10.0) ? String.valueOf((int)data.get(position).getRating()) : String.valueOf(data.get(position).getRating())));
        Glide.with(holder.wordPicture.getContext()).load(data.get(position).getImageUrl()).placeholder(R.drawable.howboutno).into(holder.wordPicture);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public interface OnCardClickListener{
        void onCardClick(String word);
    }
}

