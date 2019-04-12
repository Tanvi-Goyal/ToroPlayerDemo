package com.trialweek;

import android.net.Uri;

public class Content {

    public static class Media{
        public final int index;
        public final Uri mediaUri;

        public Media(int index, Uri mediaUri) {
            this.index = index;
            this.mediaUri = mediaUri;
        }

        static Media getItem(int index) {
            return new Media(index, Uri.parse("https://content.jwplatform.com/manifests/jRphHaoX.m3u8"));
        }

        @Override public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof Media)) return false;

            Media media = (Media) o;

            if (index != media.index) return false;
            return mediaUri.equals(media.mediaUri);
        }

        @Override public int hashCode() {
            int result = index;
            result = 31 * result + mediaUri.hashCode();
            return result;
        }

    }
}
