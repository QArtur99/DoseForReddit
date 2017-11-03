package com.qartf.doseforreddit.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.qartf.doseforreddit.R;
import com.qartf.doseforreddit.data.entity.RulesItem;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SubredditRulesAdapter extends RecyclerView.Adapter<SubredditRulesAdapter.MyViewHolder> {

    private static final String DOT = "\u2022";

    private List<RulesItem> data;
    private Context context;

    public SubredditRulesAdapter(Context context, List<RulesItem> myDataset) {
        this.context = context;
        data = myDataset;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.row_subreddit_rules_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.bind(position);


    }

    public Object getDataAtPosition(int position) {
        return data.get(position);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void clearMovies() {
        this.data.clear();
        notifyDataSetChanged();
    }

    public void setMovies(List<RulesItem> data) {
        this.data.addAll(data);
        notifyDataSetChanged();
    }

    public List<RulesItem> getData() {
        return data;
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.point) TextView point;
        @BindView(R.id.description) TextView description;



        public MyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bind(int position) {
            RulesItem post = (RulesItem) getDataAtPosition(position);
            String pointString = String.valueOf(position + 1) + ". " + post.violationReason;
            point.setText(pointString);
            description.setText(post.description);
        }

    }
}
