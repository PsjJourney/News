package adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.psj.news.R;

import java.util.ArrayList;

import date.NewsDate;

/**
 * Created by psj on 2016/5/17.
 */
public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
    private ArrayList<NewsDate> newsDates = new ArrayList<>();
    private Context mContext;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case

        public TextView textView,postTime;
        public ViewHolder(View v) {
            super(v);
            textView = (TextView)v.findViewById(R.id.info_text);
            postTime = (TextView)v.findViewById(R.id.post_time);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public MyAdapter(Context context) {
        mContext = context;
    }

    public void setData(ArrayList<NewsDate> myDataset) {
        this.newsDates = myDataset;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public MyAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                   int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.my_text_view, parent, false);
//        TextView textView = (TextView) v.findViewById(R.id.info_text);
        // set the view's size, margins, paddings and layout parameters
//        ViewHolder vh = new ViewHolder(textView);
        return new MyAdapter.ViewHolder(v);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.textView.setText(newsDates.get(position).getTitle());
        holder.postTime.setText(newsDates.get(position).getPostdate());
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return newsDates.size();
    }
}


