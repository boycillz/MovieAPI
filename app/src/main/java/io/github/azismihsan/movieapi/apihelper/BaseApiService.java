package io.github.azismihsan.movieapi.apihelper;

import io.github.azismihsan.movieapi.model.MovieResponse;
import io.github.azismihsan.movieapi.model.TvResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface BaseApiService {

    @GET("movie/now_playing")
    Call<MovieResponse> getMovie(
            @Query("api_key") String apiKey,
            @Query("language") String language,
            @Query("page") int page
    );

    @GET("tv/on_the_air")
    Call<TvResponse> getTv(
      @Query("api_key") String apiKey,
      @Query("language") String language,
      @Query("page") int page
    );

    @GET("discover/movie")
    Call<MovieResponse> getReleaseMovie(
            @Query("api_key") String apiKey,
            @Query("primary_release_date.gte") String today,
            @Query("primary_release_date.lte") String today1
    );

    @GET("search/movie")
    Call<MovieResponse> getMovieFilter(
            @Query("api_key") String apiKey,
            @Query("language") String language,
            @Query("query") String query
    );
}
