package com.qartf.doseforreddit.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.transition.TransitionManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.qartf.doseforreddit.R;
import com.qartf.doseforreddit.activity.LinkActivity;
import com.qartf.doseforreddit.model.Post;
import com.qartf.doseforreddit.utility.Utility;

import java.util.List;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;

public class PostsAdapter extends RecyclerView.Adapter<PostsAdapter.MyViewHolder> {

    public static final String DOT = "\u2022";
    final private ListItemClickListener mOnClickListener;

    private List<Post> data;
    private LinearLayout previousView;
    private boolean isOpen = false;
    private Context context;


    public PostsAdapter(Context context, List<Post> myDataset, ListItemClickListener listener) {
        this.context = context;
        data = myDataset;
        mOnClickListener = listener;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.row_post_item, parent, false);
        Utility.setThumbnailSize(context, view);
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

    public void setMovies(List<Post> data) {
        this.data.addAll(data);
        notifyDataSetChanged();
    }

    public List<Post> getData() {
        return data;
    }

    public interface ListItemClickListener {
        void onListItemClick(int clickedItemIndex, View view);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        @BindView(R.id.thumbnail) ImageView thumbnail;
        @BindView(R.id.ups) TextView ups;
        @BindView(R.id.title) TextView title;
        @BindView(R.id.subreddit) TextView subreddit;
        @BindView(R.id.linkFlairText) TextView linkFlairText;
        @BindView(R.id.domain) TextView domain;
        @BindView(R.id.comments) TextView comments;
        @BindView(R.id.time) TextView time;
        @BindView(R.id.expandArea) LinearLayout expandArea;

        @BindView(R.id.upContainer) RelativeLayout upContainer;
        @BindView(R.id.downContainer) RelativeLayout downContainer;
        @BindView(R.id.detailContainer) RelativeLayout detailContainer;
        @BindView(R.id.imageContainer) RelativeLayout imageContainer;

        @BindView(R.id.commentsAction) ImageView commentsAction;
        @BindView(R.id.shareAction) ImageView shareAction;

        @BindString(R.string.comments) String commentsString;

        public MyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            upContainer.setOnClickListener(this);
            downContainer.setOnClickListener(this);
            detailContainer.setOnClickListener(this);
            imageContainer.setOnClickListener(this);
            imageContainer.setOnLongClickListener(this);
            commentsAction.setOnClickListener(this);
            shareAction.setOnClickListener(this);
        }

        public void bind(int position) {
            Post post = (Post) getDataAtPosition(position);
            loadData(post);
        }

        private void loadData(Post post) {
            Utility.upsFormat(ups, Integer.valueOf(post.ups));
            title.setText(post.title);
            Utility.loadLinkFlairText(linkFlairText, post.linkFlairText);
            domain.setText("(" + post.domain + ")");
            subreddit.setText(post.subreddit);
            comments.setText(DOT + post.numComents + " " + commentsString);
            Utility.timeFormat(post.createdUTC, time);
            Utility.loadThumbnail(context, post, thumbnail);
        }

        @Override
        public void onClick(View v) {
            int clickedPosition = getAdapterPosition();
            switch (v.getId()) {
                case R.id.detailContainer:
                    actionDetail();
                    break;
                default:
                    mOnClickListener.onListItemClick(clickedPosition, v);
            }
        }

        private void actionDetail() {
            if (previousView != null) {
                previousView.setVisibility(View.GONE);
                previousView.setActivated(false);
            }

            if (previousView == null || !previousView.equals(expandArea) || isOpen) {
                expandArea.setVisibility(View.VISIBLE);
                expandArea.setActivated(true);
                expandArea.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        TransitionManager.beginDelayedTransition(expandArea);
//                    notifyDataSetChanged();
                    }
                });
                isOpen = false;
            } else {
                isOpen = true;
            }
            previousView = expandArea;
        }

        @Override
        public boolean onLongClick(View view) {
            int clickedPosition = getAdapterPosition();
            Post post = (Post) getDataAtPosition(clickedPosition);
            Intent intent = new Intent(context, LinkActivity.class);
            intent.putExtra("link", post.url);
            context.startActivity(intent);
            return true;
        }
    }

}
