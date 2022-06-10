package com.codepath.apps.restclienttemplate;

import android.content.Context;
import android.content.Intent;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.codepath.apps.restclienttemplate.models.Tweet;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;

import org.parceler.Parcels;
import org.w3c.dom.Text;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import okhttp3.Headers;

public class TweetsAdapter extends RecyclerView.Adapter<TweetsAdapter.ViewHolder> {
    Context context;
    List<Tweet> tweets;

    public TweetsAdapter(Context context, List<Tweet> tweets) {
        this.context = context;
        this.tweets = tweets;
    }

    public void clear() {
        tweets.clear();
        notifyDataSetChanged();
    }

    public void addAll(List<Tweet> list) {
        tweets.addAll(list);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_tweet, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Tweet tweet = tweets.get(position);
        holder.bind(tweet);
    }

    @Override
    public int getItemCount() {
        return tweets.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView ivProfileImage;
        TextView tvBody;
        TextView tvScreenName;
        TextView ttime;
        ImageView tImage;
        ImageView likeButton;
        ImageView replyButton;
        ImageView retweetButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            ivProfileImage = itemView.findViewById(R.id.ivProfileImage);
            tvBody = itemView.findViewById(R.id.tvBody);
            tvScreenName = itemView.findViewById(R.id.tvScreenName);
            ttime = itemView.findViewById(R.id.time);
            tImage = itemView.findViewById(R.id.tImage);
            likeButton = itemView.findViewById(R.id.likeButton);
            replyButton = itemView.findViewById(R.id.replyButton);
            retweetButton = itemView.findViewById(R.id.retweetButton);
        }

        public void bind(Tweet tweet) {
            tvBody.setText(tweet.body);
            tvScreenName.setText("@" + tweet.user.screenName);

            int rad = 75;
            Glide.with(context).load(tweet.user.publicImageUrl).apply(new RequestOptions().centerCrop().transform(new RoundedCorners(rad))).into(ivProfileImage);
            ttime.setText(tweet.getRelativeTimeAgo(tweet.createdAt));

            if (tweet.imageURL != null) {
                Glide.with(context).load(tweet.imageURL).apply(new RequestOptions().centerCrop().transform(new RoundedCorners(rad))).into(tImage);
                tImage.setVisibility(View.VISIBLE);
            } else {
                tImage.setVisibility(View.GONE);
            }

            tvBody.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(context, TweetDetailsActivity.class);
                    i.putExtra("tweet", Parcels.wrap(tweet));
                    context.startActivity(i);
                }
            });

            TwitterClient tc = new TwitterClient(context);

            likeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    tc.like(tweet.id, new JsonHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Headers headers, JSON json) {}

                        @Override
                        public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {}
                    });
                }
            });

            retweetButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    tc.retweet(tweet.id, new JsonHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Headers headers, JSON json) {}

                        @Override
                        public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {}
                    });
                }
            });
        }
    }
}
