package io.github.azismihsan.movieapi.adapter;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

import io.github.azismihsan.movieapi.R;
import io.github.azismihsan.movieapi.activity.DetailMovieActivity;
import io.github.azismihsan.movieapi.model.MovieModel;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MainViewHolder> {


    private List<MovieModel> mDataMovie;
    private static final String URL ="https://image.tmdb.org/t/p/w342";

    public MovieAdapter(List<MovieModel> listMovie){
        this.mDataMovie = listMovie;
    }

    @NonNull
    @Override
    public MovieAdapter.MainViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_items, parent, false);
        return new MainViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieAdapter.MainViewHolder holder, int position) {
        holder.bind(mDataMovie.get(position));
    }

    @Override
    public int getItemCount() {
        return mDataMovie.size();
    }

    public class MainViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView titleMovie,overviewMovie;
        ImageView cover;

        private MainViewHolder(@NonNull View itemView){
            super(itemView);

            titleMovie = itemView.findViewById(R.id.tv_title);
            overviewMovie = itemView.findViewById(R.id.tv_overview);
            cover = itemView.findViewById(R.id.img_cover);

            itemView.setOnClickListener(this);
        }

        private void bind (MovieModel movieModel){
            titleMovie.setText(movieModel.getTitle());
            overviewMovie.setText(movieModel.getOverview());
            Glide.with(itemView).load(URL+movieModel.getPoster()).into(cover);
        }

        public void onClick(View view){
            int position = getAdapterPosition();
            MovieModel movieModel = mDataMovie.get(position);

            movieModel.setId(movieModel.getId());
            movieModel.setTitle(movieModel.getTitle());
            movieModel.setOverview(movieModel.getOverview());
            movieModel.setDateRelease(movieModel.getDateRelease());
            movieModel.setPoster(movieModel.getPoster());
            movieModel.setBgMovie(movieModel.getBgMovie());
            movieModel.setRating(movieModel.getRating());

            Intent tvIntent = new Intent(itemView.getContext(), DetailMovieActivity.class);
            tvIntent.putExtra(DetailMovieActivity.EXTRA_MOVIE,movieModel);
            itemView.getContext().startActivity(tvIntent);
        }
    }
}
