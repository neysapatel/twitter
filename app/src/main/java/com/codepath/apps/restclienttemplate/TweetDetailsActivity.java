package com.codepath.apps.restclienttemplate;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.media.Image;
import android.os.Bundle;
import android.os.Parcel;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.codepath.apps.restclienttemplate.models.Tweet;

import org.parceler.Parcels;

public class TweetDetailsActivity extends AppCompatActivity {

    ImageView ivProfileImage;
    TextView tvBody;
    TextView tvScreenName;
    TextView tvName;
    TextView ttime;
    ImageView tImage;
    ImageView likeButton;
    ImageView replyButton;
    ImageView retweetButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tweet_details);

        Tweet tweet = Parcels.unwrap(getIntent().getParcelableExtra("tweet"));

        ivProfileImage = findViewById(R.id.ivProfileImage);
        tvBody = findViewById(R.id.tvBody);
        tvScreenName = findViewById(R.id.tvScreenName);
        tvName = findViewById(R.id.tvName);
        ttime = findViewById(R.id.time);
        tImage = findViewById(R.id.tImage);
        likeButton = findViewById(R.id.likeButton);
        replyButton = findViewById(R.id.replyButton);
        retweetButton = findViewById(R.id.retweetButton);

        tvBody.setText(tweet.body);
        tvScreenName.setText("@" + tweet.user.screenName);
        tvName.setText("@" + tweet.user.name);
        ttime.setText(tweet.getRelativeTimeAgo(tweet.createdAt));

        int rad = 75;
        Glide.with(this).load(tweet.user.publicImageUrl).apply(new RequestOptions().centerCrop().transform(new RoundedCorners(rad))).into(ivProfileImage);

        if (tweet.imageURL != null) {
            Glide.with(this).load(tweet.imageURL).apply(new RequestOptions().centerCrop().transform(new RoundedCorners(rad))).into(tImage);
            tImage.setVisibility(View.VISIBLE);
        } else {
            tImage.setVisibility(View.GONE);
        }
    }
}
