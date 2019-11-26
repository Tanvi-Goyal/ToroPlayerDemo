# ToroPlayerDemo
## Toro Player Demo

## About
This repo helps users to getting started with Toro and Exoplayer while starting with Video Streaming in Android.

Link to APK - https://drive.google.com/open?id=1ZIvhOHL_243eqUP3eHnyOFTnStEIKi59

## Getting Started

## Video Streaming

Video streaming is a type of media streaming in which the data from a video file is continuously delivered via the Internet to a remote user. It allows a video to be viewed online without being downloaded on a host computer or device.

Video streaming works on data streaming principles, where all video file data is compressed and sent to a requesting device in small chunks. Video streaming typically requires a compatible video player that connects with a remote server, which hosts a pre-recorded or pre-stored media file or live feed. The server uses specific algorithms to compress the media file or data for transfer over the network or Internet connection.
 Video Streaming in Android

The Android platform provides libraries you can use to stream media files, such as remote videos, presenting them for playback in your apps. 
While the goal can be achieved using native Android Video View and Media Controller, there are some cool libraries to perform the task more efficiently.

## ExoPlayer

ExoPlayer is an application level media player for Android. It provides an alternative to Android’s MediaPlayer API for playing audio and video both locally and over the Internet. ExoPlayer supports features not currently supported by Android’s MediaPlayer API, including DASH and SmoothStreaming adaptive playbacks. Unlike the MediaPlayer API, ExoPlayer is easy to customize and extend, and can be updated through Play Store application updates.

## Why Toro

1.	It’s a specially built custom library for android.
2.	It internally manages the visibility area and handles which Player to to start on the basis of the screen availability which is not supported in Exoplayer.
3.	Toro creates some instances of the Player View and reuses them again and again instead of creating a separate view for every recycler view item. A recycler view in Toro is called a container

## Features implemented in the project

1.	An application which houses a list of videos, with gesture controlled navigation.
2.	Support fling gestures to navigate between different videos.
3.	Tap gestures to seek the video to a different time.
4.	Handled memory leaks in the application using LeakCanary

## Tech Stack Used 
The application is made using Toro and Exoplayer for Video Streaming. For animations, I have used the LottieAnimations Library. ButterKnife is used for fast and easy binding of xml components.

## Implementation

1.	To store and retrieve the video state in playerview, PlaybckInfo class of Toro is used. I created a Adapter for the playerview container which implements the CacheManager which maintains the order and key of the player view being currently instantiated by Toro. Further in Mainactivity, the container is set to this cache manager using container.setCacheManager().
Then using the getSavedPlayerOrder(), all the saved orders are retrieved along this their playback state using PlaybackInfo class .

        container.setCacheManager(adapter);
        //Save Link of the current ToroPlayer
        for (int i = 0; i < container.getSavedPlayerOrders().size(); i++) {
            int order = adapter.getOrderForKey(container.getSavedPlayerOrders().get(i));
            PlaybackInfo playbackInfo = container.getPlaybackInfo(order);

            container.savePlaybackInfo(order, playbackInfo);
        }
2.	Smooth scrolling and snapping is maintained in the application using onSmoothScroll function of Linearlayoutmanager and PageSnapHelper class of Android.

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
        
         PagerSnapHelper snapHelper = new PagerSnapHelper();
        snapHelper.attachToRecyclerView(container);
        
3.	Gestures are being supported using theGestureDetector Listener of Android overriding the onSingleTap and onDoubleTap methods for play, pause, forward and rewind video functionality.
 
       holder.playerView.setOnTouchListener(new View.OnTouchListener() {
            private GestureDetector gestureDetector = new GestureDetector(holder.itemView.getContext(), new                      GestureDetector.SimpleOnGestureListener() {
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


                    }else if(e.getX()>= arrayList.get(1).getStartCoordinate() && e.getX()  <=arrayList.get(1).getEndCoordinate()) {
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
                    if(holder.isPlaying()) holder.pause();
                    else holder.play();  
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
4.	Memory leaks in the application is maintained using LeakCanary Library.
 
        if (LeakCanary.isInAnalyzerProcess(this)) {
            return;
        }
        LeakCanary.install(getApplication());

