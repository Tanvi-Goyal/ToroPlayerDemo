package com.trialweek;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewParent;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;

import butterknife.BindView;
import butterknife.ButterKnife;
import im.ene.toro.ToroPlayer;
import im.ene.toro.ToroUtil;
import im.ene.toro.exoplayer.SimpleExoPlayerViewHelper;
import im.ene.toro.helper.ToroPlayerHelper;
import im.ene.toro.media.PlaybackInfo;
import im.ene.toro.widget.Container;

public class SimplePlayerViewHolder extends RecyclerView.ViewHolder implements ToroPlayer {

    SimpleExoPlayerView playerView;
    ToroPlayerHelper helper;
    Uri mediaUri;
    Bitmap icon;
    ImageButton play;
    RelativeLayout subtitle;
    LottieAnimationView rewind, fastforward, buffering;
    ToroPlayer.EventListener eventListener = new EventListener() {
        @Override
        public void onBuffering() {
            buffering.playAnimation();
        }

        @Override
        public void onPlaying() {
            buffering.cancelAnimation();
            buffering.setVisibility(View.INVISIBLE);
            rewind.setVisibility(View.INVISIBLE);
            fastforward.setVisibility(View.INVISIBLE);
        }

        @Override
        public void onPaused() {
        }

        @Override
        public void onCompleted(Container container, ToroPlayer player) {

        }
    };

    public SimplePlayerViewHolder(View itemView) {
        super(itemView);
        playerView = itemView.findViewById(R.id.player);
        play = itemView.findViewById(R.id.play_btn);
        fastforward = itemView.findViewById(R.id.forward_btn);
        rewind = itemView.findViewById(R.id.rewind_btn);
        buffering = itemView.findViewById(R.id.buffering);
        subtitle = itemView.findViewById(R.id.subtitle);
         icon = BitmapFactory.decodeResource(itemView.getResources(),
                R.drawable.food_bg);
    }

    @Override public View getPlayerView() {
        return playerView;
    }

    @Override public PlaybackInfo getCurrentPlaybackInfo() {
        return helper != null ? helper.getLatestPlaybackInfo() : new PlaybackInfo();
    }

    @Override
    public void initialize(Container container, PlaybackInfo playbackInfo) {

        if (helper == null) {

            helper = new SimpleExoPlayerViewHelper(container, this, mediaUri);
            helper.addPlayerEventListener(eventListener);
        }
        helper.initialize(playbackInfo);
        helper.addPlayerEventListener(eventListener);
    }

    @Override public void play() {
        if (helper != null) helper.play();
    }

    @Override public void pause() {
        if (helper != null) helper.pause();
    }

    @Override public boolean isPlaying() {
        return helper != null && helper.isPlaying();
    }

    @Override public void release() {
        if (helper != null) {
            helper.removePlayerEventListener(eventListener);
            helper.release();
            helper = null;
        }
    }

    @Override public boolean wantsToPlay() {
        ViewParent parent = itemView.getParent();
        float offset = 0;
        if (parent != null && parent instanceof View) {
            offset = ToroUtil.visibleAreaOffset(this, itemView.getParent());
        }
        return offset >= 0.85;
    }

    @Override public int getPlayerOrder() {
        return getAdapterPosition();
    }

    void bind(Content.Media media) {
        this.mediaUri = media.mediaUri;
    }
}