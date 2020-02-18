package uni.harfeld.assignment1;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {
    private String[] data;

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        public TextView textView;

        public MyViewHolder(TextView textView){
            super(textView);
            this.textView = textView;
        }
    }

    public MyAdapter(String[] data){
        this.data = data;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        TextView textView = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_text_view, parent, false);

        MyViewHolder viewHolder = new MyViewHolder(textView);
        return viewHolder;
    }
}
