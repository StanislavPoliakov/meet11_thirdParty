package home.stanislavpoliakov.meet11_practice;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;
import java.util.zip.Inflater;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {
    private static final String TAG = "meet11_logs";
    private List<Entry> oldData, newData;

    public MyAdapter(List<Entry> data) {
        this.newData = data;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_holder, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        /*Log.d(TAG, "onBindViewHolder: title = " + newData.get(position).getTitle());
        Log.d(TAG, "onBindViewHolder: body = " + newData.get(position).getText());
        Log.d(TAG, "onBindViewHolder: timestamp = " + newData.get(position).getId());*/

        //StringBuffer titleBuffer = new StringBuffer(newData.get(position).getTitle());
        holder.title.setText(newData.get(position).getTitle());
        holder.body.setText(newData.get(position).getText());
        holder.timestamp.setText(newData.get(position).getTimeStamp());
    }

    @Override
    public int getItemCount() {
        return newData.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title, body, timestamp;

        public MyViewHolder(View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.title);
            body = itemView.findViewById(R.id.body);
            timestamp = itemView.findViewById(R.id.timestamp);
        }
    }
}
