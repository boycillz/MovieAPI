package io.github.azismihsan.moviefavorite.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MovieModel implements Parcelable {


    @SerializedName("id")
    @Expose
    private int id;

    @SerializedName("title")
    @Expose
    private String title;

    @SerializedName("overview")
    @Expose
    private String overview;

    @SerializedName("release_date")
    @Expose
    private String dateRelease;

    @SerializedName("poster_path")
    @Expose
    private String poster;

    @SerializedName("backdrop_path")
    @Expose
    private String BgMovie;

    @SerializedName("vote_average")
    @Expose
    private float rating;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getDateRelease() {
        return dateRelease;
    }

    public void setDateRelease(String dateRelease) {
        this.dateRelease = dateRelease;
    }

    public String getPoster() {
        return poster;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }

    public String getBgMovie() {
        return BgMovie;
    }

    public void setBgMovie(String bgMovie) {
        BgMovie = bgMovie;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(Float rating) {
        this.rating = rating;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public MovieModel(int movieId, String titleMovie, String overviewMovie, String coverMovie,
                      String coverBackground, String dateRelease, Float rating) {
        this.id = movieId;
        this.title = titleMovie;
        this.overview = overviewMovie;
        this.poster = coverMovie;
        this.BgMovie = coverBackground;
        this.rating = rating;
        this.dateRelease = dateRelease;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.title);
        dest.writeString(this.overview);
        dest.writeString(this.dateRelease);
        dest.writeString(this.poster);
        dest.writeString(this.BgMovie);
        dest.writeFloat(this.rating);
    }


    public MovieModel(Parcel in){
        this.id = in.readInt();
        this.title = in.readString();
        this.overview = in.readString();
        this.dateRelease = in.readString();
        this.poster = in.readString();
        this.BgMovie = in.readString();
        this.rating = in.readFloat();
    }

    public static final Creator<MovieModel> CREATOR = new Creator<MovieModel>() {
        @Override
        public MovieModel createFromParcel(Parcel in) {
            return new MovieModel(in);
        }

        @Override
        public MovieModel[] newArray(int size) {
            return new MovieModel[size];
        }
    };
}