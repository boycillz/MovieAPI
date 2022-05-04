package io.github.azismihsan.movieapi.fragment;

import android.content.Context;
import android.database.ContentObserver;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

import io.github.azismihsan.movieapi.R;
import io.github.azismihsan.movieapi.adapter.MovieFavoriteAdapter;
import io.github.azismihsan.movieapi.database.MappingHelper;
import io.github.azismihsan.movieapi.database.MovieHelper;
import io.github.azismihsan.movieapi.model.MovieModel;

import static io.github.azismihsan.movieapi.database.DatabaseContract.MovieFavorite.CONTENT_URI_MOVIE;

public class FragmentFilmFavorite extends Fragment implements LoadMovieCallBack {

    private static final String EXTRA_STATE = "EXTRA_STATE";
    MovieFavoriteAdapter movieFavoriteAdapter;
    ProgressBar progressBar;
    MovieHelper movieHelper;
    RecyclerView recyclerView;

    public FragmentFilmFavorite(){

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_favorite, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        movieHelper = MovieHelper.getInstance(getActivity());
        movieHelper.openConnection();
        progressBar = view.findViewById(R.id.progress_bar);
        recyclerView = view.findViewById(R.id.rv_film);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true);
        movieFavoriteAdapter = new MovieFavoriteAdapter(getActivity());
        recyclerView.setAdapter(movieFavoriteAdapter);
        progressBar.setVisibility(View.VISIBLE);

        HandlerThread handlerThread = new HandlerThread("DataObserver");
        handlerThread.start();
        Handler handler = new Handler(handlerThread.getLooper());

        DataObserver observer = new DataObserver(handler, getContext());
        getContext().getContentResolver().registerContentObserver
                (CONTENT_URI_MOVIE, true, observer);

        //saving data when screen rotation
        if (savedInstanceState == null){
            new LoadMovieData(getContext(), this).execute();
        } else {
            ArrayList <MovieModel> models = savedInstanceState.getParcelableArrayList(EXTRA_STATE);
            if (models != null){
                movieFavoriteAdapter.setMovieModels(models);
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        movieHelper.closeConnection();
    }

    @Override
    public void preExecute(){
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                progressBar.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    public void postExecute(ArrayList<MovieModel> movies) {
        progressBar.setVisibility(View.GONE);
        if (movies.size() > 0){
            movieFavoriteAdapter.setMovieModels(movies);
        }else {
            movieFavoriteAdapter.setMovieModels(new ArrayList<MovieModel>());
        }
    }

    private static class LoadMovieData extends AsyncTask<Void, Void, ArrayList <MovieModel>>{

        private final WeakReference<LoadMovieCallBack> weakCallBack;
        private final WeakReference<Context> weakContext;

        private LoadMovieData(Context context, LoadMovieCallBack weakCallBack){
            this.weakCallBack = new WeakReference<>(weakCallBack);
            this.weakContext = new WeakReference<>(context);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            weakCallBack.get().preExecute();
        }


        @Override
        protected ArrayList<MovieModel> doInBackground(Void... voids) {
            Context context = weakContext.get();
            Cursor cursor1 = context.getContentResolver().query(CONTENT_URI_MOVIE, null,
                    null, null, null);
            return MappingHelper.mapCirsorSelectMovieShow(cursor1);
        }

        @Override
        protected void onPostExecute(ArrayList<MovieModel> movieModels) {
            super.onPostExecute(movieModels);
            weakCallBack.get().postExecute(movieModels);
        }

    }

    public static class DataObserver extends ContentObserver{
        final Context context;

        public DataObserver(Handler handler, Context context){
            super(handler);
            this.context = context;
        }
    }
}

interface LoadMovieCallBack{
    void preExecute();
    void postExecute(ArrayList<MovieModel> movies);
}