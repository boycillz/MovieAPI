package io.github.azismihsan.moviefavorite.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.RemoteViews;
import android.widget.Toast;

import io.github.azismihsan.moviefavorite.R;

public class FavoriteMovieAppWidget extends AppWidgetProvider {

    private static final String TOAST_ACTION = "io.github.azismihsan.fav.TOAST_ACTION";
    public static final String EXTRA_ITEMS = "io.github.azismihsan.fav.EXTRA_ITEM";
    public static final String EXTRA_TITLE = "Title";

    static void updateAppWidget(Context context, AppWidgetManager widgetManager, int AppWidgetId) {

        Intent intent = new Intent(context, StackWidgetService.class);
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetId);
        intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));

        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.favorite_movie_app_widget);
        remoteViews.setRemoteAdapter(R.id.stackView, intent);
        remoteViews.setEmptyView(R.id.stackView, R.id.emptyView);

        Intent intent1 = new Intent(context, FavoriteMovieAppWidget.class);
        intent1.setAction(FavoriteMovieAppWidget.TOAST_ACTION);
        intent1.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetId);
        intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0,
                intent1, PendingIntent.FLAG_UPDATE_CURRENT);
        remoteViews.setPendingIntentTemplate(R.id.stackView, pendingIntent);

        widgetManager.updateAppWidget(AppWidgetId, remoteViews);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for (int AppWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, AppWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
    }

    @Override
    public void onDisabled(Context context) {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        if (intent.getAction() != null) {
            if (intent.getAction().equals(TOAST_ACTION)) {
                String Title = intent.getStringExtra(EXTRA_TITLE);
                Toast.makeText(context, Title, Toast.LENGTH_SHORT).show();
            }
        }
    }
}