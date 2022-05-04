package io.github.azismihsan.moviefavorite.adapter;

import android.app.Activity;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import io.github.azismihsan.moviefavorite.R;
import io.github.azismihsan.moviefavorite.database.MovieHelper;
import io.github.azismihsan.moviefavorite.model.MovieModel;

import static io.github.azismihsan.moviefavorite.database.DatabaseContract.MovieFavorite.CONTENT_URI_MOVIE;

public class MovieFavoriteAdapter extends RecyclerView.Adapter<MovieFavoriteAdapter.MovieFavoriteHolder> {

    private ArrayList<MovieModel> movieModels = new ArrayList<>();
    private static final String URL ="https://image.tmdb.org/t/p/w342";
    private Activity activity;
    MovieHelper helper;


    public MovieFavoriteAdapter(Activity activity){
        this.activity = activity;
    }

    public ArrayList<MovieModel> getMovieModels(){
        return movieModels;
    }

    public void setMovieModels(ArrayList <MovieModel> list){
        if (movieModels.size() > 0){
            this.movieModels.clear();
        }
        this.movieModels.addAll(list);
        notifyDataSetChanged();
    }

    public void addMovie(MovieModel movieModel){
        this.movieModels.add(movieModel);
        notifyItemInserted(movieModels.size() -1 );
    }

    public void updateMovie(int position, MovieModel movieModel){
        this.movieModels.set(position, movieModel);
        notifyItemChanged(position, movieModel);
    }

    public void removeMovie(int position){
        this.movieModels.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, movieModels.size());
    }

    @NonNull
    @Override
    public MovieFavoriteAdapter.MovieFavoriteHolder onCreateViewHolder
            (@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_items_favorite, parent, false);
        return new MovieFavoriteHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieFavoriteAdapter
            .MovieFavoriteHolder holder, int position) {
        holder.tvTitle.setText(movieModels.get(position).getTitle());
        holder.tvOverview.setText(movieModels.get(position).getOverview());
        Glide.with(holder.itemView.getContext()).load(URL+movieModels
                .get(position).getPoster()).into(holder.imgCover);
    }

    @Override
    public int getItemCount() {
        return movieModels.size();
    }

    public class MovieFavoriteHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        final TextView tvTitle, tvOverview;
        final ImageView imgCover;
        final Button btnUnFavorite;

        public MovieFavoriteHolder(@NonNull View itemView){
            super(itemView);

            tvTitle = itemView.findViewById(R.id.tv_title);
            tvOverview = itemView.findViewById(R.id.tv_overview);
            imgCover = itemView.findViewById(R.id.img_cover);
            btnUnFavorite = itemView.findViewById(R.id.btn_unfavorite);
            btnUnFavorite.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            MovieModel movieModel = movieModels.get(position);

            switch (v.getId()){
                case R.id.btn_unfavorite:

                    Uri uriDelete = Uri.parse(CONTENT_URI_MOVIE+" / "+movieModel.getId());
                    helper = MovieHelper.getInstance(v.getContext());
                    helper.openConnection();

                    int result = v.getContext().getContentResolver().delete(uriDelete,
                            null, null);
                    if (result > 0){
                        MovieFavoriteAdapter.this.removeMovie(position);
                        Toast.makeText(v.getContext(), String.valueOf(v.getResources()
                                        .getString(R.string.delete_)),
                                Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(v.getContext(), String.valueOf(v.getResources()
                                        .getString(R.string.cancel_delete_)),
                                Toast.LENGTH_SHORT).show();//baru sampai sini
                    }
                    break;
            }
        }
    }
}
