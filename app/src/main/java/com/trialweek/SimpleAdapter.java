package com.trialweek;

import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Random;

import im.ene.toro.CacheManager;

public class SimpleAdapter extends RecyclerView.Adapter<SimplePlayerViewHolder> implements CacheManager {

    MediaList mediaList = new MediaList();
    int[] gradient = {R.drawable.gradient_sub_1, R.drawable.gradient_sub_2, R.drawable.gradient_sub_3,
            R.drawable.gradient_sub_4, R.drawable.gradient_sub_5 };

    @Override public SimplePlayerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.view_holder_exoplayer_basic, parent, false);
        return new SimplePlayerViewHolder(view);
    }

    @Override public void onBindViewHolder(final SimplePlayerViewHolder holder, final int position) {
        holder.bind(mediaList.get(position));
        final ArrayList<CoordinatesDimension> arrayList = calculateCoordinates(MainActivity.widthPixels);
        holder.playerView.setOnTouchListener(new View.OnTouchListener() {
            private GestureDetector gestureDetector = new GestureDetector(holder.itemView.getContext(), new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onDoubleTap(MotionEvent e) {
                    Log.d("TEST", "onDoubleTap");

                    if(e.getX()>= arrayList.get(0).getStartCoordinate() && e.getX()<=arrayList.get(0).getEndCoordinate()) {
                        Log.d("test", "onDoubleTap: left");
                        holder.rewind.playAnimation();
                        holder.rewind.setVisibility(View.VISIBLE);
                        holder.fastforward.setVisibility(View.INVISIBLE);

                        long currentPosition = holder.playerView.getPlayer().getCurrentPosition();

                        if(currentPosition -10000  >=0 ) {
                            holder.playerView.getPlayer().seekTo(currentPosition - 1000);
                        } else {
                            holder.playerView.getPlayer().seekTo(0);
                        }


                    }else if(e.getX()>= arrayList.get(1).getStartCoordinate() && e.getX()<=arrayList.get(1).getEndCoordinate()) {
                        Log.d("test", "onDoubleTap: centre");
                    }else if(e.getX()>= arrayList.get(2).getStartCoordinate() && e.getX()<=arrayList.get(2).getEndCoordinate()) {
                        Log.d("test", "onDoubleTap: right");
                        holder.fastforward.playAnimation();
                        holder.fastforward.setVisibility(View.VISIBLE);
                        holder.rewind.setVisibility(View.INVISIBLE);

                        long currentPosition = holder.playerView.getPlayer().getCurrentPosition();

                        if(currentPosition+10000 <= holder.playerView.getPlayer().getDuration()) {
                            holder.playerView.getPlayer().seekTo(currentPosition + 10000);
                        } else holder.playerView.getPlayer().seekTo(holder.playerView.getPlayer().getDuration());
                    }

                    return super.onDoubleTap(e);
                }

                @Override
                public boolean onSingleTapConfirmed(MotionEvent e) {
                    if(holder.isPlaying()) {
                        holder.play.setVisibility(View.VISIBLE);
                        holder.pause();
                    }
                    else {
                        holder.play.setVisibility(View.INVISIBLE);
                        holder.play();
                    }
                    return super.onSingleTapConfirmed(e);
                }

                @Override
                public boolean onDown(MotionEvent e) {
//                    if(holder.isPlaying()) holder.pause();
//                    else holder.play();
                    return super.onDown(e);
                }
            });

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Log.d("TEST", "Raw event: " + event.getAction() + ", (" + event.getRawX() + ", " + event.getRawY() + ")");
                gestureDetector.onTouchEvent(event);
                return true;
            }
        });
        final int random = new Random().nextInt(5);
        holder.subtitle.setBackgroundResource(gradient[random]);
    }

    private ArrayList<CoordinatesDimension> calculateCoordinates(int widthPixels) {
        ArrayList<CoordinatesDimension> arr = new ArrayList<>();

        int leftEnd = widthPixels/3;
        CoordinatesDimension leftCoordinates = new CoordinatesDimension(0,leftEnd);
        arr.add(0,leftCoordinates);

        int centreStart = leftEnd;
        int centreEnd = leftEnd*2;

        CoordinatesDimension centreCoordinates = new CoordinatesDimension(centreStart,centreEnd);
        arr.add(1,centreCoordinates);

        int rightStart = centreEnd;
        int rightEnd = widthPixels;

        CoordinatesDimension rightCoordinates = new CoordinatesDimension(rightStart,rightEnd);
        arr.add(2,rightCoordinates);

        return arr;
    }

    @Override public int getItemCount() {
        return mediaList.size();
    }

    @Nullable
    @Override
    public Object getKeyForOrder(int order) {
        return order;
    }

    @Nullable
    @Override
    public Integer getOrderForKey(@NonNull Object key) {
        return (Integer) key;
    }
}
