package io.github.azismihsan.movieapi.apihelper;


public class UtilsApi {

    private static final String BASE_URL ="https://api.themoviedb.org/3/";

    public static BaseApiService getAPIService(){
        return RetrofitClient.getClient(BASE_URL).create(BaseApiService.class);
    }
}
