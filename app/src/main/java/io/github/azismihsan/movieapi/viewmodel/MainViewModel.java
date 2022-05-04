package io.github.azismihsan.movieapi.viewmodel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

import io.github.azismihsan.movieapi.model.MovieModel;
import io.github.azismihsan.movieapi.model.MovieRepository;
import io.github.azismihsan.movieapi.model.TvModel;

public class MainViewModel extends AndroidViewModel {

    private MovieRepository movieRepository;

    public MainViewModel(Application app){
        super(app);
        movieRepository = new MovieRepository(app);
    }
    public LiveData<List<MovieModel>> getAllMovie(){
        return movieRepository.getMutableLiveData();
    }

    public LiveData<List<TvModel>> getAllTv(){
        return movieRepository.getMutableTv();
    }

    public LiveData<List<MovieModel>> getSearchMovie(String query){
        return movieRepository.getSearchMovie(query);
    }
}
