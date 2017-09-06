package com.qartf.doseforreddit.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.support.v7.widget.RecyclerView;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.qartf.doseforreddit.R;
import com.qartf.doseforreddit.activity.LinkActivity;
import com.qartf.doseforreddit.model.Subreddit;
import com.qartf.doseforreddit.utility.Utility;

import java.text.DecimalFormat;
import java.util.List;

import butterknife.BindColor;
import butterknife.BindView;
import butterknife.ButterKnife;

public class SubredditAdapter extends RecyclerView.Adapter<SubredditAdapter.MyViewHolder> {

    public static final String DOT = "\u2022";
    public static final String KILO = "K";
    final private ListItemClickListener mOnClickListener;
    private DecimalFormat decimalFormat = new DecimalFormat("##.#");

    private List<Subreddit> data;
    private LinearLayout previousView;
    private boolean isOpen = false;
    private Context context;


    public SubredditAdapter(Context context, List<Subreddit> myDataset, ListItemClickListener listener) {
        this.context = context;
        data = myDataset;
        mOnClickListener = listener;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.row_subreddit_item, parent, false);
//        setThumbnailSize(view);
        return new MyViewHolder(view);
    }

    private void setThumbnailSize(View view) {
        ImageView imageView = view.findViewById(R.id.thumbnail);
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getRealSize(size);
        int width = size.x / 5;

        ViewGroup.LayoutParams layoutParams = imageView.getLayoutParams();
        layoutParams.height = width;
        imageView.setLayoutParams(layoutParams);
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

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

        @BindView(R.id.isSubscribe) TextView isSubscribe;
        @BindView(R.id.isAdult) TextView isAdult;
        @BindView(R.id.namePrefix) TextView namePrefix;
        @BindView(R.id.title) TextView title;
        @BindView(R.id.subsribersNo) TextView subsribersNo;
        @BindView(R.id.createdTime) TextView createdTime;
        @BindView(R.id.subredditTitleFrame) RelativeLayout subredditTitleFrame;
        @BindColor(R.color.subredditButtonBackgroundPrivate) int color;


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

        private void loadData(Subreddit post) {
            setSubscribeButton(post);
            setAdultMark(post);
            namePrefix.setText(post.display_name_prefixed);
            title.setText(post.title);
            String subNo = post.subscribers + " subscribers";
            subsribersNo.setText(subNo);
            Utility.timeFormat(post.created_utc, createdTime);
        }

        private void setAdultMark(Subreddit post) {
            if (post.over18.equals("true")) {
                isAdult.setText("18+");
            } else {
                isAdult.setText("");
            }
        }

        private void setSubscribeButton(Subreddit subreddit) {
            isSubscribe.setSelected(false);
            if (subreddit.user_is_subscriber.equals("false")) {
                isSubscribe.setActivated(false);
                isSubscribe.setText("subsribe");
            } else if (subreddit.user_is_subscriber.equals("true")) {
                isSubscribe.setActivated(true);
                isSubscribe.setText("unsubscribe");
            } else if (subreddit.user_is_subscriber.isEmpty()) {
                isSubscribe.setSelected(true);
                isSubscribe.setText("private");
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
                            isSubscribe.setText("subsribe");
                        } else {
                            isSubscribe.setActivated(true);
                            isSubscribe.setText("unsubscribe");
                        }
                        mOnClickListener.onListItemClick(clickedPosition, v);
                    }
                    break;
                case R.id.subredditTitleFrame:
                    mOnClickListener.onListItemClick(clickedPosition, v);
                    break;
            }
        }

        @Override
        public boolean onLongClick(View view) {
            int clickedPosition = getAdapterPosition();
            Subreddit post = (Subreddit) getDataAtPosition(clickedPosition);
            Intent intent = new Intent(context, LinkActivity.class);
            intent.putExtra("link", post.url);
            context.startActivity(intent);
            return true;
        }
    }


}
