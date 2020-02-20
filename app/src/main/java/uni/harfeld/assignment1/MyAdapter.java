package uni.harfeld.assignment1;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {
    private List<Word> data;

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        private TextView titelView;
        private TextView pronounciationView;
        private TextView ratingView;

        public MyViewHolder(CardView cardView){
            super(cardView);
            this.titelView = cardView.findViewById(R.id.word_titel);
            this.pronounciationView = cardView.findViewById(R.id.word_pronounciation);
            this.ratingView = cardView.findViewById(R.id.word_rating);

        }
    }

    public MyAdapter(List<Word> data){
        this.data = data;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        CardView cardView = (CardView) LayoutInflater.from(parent.getContext()).inflate(R.layout.card_word_item, parent, false);

        MyViewHolder viewHolder = new MyViewHolder(cardView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
//        holder.cardView.setText(data[position]);
        holder.cardView.
    }

    @Override
    public int getItemCount() {
        return data.size();
    }
}
