package io.github.azismihsan.movieapi.model;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;
import java.util.List;

import io.github.azismihsan.movieapi.BuildConfig;
import io.github.azismihsan.movieapi.R;
import io.github.azismihsan.movieapi.apihelper.BaseApiService;
import io.github.azismihsan.movieapi.apihelper.UtilsApi;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MovieRepository {

    private ArrayList<MovieModel> movies = new ArrayList<>();//Array for Movies
    private ArrayList<TvModel> tv = new ArrayList<>();//Array for TV
    private ArrayList<MovieModel> searchMovie = new ArrayList<>();

    private MutableLiveData<List<MovieModel>> mutableLiveData = new MutableLiveData<>();//MutableLiveData for Movies
    private MutableLiveData<List<TvModel>> mutableTv = new MutableLiveData<>();//MutableLiveData for TV
    private MutableLiveData<List<MovieModel>> mutableSearchMovie = new MutableLiveData<>();

    private Application application;
    private static final String LANG = "en-US";
    private static final String API_KEY = BuildConfig.TMDB_API_KEY;
    private static final int PAGE = 1;

    public MovieRepository(Application app){
        this.application = app;
    }

    public MutableLiveData<List<MovieModel>> getMutableLiveData(){

        BaseApiService apiService = UtilsApi.getAPIService();
        Call<MovieResponse> call = apiService.getMovie(BuildConfig.TMDB_API_KEY ,LANG,PAGE);

        call.enqueue(new Callback<MovieResponse>() {
            @Override
            public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
                if (response.isSuccessful()){
                    MovieResponse movieResponse = response.body();
                    if (movieResponse !=null && movieResponse.getMovies() !=null){
                        movies = movieResponse.getMovies();
                        mutableLiveData.setValue(movies);
                    }
                } else {
                    Log.d("Error", String.valueOf(R.string.failed_connect_api));
                }
            }

            @Override
            public void onFailure(Call<MovieResponse> call, Throwable t) {
                Log.d("Error", String.valueOf(R.string.no_connection_internet));
            }
        });
        return mutableLiveData;
    }

    public MutableLiveData<List<TvModel>> getMutableTv(){

        BaseApiService apiService = UtilsApi.getAPIService();
        Call<TvResponse> callTv = apiService.getTv(BuildConfig.TMDB_API_KEY,LANG,PAGE);

        callTv.enqueue(new Callback<TvResponse>() {
            @Override
            public void onResponse(Call<TvResponse> call, Response<TvResponse> response) {
                if (response.isSuccessful()){
                    TvResponse tvResponse = response.body();
                    if (tvResponse != null & tvResponse.getTvModels() != null){
                        tv = tvResponse.getTvModels();
                        mutableTv.setValue(tv);
                    }
                } else {
                    Log.d("Error", String.valueOf(R.string.failed_connect_api));
                }
            }

            @Override
            public void onFailure(Call<TvResponse> call, Throwable t) {
                Log.d("Error", String.valueOf(R.string.no_connection_internet));
            }
        });

        return mutableTv;
    }

    public MutableLiveData <List<MovieModel>> getSearchMovie(String query){

        BaseApiService apiService = UtilsApi.getAPIService();
        Call <MovieResponse> callSearchMovie = apiService.getMovieFilter(API_KEY, LANG, query);

        callSearchMovie.enqueue(new Callback<MovieResponse>() {
            @Override
            public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
                if (response.isSuccessful()){
                    MovieResponse responeMovie = response.body();
                    if (responeMovie != null && responeMovie.getMovies() != null){
                        searchMovie = (ArrayList<MovieModel>) responeMovie.getMovies();
                        mutableSearchMovie.setValue(searchMovie);
                    }
                } else {
                    Log.d("Error", String.valueOf(R.string.failed_connect_api));
                }
            }

            @Override
            public void onFailure(Call<MovieResponse> call, Throwable t) {
                Log.d("Error", String.valueOf(R.string.no_connection_internet));
            }
        });
    return mutableSearchMovie;
    }
}
