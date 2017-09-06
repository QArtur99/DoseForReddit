package com.qartf.doseforreddit.adapter;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.qartf.doseforreddit.R;
import com.qartf.doseforreddit.activity.CommentsActivity;
import com.qartf.doseforreddit.model.Comment;

import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CommentsAdapter extends RecyclerView.Adapter<CommentsAdapter.MyViewHolder> {

    public static final String DOT = "\u2022";
    public static final String KILO = "K";
    final private ListItemClickListener mOnClickListener;
    private DecimalFormat decimalFormat = new DecimalFormat("##.#");

    private List<Comment> data;
    private LinearLayout previousView;
    private boolean isOpen = false;
    private Context context;


    public CommentsAdapter(Context context, List<Comment> myDataset, ListItemClickListener listener) {
        this.context = context;
        data = myDataset;
        mOnClickListener = listener;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.row_comment_item, parent, false);
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

    public void setMovies(List<Comment> data) {
        this.data.addAll(data);
        notifyDataSetChanged();
    }

    public List<Comment> getData() {
        return data;
    }

    public interface ListItemClickListener {
        void onListItemClick(int clickedItemIndex, View view);
        void onCommentSelected(int clickedItemIndex, View expandableView, View parent);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.author) TextView author;
        @BindView(R.id.score) TextView score;
        @BindView(R.id.time) TextView time;
        @BindView(R.id.body) TextView body;
        @BindView(R.id.recyclerView) RecyclerView recyclerView;
        @BindView(R.id.childrenCommentFrame) LinearLayout childrenCommentFrame;
        @BindView(R.id.expandArea) LinearLayout expandArea;
        @BindView(R.id.commentItemFrame) LinearLayout commentFrame;

        public MyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            commentFrame.setOnClickListener(this);
        }

        public void bind(int position) {
            Comment post = (Comment) getDataAtPosition(position);
            loadData(post);
        }

        private void loadData(Comment post) {
            String authorString = post.author + DOT;
            author.setText(authorString);
            upsFormat(post);
            timeFormat(post);
            body.setText(post.body);
            if(post.commentList != null){
                childrenCommentFrame.setVisibility(View.VISIBLE);
                LinearLayoutManager layoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
                layoutManager.setAutoMeasureEnabled(true);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setHasFixedSize(true);
                CommentsAdapter postsAdapter = new CommentsAdapter(context, post.commentList, (CommentsActivity) context);
                recyclerView.setAdapter(postsAdapter);
            }



        }


        private void upsFormat(Comment post) {
            int upsInteger = Integer.valueOf(post.score);
            if (upsInteger >= 10000) {
                double upsDouble = upsInteger / 1000d;
                String upsString = decimalFormat.format(upsDouble) + KILO + " points";
                score.setText(upsString);
            } else {
                String points = String.valueOf(upsInteger) + " points";
                score.setText(points);
            }
        }

        private void timeFormat(Comment post) {
            Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
            long currentTime = calendar.getTimeInMillis() / 1000L;
            double timeDouble = Double.valueOf(post.createdUtc);
            long trueTime = currentTime - ((long) timeDouble);
            if (trueTime >= 31536000) {
                int date = (int) trueTime / 31536000;
                String dateString = DOT + String.valueOf(date);
                dateString += (date == 1) ? "yr" : "yrs";
                time.setText(dateString);
            } else if (trueTime >= 2592000) {
                int date = (int) trueTime / 2592000;
                String dateString = DOT + String.valueOf(date) + "m";
                time.setText(dateString);
            } else if (trueTime >= 604800) {
                int date = (int) trueTime / 604800;
                String dateString = DOT + String.valueOf(date) + "wk";
                time.setText(dateString);
            } else if (trueTime >= 86400) {
                int date = (int) trueTime / 86400;
                String dateString = DOT + String.valueOf(date);
                dateString += (date == 1) ? "day" : "days";
                time.setText(dateString);
            } else if (trueTime >= 3600) {
                int date = (int) trueTime / 3600;
                String dateString = DOT + String.valueOf(date);
                dateString += (date == 1) ? "hr" : "hrs";
                time.setText(dateString);
            } else if (trueTime >= 60) {
                int date = (int) trueTime / 60;
                String dateString = DOT + String.valueOf(date) + "min";
                time.setText(dateString);
            } else {
                String dateString = DOT + String.valueOf(trueTime) + "sec";
                time.setText(dateString);
            }
        }

        @Override
        public void onClick(View v) {
            int clickedPosition = getAdapterPosition();
            switch (v.getId()) {
                case R.id.upContainer:
                    break;
                case R.id.downContainer:
                    break;
                case R.id.commentItemFrame:
                    mOnClickListener.onCommentSelected(clickedPosition, expandArea, commentFrame);
                    break;
                case R.id.imageContainer:
                    mOnClickListener.onListItemClick(clickedPosition, v);
                    break;
                case R.id.commentsAction:
                    mOnClickListener.onListItemClick(clickedPosition, v);
                    break;
            }
        }
    }
}