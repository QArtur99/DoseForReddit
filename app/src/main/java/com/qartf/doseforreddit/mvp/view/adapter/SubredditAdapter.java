package com.qartf.doseforreddit.mvp.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.qartf.doseforreddit.R;
import com.qartf.doseforreddit.mvp.data.entity.Subreddit;
import com.qartf.doseforreddit.mvp.presenter.utility.Utility;

import java.util.List;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;

public class SubredditAdapter extends RecyclerView.Adapter<SubredditAdapter.MyViewHolder> {

    final private ListItemClickListener mOnClickListener;

    private List<Subreddit> data;

    public SubredditAdapter(Context context, List<Subreddit> myDataset, ListItemClickListener listener) {
        data = myDataset;
        mOnClickListener = listener;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.row_subreddit_item, parent, false);
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

    public void setMovies(List<Subreddit> data) {
        this.data.addAll(data);
        notifyDataSetChanged();
    }

    public List<Subreddit> getData() {
        return data;
    }

    public interface ListItemClickListener {
        void onListItemClick(int clickedItemIndex, View view);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.isSubscribe) TextView isSubscribe;
        @BindView(R.id.isAdult) TextView isAdult;
        @BindView(R.id.namePrefix) TextView namePrefix;
        @BindView(R.id.title) TextView title;
        @BindView(R.id.subsribersNo) TextView subsribersNo;
        @BindView(R.id.createdTime) TextView createdTime;
        @BindView(R.id.subredditTitleFrame) RelativeLayout subredditTitleFrame;

        @BindString(R.string.subscribers) String subscribers;
        @BindString(R.string.subsribe) String subsribe;
        @BindString(R.string.unsubscribe) String unsubscribe;
        @BindString(R.string.privateSub) String privateSub;
        @BindString(R.string.is18) String is18;


        public MyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            isSubscribe.setOnClickListener(this);
            subredditTitleFrame.setOnClickListener(this);
        }

        public void bind(int position) {
            Subreddit post = (Subreddit) getDataAtPosition(position);
            loadData(post);
        }

        private void loadData(Subreddit subreddit) {
            setSubscribeButton(subreddit);
            setAdultMark(subreddit);
            namePrefix.setText(subreddit.display_name_prefixed);
            title.setText(subreddit.title);
            String subNo = subreddit.subscribers + " " + subscribers;
            subsribersNo.setText(subNo);
            createdTime.setText(Utility.timeFormat(subreddit.created_utc));
        }

        private void setAdultMark(Subreddit post) {
            if (post.over18.equals("true")) {
                isAdult.setText(is18);
            } else {
                isAdult.setText("");
            }
        }

        private void setSubscribeButton(Subreddit subreddit) {
            isSubscribe.setSelected(false);
            if (subreddit.user_is_subscriber.equals("false")) {
                isSubscribe.setActivated(false);
                isSubscribe.setText(subsribe);
            } else if (subreddit.user_is_subscriber.equals("true")) {
                isSubscribe.setActivated(true);
                isSubscribe.setText(unsubscribe);
            } else if (subreddit.user_is_subscriber.isEmpty()) {
                isSubscribe.setSelected(true);
                isSubscribe.setText(privateSub);
            }
        }

        @Override
        public void onClick(View v) {
            int clickedPosition = getAdapterPosition();
            switch (v.getId()) {
                case R.id.isSubscribe:
                    Subreddit subreddit = (Subreddit) getDataAtPosition(clickedPosition);
                    if (!subreddit.user_is_subscriber.isEmpty()) {
                        if (isSubscribe.isActivated()) {
                            isSubscribe.setActivated(false);
                            isSubscribe.setText(subsribe);
                        } else {
                            isSubscribe.setActivated(true);
                            isSubscribe.setText(unsubscribe);
                        }
                        mOnClickListener.onListItemClick(clickedPosition, v);
                    }
                    break;
                case R.id.subredditTitleFrame:
                    mOnClickListener.onListItemClick(clickedPosition, v);
                    break;
            }
        }

    }

}
