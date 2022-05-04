package io.github.azismihsan.movieapi.activity;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.snackbar.Snackbar;

import io.github.azismihsan.movieapi.R;
import io.github.azismihsan.movieapi.database.MovieHelper;
import io.github.azismihsan.movieapi.model.TvModel;

import static io.github.azismihsan.movieapi.database.DatabaseContract.TvShowFavorite.COLOUMS_BACKGROUND;
import static io.github.azismihsan.movieapi.database.DatabaseContract.TvShowFavorite.COLOUMS_MOVIE_ID;
import static io.github.azismihsan.movieapi.database.DatabaseContract.TvShowFavorite.COLOUMS_OVERVIEW_MOVIE;
import static io.github.azismihsan.movieapi.database.DatabaseContract.TvShowFavorite.COLOUMS_POSTER;
import static io.github.azismihsan.movieapi.database.DatabaseContract.TvShowFavorite.COLOUMS_RATE;
import static io.github.azismihsan.movieapi.database.DatabaseContract.TvShowFavorite.COLOUMS_RELEASE_DATE;
import static io.github.azismihsan.movieapi.database.DatabaseContract.TvShowFavorite.COLOUMS_TITLE_MOVIE;
import static io.github.azismihsan.movieapi.database.DatabaseContract.TvShowFavorite.CONTENT_URI_TVSHOWS;

public class DetailTvActivity extends AppCompatActivity implements View.OnClickListener {

    public static final String EXTRA_FILM = "extra_film";
    private static final String URL = "https://image.tmdb.org/t/p/w342";
    private Toolbar toolbar_;
    TextView txtTitleMovie,txtReleaseDate,txtRating,txtOverview;
    ImageView imgHeader,imgCover;
    TvModel tvModel;
    MovieHelper helper;
    Button btnFavorite_,btnUnFavorite_;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_tv);

        helper = MovieHelper.getInstance(getApplicationContext());
        helper.openConnection();

        initView();
    }

    private void initView(){
        txtTitleMovie = findViewById(R.id.txt_title);
        txtReleaseDate = findViewById(R.id.txt_realese);
        txtRating = findViewById(R.id.txt_rating);
        txtOverview = findViewById(R.id.desc_movie);
        imgCover = findViewById(R.id.img_covermovie);
        imgHeader = findViewById(R.id.bg_background);
        tvModel = getIntent().getParcelableExtra(EXTRA_FILM);

        toolbar_ = findViewById(R.id.toolbar);
        toolbar_.setNavigationIcon(R.drawable.ic_arrow_black);
        toolbar_.setTitle(R.string.app_name);
        setSupportActionBar(toolbar_);

        Cursor count = helper.selectTvShowsFavoriteId(tvModel.getId());
//        int count = helper.selectTvShowsFavoriteId(tvModel.getId());
        btnFavorite_ = findViewById(R.id.btn_favorite);
        btnUnFavorite_ = findViewById(R.id.btn_unfavorite);
        if (count.getCount() > 0){
            btnFavorite_.setVisibility(View.GONE);
            btnUnFavorite_.setVisibility(View.VISIBLE);

            btnUnFavorite_.setOnClickListener(this);
        }else{
            btnFavorite_.setVisibility(View.VISIBLE);
            btnUnFavorite_.setVisibility(View.GONE);

            btnFavorite_.setOnClickListener(this);
        }

        toolbar_.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        CollapsingToolbarLayout collapsingToolbarLayout = findViewById(R.id.collapsing_toolbar);
        collapsingToolbarLayout.setTitle(tvModel.getTitle());
        collapsingToolbarLayout.setCollapsedTitleTextColor
                (ContextCompat.getColor(getApplicationContext(), R.color.white));
        collapsingToolbarLayout.setExpandedTitleColor
                (ContextCompat.getColor(getApplicationContext(), R.color.white));

        txtTitleMovie.setText(tvModel.getTitle());
        txtOverview.setText(tvModel.getOverview());
        txtReleaseDate.setText(tvModel.getDateRelease());
        txtRating.setText(String.valueOf(tvModel.getRateTv()));
        Glide.with(getApplicationContext()).load(URL+tvModel.getCover()).into(imgCover);
        Glide.with(getApplicationContext()).load(URL+tvModel.getBgTv()).into(imgHeader);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_favorite:
                ContentValues contentValues = new ContentValues();
                contentValues.put(COLOUMS_MOVIE_ID, tvModel.getId());
                contentValues.put(COLOUMS_TITLE_MOVIE, tvModel.getTitle());
                contentValues.put(COLOUMS_OVERVIEW_MOVIE, tvModel.getOverview());
                contentValues.put(COLOUMS_RATE, tvModel.getRateTv());
                contentValues.put(COLOUMS_RELEASE_DATE, tvModel.getDateRelease());
                contentValues.put(COLOUMS_POSTER, tvModel.getCover());
                contentValues.put(COLOUMS_BACKGROUND, tvModel.getBgTv());

                Uri uri = getContentResolver().insert(CONTENT_URI_TVSHOWS, contentValues);
                Snackbar.make(v, R.string.succes_, Snackbar.LENGTH_SHORT).show();
                btnUnFavorite_.setVisibility(View.VISIBLE);
                btnFavorite_.setVisibility(View.GONE);
                Log.d("TV Shows", "uri:"+uri);
                break;

            case R.id.btn_unfavorite:
                Uri uri1 = Uri.parse(CONTENT_URI_TVSHOWS+"/"+tvModel.getId());
                getContentResolver().delete(uri1, null, null);
                Snackbar.make(v, R.string.succes_, Snackbar.LENGTH_SHORT).show();
                btnUnFavorite_.setVisibility(View.GONE);
                btnFavorite_.setVisibility(View.VISIBLE);
                break;
        }

    }
}
