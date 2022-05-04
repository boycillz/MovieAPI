package io.github.azismihsan.movieapi.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TvModel implements Parcelable {


    @SerializedName("id")
    @Expose
    private int id;

    @SerializedName("name")
    @Expose
    private String title;

    @SerializedName("overview")
    @Expose
    private String overview;

    @SerializedName("poster_path")
    @Expose
    private String cover;

    @SerializedName("backdrop_path")
    @Expose
    private String bgTv;

    @SerializedName("vote_average")
    @Expose
    private float rateTv;

    @SerializedName("first_air_date")
    @Expose
    private String dateRelease;

    public TvModel(int tvId) {
        this.id=tvId;
    }

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

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public String getBgTv() {
        return bgTv;
    }

    public void setBgTv(String bgTv) {
        this.bgTv = bgTv;
    }

    public float getRateTv() {
        return rateTv;
    }

    public void setRateTv(float rateTv) {
        this.rateTv = rateTv;
    }

    public String getDateRelease() {
        return dateRelease;
    }

    public void setDateRelease(String dateRelease) {
        this.dateRelease = dateRelease;
    }

    public TvModel(int movieId, String titleMovie, String titleOverview, String coverMovie,
                   String coverBg, String dateRelease, float rating){
        this.id = movieId;
        this.title = titleMovie;
        this.overview = titleOverview;
        this.cover = coverMovie;
        this.bgTv = coverBg;
        this.rateTv = rating;
        this.dateRelease = dateRelease;
    }

    protected TvModel(Parcel in) {
        this.id = in.readInt();
        this.title = in.readString();
        this.overview = in.readString();
        this.cover = in.readString();
        this.bgTv = in.readString();
        this.rateTv = in.readFloat();
        this.dateRelease = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.title);
        dest.writeString(this.overview);
        dest.writeString(this.cover);
        dest.writeString(this.bgTv);
        dest.writeFloat(this.rateTv);
        dest.writeString(this.dateRelease);
    }

    public static final Creator<TvModel> CREATOR = new Creator<TvModel>() {
        @Override
        public TvModel createFromParcel(Parcel in) {
            return new TvModel(in);
        }

        @Override
        public TvModel[] newArray(int size) {
            return new TvModel[size];
        }
    };
}
