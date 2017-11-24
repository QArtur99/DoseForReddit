package com.qartf.doseforreddit.view.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.VideoOptions;
import com.google.android.gms.ads.formats.NativeAd;
import com.google.android.gms.ads.formats.NativeAdOptions;
import com.google.android.gms.ads.formats.NativeContentAd;
import com.google.android.gms.ads.formats.NativeContentAdView;
import com.qartf.doseforreddit.BuildConfig;
import com.qartf.doseforreddit.R;
import com.qartf.doseforreddit.data.entity.Post;
import com.qartf.doseforreddit.presenter.utility.Utility;

import java.util.List;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;

public class PostsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public static final String DOT = "\u2022";
    public static final int AD_RATE = 12;
    private static final String ADMOB_AD_UNIT_ID = BuildConfig.ADMOB_AD_UNIT_ID;
    private static final String ADMOB_APP_ID = BuildConfig.ADMOB_APP_ID;
    private static final int ITEM_VIEW_TYPE = 0;
    private static final int AD_VIEW_TYPE = 1;

    final private ListItemClickListener mOnClickListener;
    private List<Object> data;
    private Context context;


    public PostsAdapter(Context context, List<Object> myDataset, ListItemClickListener listener) {
        this.context = context;
        data = myDataset;
        mOnClickListener = listener;
        MobileAds.initialize(context, ADMOB_APP_ID);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case ITEM_VIEW_TYPE:
                View itemView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.row_post_item, parent, false);
                Utility.setThumbnailSize(context, itemView);
                return new MyViewHolder(itemView);

            case AD_VIEW_TYPE:
            default:
                View nativeExpressLayoutView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.ad_native, parent, false);
                Utility.setThumbnailSize(context, nativeExpressLayoutView);
                return new NativeAdViewHolder(nativeExpressLayoutView);


        }
    }

    @Override
    public int getItemViewType(int position) {
//        return position !=0 && (position % AD_RATE == 0) && !Utility.isTablet(context) ? AD_VIEW_TYPE : ITEM_VIEW_TYPE;
        return ITEM_VIEW_TYPE;
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        int viewType = getItemViewType(position);

        switch (viewType) {
            case ITEM_VIEW_TYPE:
                MyViewHolder myViewHolder = (MyViewHolder) holder;
                myViewHolder.bind(position);
                break;
            case AD_VIEW_TYPE:
                NativeAdViewHolder nativeAdViewHolder = (NativeAdViewHolder) holder;
                nativeAdViewHolder.bind(position);
                break;

        }
    }

    public Object getDataAtPosition(int position) {
        return data.get(position);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void clearPosts() {
        this.data.clear();
        notifyDataSetChanged();
    }

    public void setPosts(List<Object> data) {
        this.data.addAll(data);
        notifyDataSetChanged();
    }

    public List<Object> getData() {
        return data;
    }

    public interface ListItemClickListener {
        void onPostSelected(int clickedItemIndex, View expandableView, View parent);

        void onListItemClick(int clickedItemIndex, View view);

        int getSelectedPosition();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
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
        @BindView(R.id.upArrow) ImageView upArrow;
        @BindView(R.id.downContainer) RelativeLayout downContainer;
        @BindView(R.id.downArrow) ImageView downArrow;
        @BindView(R.id.detailContainer) RelativeLayout detailContainer;
        @BindView(R.id.imageContainer) RelativeLayout imageContainer;
        @BindView(R.id.commentItemFrame) LinearLayout commentFrame;
        @BindView(R.id.commentsAction) ImageView commentsAction;
        @BindView(R.id.shareAction) ImageView shareAction;
        @BindView(R.id.save) ImageView save;
        @BindView(R.id.goToUrl) ImageView goToUrl;

        @BindString(R.string.comments) String commentsString;


        public MyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            upContainer.setOnClickListener(this);
            downContainer.setOnClickListener(this);
            detailContainer.setOnClickListener(this);
            imageContainer.setOnClickListener(this);
            commentsAction.setOnClickListener(this);
            shareAction.setOnClickListener(this);
            save.setOnClickListener(this);
            goToUrl.setOnClickListener(this);
        }

        public void bind(int position) {
            Post post = (Post) getDataAtPosition(position);
            if (position != mOnClickListener.getSelectedPosition()) {
                expandArea.setActivated(false);
                expandArea.setVisibility(View.GONE);
            }
            loadData(post);
        }

        private void loadData(Post post) {
            if (post.saved.equals("true")) {
                save.setColorFilter(ContextCompat.getColor(context, R.color.commentSave));
            } else {
                save.setColorFilter(ContextCompat.getColor(context, R.color.arrowColor));
            }
            Utility.upsFormat(ups, Integer.valueOf(post.ups));
            if (post.likes.equals("true")) {
                upArrow.setColorFilter(ContextCompat.getColor(context, R.color.upArrow));
                downArrow.setColorFilter(ContextCompat.getColor(context, R.color.arrowColor));

            } else if (post.likes.equals("false")) {
                upArrow.setColorFilter(ContextCompat.getColor(context, R.color.arrowColor));
                downArrow.setColorFilter(ContextCompat.getColor(context, R.color.downArrow));
            }
            title.setText(post.title);
            Utility.loadLinkFlairText(linkFlairText, post.linkFlairText);
            domain.setText("(" + post.domain + ")");
            subreddit.setText(post.subreddit);
            comments.setText(DOT + post.numComents + " " + commentsString);
            time.setText(Utility.timeFormat(post.createdUTC));
            Utility.loadThumbnail(context, post, thumbnail);
        }

        @Override
        public void onClick(View v) {
            int clickedPosition = getAdapterPosition();
            switch (v.getId()) {
                case R.id.detailContainer:
                    mOnClickListener.onPostSelected(clickedPosition, expandArea, commentFrame);
                    break;
                case R.id.upContainer:
//                    upArrow.setColorFilter(ContextCompat.getColor(context, R.color.upArrow));
//                    downArrow.setColorFilter(ContextCompat.getColor(context, R.color.arrowColor));
                    mOnClickListener.onListItemClick(clickedPosition, v);
                    break;
                case R.id.downContainer:
//                    upArrow.setColorFilter(ContextCompat.getColor(context, R.color.arrowColor));
//                    downArrow.setColorFilter(ContextCompat.getColor(context, R.color.downArrow));
                    mOnClickListener.onListItemClick(clickedPosition, v);
                    break;
                default:
                    mOnClickListener.onListItemClick(clickedPosition, v);
            }
        }

    }

    public class NativeAdViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.nativeContentAdView) NativeContentAdView nativeContentAdView;

        @BindView(R.id.contentad_headline) TextView contentad_headline;
        @BindView(R.id.thumbnail) ImageView contentad_image;
        @BindView(R.id.contentad_body) TextView contentad_body;
        @BindView(R.id.contentad_call_to_action) TextView contentad_call_to_action;
        @BindView(R.id.contentad_logo) ImageView contentad_logo;
        @BindView(R.id.contentad_advertiser) TextView contentad_advertiser;

        public NativeAdViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bind(int position) {
            refreshAd();
        }

        private void populateContentAdView(NativeContentAd nativeContentAd,
                                           NativeContentAdView adView) {

            adView.setHeadlineView(contentad_headline);
            adView.setImageView(contentad_image);
            adView.setBodyView(contentad_body);
            adView.setCallToActionView(contentad_call_to_action);
            adView.setLogoView(contentad_logo);
            adView.setAdvertiserView(contentad_advertiser);

            // Some assets are guaranteed to be in every NativeContentAd.
            ((TextView) adView.getHeadlineView()).setText(nativeContentAd.getHeadline());
            ((TextView) adView.getBodyView()).setText(nativeContentAd.getBody());
            ((TextView) adView.getCallToActionView()).setText(nativeContentAd.getCallToAction());
            ((TextView) adView.getAdvertiserView()).setText(nativeContentAd.getAdvertiser());

            List<NativeAd.Image> images = nativeContentAd.getImages();

            if (images.size() > 0) {
                ((ImageView) adView.getImageView()).setImageDrawable(images.get(0).getDrawable());
            }

            // Some aren't guaranteed, however, and should be checked.
            NativeAd.Image logoImage = nativeContentAd.getLogo();

            if (logoImage == null) {
                adView.getLogoView().setVisibility(View.INVISIBLE);
            } else {
                ((ImageView) adView.getLogoView()).setImageDrawable(logoImage.getDrawable());
                adView.getLogoView().setVisibility(View.VISIBLE);
            }

            // Assign native ad object to the native view.
            adView.setNativeAd(nativeContentAd);
        }

        private void refreshAd() {

            AdLoader.Builder builder = new AdLoader.Builder(context, ADMOB_AD_UNIT_ID);


            builder.forContentAd(new NativeContentAd.OnContentAdLoadedListener() {
                @Override
                public void onContentAdLoaded(NativeContentAd ad) {
                    populateContentAdView(ad, nativeContentAdView);
                }
            });


            VideoOptions videoOptions = new VideoOptions.Builder()
                    .setStartMuted(false)
                    .build();

            NativeAdOptions adOptions = new NativeAdOptions.Builder()
                    .setVideoOptions(videoOptions)
                    .build();

            builder.withNativeAdOptions(adOptions);

            AdLoader adLoader = builder.withAdListener(new AdListener() {
                @Override
                public void onAdFailedToLoad(int errorCode) {

                }
            }).build();

            adLoader.loadAd(new AdRequest.Builder().build());

        }

    }


}
