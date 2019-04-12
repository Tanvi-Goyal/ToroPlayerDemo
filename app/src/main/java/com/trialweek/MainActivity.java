package com.trialweek;

import android.content.Context;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSmoothScroller;
import android.support.v7.widget.PagerSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;

import com.squareup.leakcanary.LeakCanary;

import im.ene.toro.exoplayer.MediaSourceBuilder;
import im.ene.toro.media.PlaybackInfo;
import im.ene.toro.widget.Container;

public class MainActivity extends AppCompatActivity {

    public static int height, width, widthPixels;
    Container container;
    SimpleAdapter adapter;
    LinearLayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (LeakCanary.isInAnalyzerProcess(this)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            return;
        }
        LeakCanary.install(getApplication());


        setContentView(R.layout.activity_main);
        container = findViewById(R.id.container);

        //Getting the current dimensions of the screen.
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        height = displayMetrics.heightPixels;
        width = displayMetrics.widthPixels;
        widthPixels = displayMetrics.widthPixels;

        layoutManager = new LinearLayoutManager(this){
            @Override
            public void smoothScrollToPosition(RecyclerView recyclerView, RecyclerView.State state, int position) {


                LinearSmoothScroller smoothScroller = new LinearSmoothScroller(MainActivity.this) {

                    private static final float SPEED = 300f;

                    @Override
                    protected float calculateSpeedPerPixel(DisplayMetrics displayMetrics) {
                        return SPEED / displayMetrics.densityDpi;
                    }

                };
                smoothScroller.setTargetPosition(position);
                startSmoothScroll(smoothScroller);
            }
        };

        container.setLayoutManager(layoutManager);

        adapter = new SimpleAdapter();
        container.setAdapter(adapter);

        PagerSnapHelper snapHelper = new PagerSnapHelper();
        snapHelper.attachToRecyclerView(container);

        container.setCacheManager(adapter);

        //Save Link of the current ToroPlayer
        for (int i = 0; i < container.getSavedPlayerOrders().size(); i++) {
            int order = adapter.getOrderForKey(container.getSavedPlayerOrders().get(i));
            PlaybackInfo playbackInfo = container.getPlaybackInfo(order);

            container.savePlaybackInfo(order, playbackInfo);
        }

    }
}