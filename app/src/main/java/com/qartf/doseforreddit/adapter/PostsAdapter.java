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
import com.qartf.doseforreddit.activity.CommentsActivity;
import com.qartf.doseforreddit.activity.LinkActivity;
import com.qartf.doseforreddit.model.PostObject;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;

import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by ART_F on 2017-08-27.
 */

public class PostsAdapter extends RecyclerView.Adapter<PostsAdapter.MyViewHolder> {

    public static final String DOT = "\u2022";
    public static final String KILO = "K";
    final private ListItemClickListener mOnClickListener;
    private DecimalFormat decimalFormat = new DecimalFormat("##.#");

    private List<PostObject> data;
    private LinearLayout previousView;
    private boolean isOpen = false;
    private Context context;


    public PostsAdapter(Context context, List<PostObject> myDataset, ListItemClickListener listener) {
        this.context = context;
        data = myDataset;
        mOnClickListener = listener;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.row_post, parent, false);
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

    public void setMovies(List<PostObject> data) {
        this.data.addAll(data);
        notifyDataSetChanged();
    }

    public List<PostObject> getData() {
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


        public MyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            upContainer.setOnClickListener(this);
            downContainer.setOnClickListener(this);
            detailContainer.setOnClickListener(this);
            imageContainer.setOnClickListener(this);
            imageContainer.setOnLongClickListener(this);
            commentsAction.setOnClickListener(this);
        }

        public void bind(int position) {
            PostObject post = (PostObject) getDataAtPosition(position);
            loadData(post);
        }

        private void loadData(PostObject post) {
            upsFormat(post);
            title.setText(post.title);
            loadLinkFlairText(post);
            domain.setText("(" + post.domain + ")");
            subreddit.setText(post.subreddit);
            comments.setText(DOT + post.numComents + " comments");
            timeFormat(post);
            loadThumbnail(post);
        }

        private void loadLinkFlairText(PostObject post) {
            if (!post.linkFlairText.equals("null")) {
                linkFlairText.setVisibility(View.VISIBLE);
                linkFlairText.setText(post.linkFlairText);
            } else {
                linkFlairText.setVisibility(View.GONE);
            }
        }

        private void loadThumbnail(PostObject post) {
//            if (checkThumbnail(post.thumbnail)) {
//                Glide.with(context)
//                        .load(post.thumbnail)
//                        .thumbnail(Glide.with(context).load(R.drawable.ic_ondemand_video))
//                        .into(thumbnail);
//            } else {
//                thumbnail.setImageResource(R.drawable.ic_ondemand_video);
//            }

            if (!post.thumbnail.isEmpty()) {
                String to_remove = "amp;";
                String new_string = post.thumbnail.replace(to_remove, "");
                Glide.with(context)
                        .load(new_string)
                        .thumbnail(Glide.with(context).load(R.drawable.ic_ondemand_video))
                        .into(thumbnail);
            } else if (!post.previewUrl.isEmpty()) {
                String to_remove = "amp;";
                String new_string = post.previewUrl.replace(to_remove, "");
                Glide.with(context)
                        .load(new_string)
                        .thumbnail(Glide.with(context).load(R.drawable.ic_ondemand_video))
                        .into(thumbnail);

            } else {
                thumbnail.setImageResource(R.drawable.ic_ondemand_video);
            }
        }

        public Boolean checkThumbnail(String thumnail) {
            if (thumnail.equals("self")) {
                return false;
            }
//            else if (thumnail.equals("default")) {
//                return false;
//            }
//            else if (thumnail.equals("image")) {
//                return false;
//            }
            else if (thumnail.isEmpty()) {
                return false;
            }
            return true;
        }

        private void upsFormat(PostObject post) {
            int upsInteger = Integer.valueOf(post.ups);
            if (upsInteger >= 10000) {
                double upsDouble = upsInteger / 1000d;
                String upsString = decimalFormat.format(upsDouble) + KILO;
                ups.setText(upsString);
            } else {
                ups.setText(String.valueOf(upsInteger));
            }
        }

        private void timeFormat(PostObject post) {
            Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
            long currentTime = calendar.getTimeInMillis() / 1000L;
            double timeDouble = Double.valueOf(post.createdUTC);
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
                case R.id.detailContainer:
                    actionDetail();
                    break;
                case R.id.imageContainer:
                    mOnClickListener.onListItemClick(clickedPosition, v);
                    break;
                case R.id.commentsAction:
                    String jsonString = new Gson().toJson(getDataAtPosition(clickedPosition));
                    Intent intent = new Intent(context, CommentsActivity.class);
                    intent.putExtra("link", jsonString);
                    context.startActivity(intent);
                    break;
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
            PostObject post = (PostObject) getDataAtPosition(clickedPosition);
            Intent intent = new Intent(context, LinkActivity.class);
            intent.putExtra("link", post.url);
            context.startActivity(intent);
            return true;
        }
    }

}
