package io.github.azismihsan.moviefavorite.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MovieHelper {


    private static final String TABLE_MOVIE =  DatabaseContract.MovieFavorite.TABLE_NAME;
    private static final String TABLE_TVSHOW = DatabaseContract.TvShowFavorite.TABLE_NAME;
    private static DatabaseHelper databaseHelper;
    private static MovieHelper INSTANCE;
    private static SQLiteDatabase database;

    private MovieHelper(Context context){
        databaseHelper = new DatabaseHelper(context);
    }

    //inizialization db
    public static MovieHelper getInstance(Context context){
        if (INSTANCE == null){
            synchronized (SQLiteOpenHelper.class){
                if (INSTANCE == null){
                    INSTANCE = new MovieHelper(context);
                }
            }
        }
        return INSTANCE;
    }

    public void openConnection()throws SQLException {
        database = databaseHelper.getWritableDatabase();
    }

    public void closeConnection(){
        databaseHelper.close();
        if (database.isOpen()){
            database.close();
        }
    }

    //select * from table movie
    public Cursor selectMovieFavorite(){
        return database.query(TABLE_MOVIE, null, null, null, null, null,
                DatabaseContract.MovieFavorite._ID+" ASC ");
    }

    //select * from table tv shows
    public Cursor selectTvShowFavorite(){
        return database.query(TABLE_TVSHOW, null, null, null, null, null,
                DatabaseContract.TvShowFavorite._ID+" ASC ");
    }

    //select * from table movie by id
    public Cursor selectMovieFavoriteId(int id) {
        return database.query(TABLE_MOVIE, new String[]{DatabaseContract.MovieFavorite.COLOUMS_MOVIE_ID},
                DatabaseContract.MovieFavorite.COLOUMS_MOVIE_ID + " = ?", new String[]{String.valueOf(id)},
                null, null, null, null);
    }

    //select * from table tv shows by id
    public Cursor selectTvShowsFavoriteId(int id){
        return database.query(TABLE_TVSHOW, new String[]{DatabaseContract.TvShowFavorite.COLOUMS_MOVIE_ID},
                DatabaseContract.TvShowFavorite.COLOUMS_MOVIE_ID+" = ?", new String[]{String.valueOf(id)},
                null,null, null, null);
    }

    //insert into table movie
    public long inserMovie(ContentValues values){
        return database.insert(TABLE_MOVIE, null, values);
    }

    //insert into table tv show
    public long insertTvShow(ContentValues values){
        return  database.insert(TABLE_TVSHOW, null, values);
    }

    //delete data in table movie
    public int deleteMovie(String id){
        return database.delete(TABLE_MOVIE, DatabaseContract.MovieFavorite
                .COLOUMS_MOVIE_ID+" = ?", new String[]{id});
    }

    //delete data in table tv shows
    public int deleteTvShows(String id){
        return database.delete(TABLE_TVSHOW, DatabaseContract.TvShowFavorite
                .COLOUMS_MOVIE_ID+" = ?", new String[]{id});
    }
}

