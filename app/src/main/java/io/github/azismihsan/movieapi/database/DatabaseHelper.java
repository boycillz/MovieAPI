package io.github.azismihsan.movieapi.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "dbmovies.db";
    private static final int DATABASE_VERSION = 1;

    private static final String SQL_CREATE_TABLE_MOVIE = String.format(
            "CREATE TABLE %s"+
                    "("+
                    "%s INTEGER PRIMARY KEY AUTOINCREMENT,"+
                    " %s INTEGER,"+
                    " %s TEXT NOT NULL,"+
                    " %s TEXT,"+
                    " %s TEXT,"+
                    " %s REAL,"+
                    " %s TEXT,"+
                    " %s TEXT"+
                    ")",DatabaseContract.MovieFavorite.TABLE_NAME,
            DatabaseContract.MovieFavorite._ID,
            DatabaseContract.MovieFavorite.COLOUMS_MOVIE_ID,
            DatabaseContract.MovieFavorite.COLOUMS_TITLE_MOVIE,
            DatabaseContract.MovieFavorite.COLOUMS_OVERVIEW_MOVIE,
            DatabaseContract.MovieFavorite.COLOUMS_RELEASE_DATE,
            DatabaseContract.MovieFavorite.COLOUMS_RATE,
            DatabaseContract.MovieFavorite.COLOUMS_POSTER,
            DatabaseContract.MovieFavorite.COLOUMS_BACKGROUND
    );

    private static final String SQL_CREATE_TABLE_TVSHOW = String.format(
            "CREATE TABLE %s"+"("+
                    "%s INTEGER PRIMARY KEY AUTOINCREMENT,"+
                    " %s INTEGER,"+
                    " %s TEXT NOT NULL,"+
                    " %s TEXT,"+
                    " %s TEXT,"+
                    " %s REAL,"+
                    " %s TEXT,"+
                    " %s TEXT"+
                    ")",DatabaseContract.TvShowFavorite.TABLE_NAME,
            DatabaseContract.TvShowFavorite._ID,
            DatabaseContract.TvShowFavorite.COLOUMS_MOVIE_ID,
            DatabaseContract.TvShowFavorite.COLOUMS_TITLE_MOVIE,
            DatabaseContract.TvShowFavorite.COLOUMS_OVERVIEW_MOVIE,
            DatabaseContract.TvShowFavorite.COLOUMS_RELEASE_DATE,
            DatabaseContract.TvShowFavorite.COLOUMS_RATE,
            DatabaseContract.TvShowFavorite.COLOUMS_POSTER,
            DatabaseContract.TvShowFavorite.COLOUMS_BACKGROUND
    );


    public DatabaseHelper(Context context){
        super(context, DATABASE_NAME, null,DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_TABLE_MOVIE);
        db.execSQL(SQL_CREATE_TABLE_TVSHOW);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS"+DatabaseContract.MovieFavorite.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS"+DatabaseContract.TvShowFavorite.TABLE_NAME);
        onCreate(db);
    }

}
