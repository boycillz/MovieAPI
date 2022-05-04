package io.github.azismihsan.movieapi.provider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import io.github.azismihsan.movieapi.database.DatabaseContract;
import io.github.azismihsan.movieapi.database.MovieHelper;

import static io.github.azismihsan.movieapi.database.DatabaseContract.AUTHORITY;
import static io.github.azismihsan.movieapi.database.DatabaseContract.MovieFavorite.CONTENT_URI_MOVIE;


public class MovieProvider extends ContentProvider {

    private MovieHelper movieHelper;
    private static final int MOVIE=1;
    private static final int MOVIE_ID=2;
    private static final int TV_SHOWS=3;
    private static final int TV_SHOWS_ID=4;

    private static final UriMatcher sUriMatcherMovies = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        sUriMatcherMovies.addURI(AUTHORITY, DatabaseContract.MovieFavorite.TABLE_NAME, MOVIE);
        sUriMatcherMovies.addURI(AUTHORITY, DatabaseContract.MovieFavorite.TABLE_NAME+"/#", MOVIE_ID);
        sUriMatcherMovies.addURI(AUTHORITY, DatabaseContract.TvShowFavorite.TABLE_NAME, TV_SHOWS);
        sUriMatcherMovies.addURI(AUTHORITY, DatabaseContract.TvShowFavorite.TABLE_NAME+"/#", TV_SHOWS_ID);
    }
//
//    private MovieProvider(){
//
//    }

    @Override
    public boolean onCreate() {
        movieHelper = MovieHelper.getInstance(getContext());
        movieHelper.openConnection();
        return true;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        Cursor cursor;
        switch (sUriMatcherMovies.match(uri)){
            case MOVIE:
                cursor = movieHelper.selectMovieFavorite();
                break;
            case MOVIE_ID:
                cursor = movieHelper.selectMovieFavoriteId(Integer.parseInt(uri.getLastPathSegment()));
                break;
            case TV_SHOWS:
                cursor = movieHelper.selectTvShowFavorite();
                break;
            case TV_SHOWS_ID:
                cursor = movieHelper.selectTvShowsFavoriteId(Integer.parseInt(uri.getLastPathSegment()));
                break;
            default:
                cursor = null;
                break;
        }
        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        long add;
        switch (sUriMatcherMovies.match(uri)){
            case MOVIE:
                add = movieHelper.inserMovie(values);
                break;
            case TV_SHOWS:
                add = movieHelper.insertTvShow(values);
                break;
            default:
                add = 0;
                break;
        }
        getContext().getContentResolver().notifyChange(CONTENT_URI_MOVIE,  null);
        return Uri.parse(CONTENT_URI_MOVIE+"/"+add);
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        int delete_;
        switch (sUriMatcherMovies.match(uri)){
            case MOVIE_ID:
                delete_ = movieHelper.deleteMovie(uri.getLastPathSegment());
                break;
            case TV_SHOWS_ID:
                delete_ = movieHelper.deleteTvShows(uri.getLastPathSegment());
                break;
            default:
                delete_ = 0;
                break;
        }
        getContext().getContentResolver().notifyChange(CONTENT_URI_MOVIE, null);
        return delete_;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }
}
