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
import io.github.azismihsan.moviefavorite.model.TvModel;

import static io.github.azismihsan.moviefavorite.database.DatabaseContract.TvShowFavorite.CONTENT_URI_TVSHOWS;

public class TvFavoriteAdapter extends RecyclerView.Adapter <TvFavoriteAdapter.TvShowViewHolder> {

    private ArrayList<TvModel> mTvShowModel = new ArrayList<>();
    private static final String URL = "https://image.tmdb.org/t/p/w342";
    private Activity activity;
    MovieHelper helper;

    public TvFavoriteAdapter (Activity activity){
        this.activity = activity;
    }

    public ArrayList <TvModel> getListTvShow(){
        return mTvShowModel;
    }

    public void setmTvShowModel(ArrayList <TvModel> model){
        if (mTvShowModel.size() > 0){
            this.mTvShowModel.clear();
        }
        this.mTvShowModel.addAll(model);
        notifyDataSetChanged();
    }

    public void addItemTv (TvModel tvModel){
        this.mTvShowModel.add(tvModel);
        notifyItemInserted(mTvShowModel.size() -1 );
    }

    public void removeItemTv (int n){
        this.mTvShowModel.remove(n);
        notifyItemRemoved(n);
        notifyItemRangeChanged(n, mTvShowModel.size());
    }

    public void updateItemTv (int n, TvModel tvModel){
        this.mTvShowModel.set(n, tvModel);
        notifyItemChanged(n, tvModel);
    }

    @NonNull
    @Override
    public TvFavoriteAdapter.TvShowViewHolder onCreateViewHolder(
            @NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.list_items_favorite, parent, false);
        return new TvShowViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TvFavoriteAdapter.TvShowViewHolder holder, int position) {
        holder.tvTitle.setText(mTvShowModel.get(position).getTitle());
        holder.tvOverview.setText(mTvShowModel.get(position).getOverview());
        Glide.with(holder.itemView.getContext()).load(URL+mTvShowModel.get(position).getCover()).into(holder.imgCover);
    }

    @Override
    public int getItemCount() {
        return mTvShowModel.size();
    }

    public class TvShowViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView tvTitle, tvOverview;
        ImageView imgCover;
        Button btnUnFavorite;

        public TvShowViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tv_title);
            tvOverview = itemView.findViewById(R.id.tv_overview);
            imgCover = itemView.findViewById(R.id.img_cover);
            btnUnFavorite = itemView.findViewById(R.id.btn_unfavorite);
            btnUnFavorite.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int n = getAdapterPosition();
            TvModel tvModel = mTvShowModel.get(n);

            switch (v.getId()){
                case R.id.btn_unfavorite:
                    Uri uriDelete = Uri.parse(CONTENT_URI_TVSHOWS+" / "+tvModel.getId());
                    int result = v.getContext().getContentResolver().delete(uriDelete,
                            null, null);
                    if (result > 0){
                        TvFavoriteAdapter.this.removeItemTv(n);
                        Toast.makeText(v.getContext(),String.valueOf(v.getResources()
                                .getString(R.string.delete_)),Toast.LENGTH_SHORT).show();
                    }else {
                        Toast.makeText(v.getContext(), String.valueOf(v.getResources()
                                .getString(R.string.cancel_delete_)),Toast.LENGTH_SHORT).show();
                    }
                    break;//baru sampai sini
            }

        }
    }
}

