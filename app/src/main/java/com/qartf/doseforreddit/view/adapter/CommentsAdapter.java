package com.qartf.doseforreddit.view.adapter;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.qartf.doseforreddit.R;
import com.qartf.doseforreddit.data.entity.Comment;
import com.qartf.doseforreddit.presenter.utility.Utility;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CommentsAdapter extends RecyclerView.Adapter<CommentsAdapter.MyViewHolder> {

    private static final String DOT = "\u2022";
    final private OnListItemClickListener mOnClickListener;

    private List<Comment> data;
    private Context context;

    public CommentsAdapter(Context context, List<Comment> myDataset, OnListItemClickListener listener) {
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

    public interface OnListItemClickListener {
        void onCommentListItemClick(int clickedItemIndex, View view);
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
        @BindView(R.id.commentVoteUp) ImageView commentVoteUp;
        @BindView(R.id.commentVoteDown) ImageView commentVoteDown;

        public MyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            commentFrame.setOnClickListener(this);
            commentVoteUp.setOnClickListener(this);
            commentVoteDown.setOnClickListener(this);
        }

        public void bind(int position) {
            Comment post = (Comment) getDataAtPosition(position);
            loadData(post);
        }

        private void loadData(Comment comment) {
            String authorString = comment.author + DOT;
            author.setText(authorString);
            Utility.upsFormatPoints(score, Integer.valueOf(comment.ups));
            time.setText(Utility.timeFormat(comment.createdUtc));
            body.setText(comment.body);
            if(comment.commentList != null){
                childrenCommentFrame.setVisibility(View.VISIBLE);
                LinearLayoutManager layoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
                layoutManager.setAutoMeasureEnabled(true);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setHasFixedSize(true);
                CommentsAdapter commentsAdapter = new CommentsAdapter(context, comment.commentList, mOnClickListener);
                recyclerView.setAdapter(commentsAdapter);
            }
        }

        @Override
        public void onClick(View v) {
            int clickedPosition = getAdapterPosition();
            switch (v.getId()) {
                case R.id.commentVoteUp:
                    mOnClickListener.onCommentListItemClick(clickedPosition, v);
                    break;
                case R.id.commentVoteDown:
                    mOnClickListener.onCommentListItemClick(clickedPosition, v);
                    break;
                case R.id.commentItemFrame:
                    mOnClickListener.onCommentSelected(clickedPosition, expandArea, commentFrame);
                    break;
            }
        }
    }
}