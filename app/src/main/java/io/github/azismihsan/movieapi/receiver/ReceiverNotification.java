package io.github.azismihsan.movieapi.receiver;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import java.text.SimpleDateFormat;
import java.text.ParseException;
import java.text.DateFormat;
import java.util.Calendar;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import io.github.azismihsan.movieapi.BuildConfig;
import io.github.azismihsan.movieapi.R;
import io.github.azismihsan.movieapi.apihelper.BaseApiService;
import io.github.azismihsan.movieapi.apihelper.UtilsApi;
import io.github.azismihsan.movieapi.model.MovieModel;
import io.github.azismihsan.movieapi.model.MovieResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReceiverNotification extends BroadcastReceiver {

    public static final String TYPE_DAILY_REMINDER_NOTIFICATION = "Daily Reminder";
    public static final String TYPE_RELEASE_REMINDER_NOTIFICATION = "Release Reminder";

    public static final int ID_DAILY_REMINDER = 100;
    public static final int ID_RELEASE_REMINDER = 101;

    private static final String EXTRA_MESSAGE = "Message";
    private static final String EXTRA_TYPE = "Type";
    private final static String DATE_FORMAT = "yyyy-MM-dd";
    private final static String TIME_FORMAT = "HH:mm";

    @Override
    public void onReceive(final Context context, Intent intent) {
        if (intent.getStringExtra(EXTRA_TYPE).equals("Daily Reminder")){
            String type = intent.getStringExtra(EXTRA_TYPE);
            String Message = intent.getStringExtra(EXTRA_MESSAGE);
            String Title = type;
            int NotificationId = ID_DAILY_REMINDER;

            ShowNotification(context, Title, Message, NotificationId);

        }else {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            Date date = new Date();
            final String today = dateFormat.format(date);

            BaseApiService apiService = UtilsApi.getAPIService();
            Call<MovieResponse> call = apiService.getReleaseMovie(BuildConfig.TMDB_API_KEY, today, today);

            call.enqueue(new Callback<MovieResponse>() {
                @Override
                public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
                    if (response.isSuccessful()){
                        ArrayList<MovieModel> movieModels = response.body().getMovies();
                        int id = ID_RELEASE_REMINDER;
                        for (MovieModel movie : movieModels){
                            String title = movie.getTitle();
                            String message = title + " " + context.getString(R.string.content_release);
                            todayReminderNotification(context, title, message, id);
                            id++;
                        }
                    }
                }

                @Override
                public void onFailure(Call<MovieResponse> call, Throwable t) {

                }
            });
        }
    }

    private void ShowNotification (Context context, String Title, String Message, int notificationId){
        String CHANNEL_ID = "DAILY_NOTIFICATION";
        String CHANNEL_NAME = "Daily Natification";

        NotificationManager notificationManager = (NotificationManager)context
                .getSystemService(Context.NOTIFICATION_SERVICE);
        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_movie_name)
                .setContentTitle(Title)
                .setContentText(Message)
                .setColor(ContextCompat.getColor(context, android.R.color.transparent))
                .setVibrate(new long[]{1000, 1000, 1000, 1000, 1000})
                .setSound(alarmSound);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel notificationChannel = new NotificationChannel
                    (CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT);
            notificationChannel.enableVibration(true);
            notificationChannel.setVibrationPattern(new long[]{1000, 1000, 1000, 1000, 1000});

            builder.setChannelId(CHANNEL_ID);
            if (notificationManager != null){
                notificationManager.createNotificationChannel(notificationChannel);
            }
        }
        Notification notification = builder.build();
        if (notificationManager != null) {
            notificationManager.notify(notificationId, notification);
        }
    }

    private void todayReminderNotification(Context context, String Title, String Message, int NotificationId){
        String CHANNEL_ID = "REMINDER_NOTIFICATION";
        String CHANNEL_NAME = "Today Reminder Notification";

        NotificationManager notificationManager = (NotificationManager)context
                .getSystemService(Context.NOTIFICATION_SERVICE);
        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_movie_name)
                .setContentTitle(Title)
                .setContentText(Message)
                .setColor(ContextCompat.getColor(context, android.R.color.transparent))
                .setVibrate(new long[]{1000, 1000, 1000, 1000, 1000})
                .setSound(alarmSound);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel notificationChannel = new NotificationChannel
                    (CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT);
            notificationChannel.enableVibration(true);
            notificationChannel.setVibrationPattern(new long[]{1000, 1000, 1000, 1000, 1000});

            builder.setChannelId(CHANNEL_ID);
            if (notificationManager != null){
                notificationManager.createNotificationChannel(notificationChannel);
            }
        }
        Notification notification = builder.build();
        if (notificationManager != null){
            notificationManager.notify(NotificationId, notification);
        }
    }

    public void SettingDailyReminder(Context context, String Type, String Time, String Message){
        if (DateInvalid(Time, TIME_FORMAT)) return;

        AlarmManager alarmManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, ReceiverNotification.class);
        intent.putExtra(EXTRA_MESSAGE, Message);
        intent.putExtra(EXTRA_TYPE, Type);

        String[] timeArrays = Time.split(":");

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(timeArrays[0]));
        calendar.set(Calendar.MINUTE, Integer.parseInt(timeArrays[1]));
        calendar.set(Calendar.SECOND, 0);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, ID_DAILY_REMINDER, intent, 0);
        if (alarmManager != null){
            alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                    AlarmManager.INTERVAL_DAY, pendingIntent);
        }
        Toast.makeText(context, R.string.notification_running, Toast. LENGTH_SHORT).show();

    }

    public void SettingReleaseReminder(Context context, String Type, String Time){
        if (DateInvalid(Time, TIME_FORMAT)) return;

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, ReceiverNotification.class);
        intent.putExtra(EXTRA_TYPE, Type);

        String[] timeArrays = Time.split(":");

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(timeArrays[0]));
        calendar.set(Calendar.MINUTE, Integer.parseInt(timeArrays[1]));
        calendar.set(Calendar.SECOND, 0);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, ID_RELEASE_REMINDER,  intent,0);
        if (alarmManager != null){
            alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
            AlarmManager.INTERVAL_DAY, pendingIntent);
        }
        Toast.makeText(context, R.string.notification_running, Toast.LENGTH_SHORT).show();
    }

    public void cancelReminder(Context context, String Type, int Id){
        AlarmManager alarmManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, ReceiverNotification.class);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, Id, intent, 0);
        pendingIntent.cancel();

        if (alarmManager != null){
            alarmManager.cancel(pendingIntent);
        }
        Toast.makeText(context, R.string.notification_stop, Toast.LENGTH_SHORT).show();
    }

    private boolean DateInvalid(String time, String timeFormat) {
        try {
            DateFormat DF = new SimpleDateFormat(timeFormat, Locale.getDefault());
            DF.setLenient(false);
            DF.parse(time);
            return false;
        } catch (ParseException a){
            return true;
        }
    }

}
