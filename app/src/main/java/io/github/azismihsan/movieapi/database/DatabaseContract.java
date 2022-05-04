package io.github.azismihsan.movieapi.database;

import android.net.Uri;
import android.provider.BaseColumns;

public class DatabaseContract {

    public static final String AUTHORITY = "io.github.azismihsan.movieapi";
    public static final String SCHEME = "content";

    private DatabaseContract(){

    }

    public static final class MovieFavorite implements BaseColumns{
        public static final String TABLE_NAME = "movie";
        public static final String COLOUMS_MOVIE_ID = "movie_id";
        public static final String COLOUMS_TITLE_MOVIE = "title";
        public static final String COLOUMS_OVERVIEW_MOVIE = "overview";
        public static final String COLOUMS_RELEASE_DATE = "release_date";
        public static final String COLOUMS_POSTER = "post_path";
        public static final String COLOUMS_BACKGROUND = "backdrop_path";
        public static final String COLOUMS_RATE = "vote_average";

        public static final Uri CONTENT_URI_MOVIE = new Uri.Builder().scheme(SCHEME)
                .authority(AUTHORITY)
                .appendPath(TABLE_NAME)
                .build();
    }

    public static final class TvShowFavorite implements BaseColumns{
        public static final String TABLE_NAME = "tv";
        public static final String COLOUMS_MOVIE_ID = "tvshow_id";
        public static final String COLOUMS_TITLE_MOVIE = "title";
        public static final String COLOUMS_OVERVIEW_MOVIE = "overview";
        public static final String COLOUMS_RELEASE_DATE = "release_date_tv";
        public static final String COLOUMS_POSTER = "post_path";
        public static final String COLOUMS_BACKGROUND = "backdrop_path";
        public static final String COLOUMS_RATE = "vote_average";

        public static final Uri CONTENT_URI_TVSHOWS = new Uri.Builder().scheme(SCHEME)
                .authority(AUTHORITY)
                .appendPath(TABLE_NAME)
                .build();
    }
}
