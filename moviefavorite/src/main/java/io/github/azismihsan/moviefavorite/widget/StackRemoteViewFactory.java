package io.github.azismihsan.moviefavorite.widget;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.Binder;
import android.os.Bundle;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import io.github.azismihsan.moviefavorite.R;
import io.github.azismihsan.moviefavorite.database.MappingHelper;
import io.github.azismihsan.moviefavorite.model.MovieModel;

import static io.github.azismihsan.moviefavorite.database.DatabaseContract.MovieFavorite.CONTENT_URI_MOVIE;

public class StackRemoteViewFactory implements RemoteViewsService.RemoteViewsFactory {

    Cursor cursor;
    private final Context context;
    private static final String URL = "https://image.tmdb.org/t/p/w342";
    private List <MovieModel> movieModels = new ArrayList<>();

    public StackRemoteViewFactory(Context context1){
        context = context1;
    }

    @Override
    public void onCreate() {
        final long IdentityToken = Binder.clearCallingIdentity();
        cursor = context.getContentResolver().query(CONTENT_URI_MOVIE, null, null,
                null, null);
        Binder.restoreCallingIdentity(IdentityToken);
    }

    @Override
    public void onDataSetChanged() {
        movieModels.clear();
        movieModels.addAll(MappingHelper.mapCirsorSelectMovieShow(cursor));
    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        return movieModels.size();
    }

    @Override
    public RemoteViews getViewAt(int position) {
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget_items);
        Bitmap bitmap;
        try {
            bitmap = Glide.with(context)
                    .asBitmap()
                    .load(URL + movieModels.get(position).getPoster())
                    .apply(new RequestOptions().fitCenter())
                    .submit(850, 600)
                    .get();
            remoteViews.setImageViewBitmap(R.id.imageMovie, bitmap);
            remoteViews.setTextViewText(R.id.textBanner, movieModels.get(position).getTitle());
        } catch (InterruptedException | ExecutionException e){
            Log.d("Load Error", "Load Failed");
        }

        Bundle bundle = new Bundle();
        bundle.putInt(FavoriteMovieAppWidget.EXTRA_ITEMS, position);
        bundle.putString(FavoriteMovieAppWidget.EXTRA_TITLE, movieModels.get(position).getTitle());
        Intent intent3 = new Intent();
        intent3.putExtras(bundle);

        remoteViews.setOnClickFillInIntent(R.id.imageMovie, intent3);
        return remoteViews;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }
}
