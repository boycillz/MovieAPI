package io.github.azismihsan.moviefavorite.fragment;

import android.content.Context;
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

import io.github.azismihsan.moviefavorite.R;
import io.github.azismihsan.moviefavorite.adapter.TvFavoriteAdapter;
import io.github.azismihsan.moviefavorite.database.MappingHelper;
import io.github.azismihsan.moviefavorite.database.MovieHelper;
import io.github.azismihsan.moviefavorite.model.TvModel;

import static io.github.azismihsan.moviefavorite.database.DatabaseContract.TvShowFavorite.CONTENT_URI_TVSHOWS;

public class FragmentTvFavorite extends Fragment implements LoadTvCallBack {

    private static final String EXTRA_TV = "EXTRA_TV";
    ProgressBar progressBar;
    TvFavoriteAdapter tvFavoriteAdapter;
    MovieHelper movieHelper;
    RecyclerView rvTVShow;

    public FragmentTvFavorite() {
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
        rvTVShow = view.findViewById(R.id.rv_film);
        rvTVShow.setLayoutManager(new LinearLayoutManager(getContext()));
        rvTVShow.setHasFixedSize(true);
        tvFavoriteAdapter = new TvFavoriteAdapter(getActivity());
        rvTVShow.setAdapter(tvFavoriteAdapter);
        progressBar.setVisibility(View.VISIBLE);

        HandlerThread handlerThread = new HandlerThread("DataObserver");
        handlerThread.start();
        Handler handler = new Handler(handlerThread.getLooper());
        FragmentFilmFavorite.DataObserver observer = new FragmentFilmFavorite.DataObserver(handler, getContext());
        getContext().getContentResolver().registerContentObserver(CONTENT_URI_TVSHOWS, true, observer);

        if (savedInstanceState == null) {
            new LoadTvShow(getContext(), this).execute();
        } else {
            ArrayList<TvModel> models = savedInstanceState.getParcelableArrayList(EXTRA_TV);
            if (models != null) {
                tvFavoriteAdapter.setmTvShowModel(models);
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        movieHelper.closeConnection();
    }

    @Override
    public void preExecute() {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                progressBar.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    public void postExecute(ArrayList<TvModel> tvModel) {
        progressBar.setVisibility(View.GONE);
        if (tvModel.size() > 0) {
            tvFavoriteAdapter.setmTvShowModel(tvModel);
        } else {
            tvFavoriteAdapter.setmTvShowModel(new ArrayList<TvModel>());
        }
    }

    private static class LoadTvShow extends AsyncTask<Void, Void, ArrayList<TvModel>> {

        private final WeakReference<LoadTvCallBack> weakCallBack;
        private final WeakReference<Context> weakHelper;

        private LoadTvShow(Context context, LoadTvCallBack loadTvCallBack){
            weakCallBack = new WeakReference<>(loadTvCallBack);
            weakHelper = new WeakReference<>(context);
        }

        @Override
        protected void onPreExecute(){
            super.onPreExecute();
            weakCallBack.get().preExecute();
        }

        @Override
        protected ArrayList <TvModel> doInBackground(Void... voids){
            Context context = weakHelper.get();
            Cursor cursor = context.getContentResolver().query(CONTENT_URI_TVSHOWS, null,
                    null, null, null);
            return MappingHelper.mapCursorSelectTvShow(cursor);
        }

        @Override
        protected void onPostExecute(ArrayList <TvModel> tvModels){
            super.onPostExecute(tvModels);
            weakCallBack.get().postExecute(tvModels);
        }
    }

}

interface LoadTvCallBack{
    void preExecute();
    void postExecute(ArrayList <TvModel> tvModel);
}

