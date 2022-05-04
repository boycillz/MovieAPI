package io.github.azismihsan.moviefavorite.database;

import android.database.Cursor;

import java.util.ArrayList;

import io.github.azismihsan.moviefavorite.model.MovieModel;
import io.github.azismihsan.moviefavorite.model.TvModel;

public class MappingHelper {

    public static ArrayList<MovieModel> mapCursorMovieId(Cursor cursor){
        ArrayList<MovieModel> listMovie = new ArrayList<>();
        while (cursor.moveToNext()){
            int movieId = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseContract.MovieFavorite.COLOUMS_MOVIE_ID));
            String titleMovie = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.MovieFavorite.COLOUMS_TITLE_MOVIE));
            String overviewMovie = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.MovieFavorite.COLOUMS_OVERVIEW_MOVIE));
            String coverMovie = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.MovieFavorite.COLOUMS_POSTER));
            String coverBackground = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.MovieFavorite.COLOUMS_BACKGROUND));
            String dateRelease = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.MovieFavorite.COLOUMS_RELEASE_DATE));
            Float rating = cursor.getFloat(cursor.getColumnIndexOrThrow(DatabaseContract.MovieFavorite.COLOUMS_RATE));
            listMovie.add(new MovieModel(movieId, titleMovie, overviewMovie, coverMovie, coverBackground, dateRelease, rating));
        }
        return listMovie;
    }

    public static ArrayList<MovieModel> mapCirsorSelectMovieShow(Cursor cursor){
        ArrayList<MovieModel> movieModels = new ArrayList<>();
        while (cursor.moveToNext()){
            int movieId = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseContract.MovieFavorite.COLOUMS_MOVIE_ID));
            String titleMovie = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.MovieFavorite.COLOUMS_TITLE_MOVIE));
            String overviewMovie = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.MovieFavorite.COLOUMS_OVERVIEW_MOVIE));
            String coverMovie = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.MovieFavorite.COLOUMS_POSTER));
            String coverBackground = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.MovieFavorite.COLOUMS_BACKGROUND));
            String dateRelease = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.MovieFavorite.COLOUMS_RELEASE_DATE));
            float rating =  cursor.getFloat(cursor.getColumnIndexOrThrow(DatabaseContract.MovieFavorite.COLOUMS_RATE));
            movieModels.add(new MovieModel(movieId, titleMovie, overviewMovie, coverMovie, coverBackground, dateRelease, rating));
        }
        return movieModels;
    }

    public static ArrayList<TvModel> mapCursorSelectTvShow(Cursor cursor) {
        ArrayList<TvModel> tvModels = new ArrayList<>();
        while (cursor.moveToNext()){
            int movieId = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseContract.TvShowFavorite.COLOUMS_MOVIE_ID));
            String titleMovie = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.TvShowFavorite.COLOUMS_TITLE_MOVIE));
            String overviewMovie = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.TvShowFavorite.COLOUMS_OVERVIEW_MOVIE));
            String coverMovie = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.TvShowFavorite.COLOUMS_POSTER));
            String coverBackground = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.TvShowFavorite.COLOUMS_BACKGROUND));
            String dateRelease = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.TvShowFavorite.COLOUMS_RELEASE_DATE));
            float rating =  cursor.getFloat(cursor.getColumnIndexOrThrow(DatabaseContract.TvShowFavorite.COLOUMS_RATE));
            tvModels.add(new TvModel(movieId, titleMovie, overviewMovie, coverMovie, coverBackground, dateRelease, rating));
        }
        return tvModels;
    }

    public static MovieModel mapCursorMovietoObject (Cursor cursor){
        cursor.moveToFirst();
        int movieId = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseContract.MovieFavorite.COLOUMS_MOVIE_ID));
        String titleMovie = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.MovieFavorite.COLOUMS_TITLE_MOVIE));
        String overviewMovie = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.MovieFavorite.COLOUMS_OVERVIEW_MOVIE));
        String coverMovie = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.MovieFavorite.COLOUMS_POSTER));
        String coverBackground = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.MovieFavorite.COLOUMS_BACKGROUND));
        String releaseDate = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.MovieFavorite.COLOUMS_RELEASE_DATE));
        float rating = cursor.getFloat(cursor.getColumnIndexOrThrow(DatabaseContract.MovieFavorite.COLOUMS_RATE));

        return new MovieModel(movieId, titleMovie, overviewMovie, coverMovie, coverBackground, releaseDate, rating);
    }
}