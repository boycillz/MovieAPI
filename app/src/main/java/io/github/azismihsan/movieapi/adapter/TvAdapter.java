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
import io.github.azismihsan.movieapi.activity.DetailTvActivity;
import io.github.azismihsan.movieapi.model.TvModel;

public class TvAdapter extends RecyclerView.Adapter<TvAdapter.SerialViewHolder> {

    private List<TvModel> mDataTv;
    private static final String URL ="https://image.tmdb.org/t/p/w342";

    public TvAdapter(List<TvModel> listTv){
        this.mDataTv=listTv;
    }

    @NonNull
    @Override
    public SerialViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_items,parent, false);
        return new SerialViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SerialViewHolder holder, int position) {
        holder.bind(mDataTv.get(position));
    }

    @Override
    public int getItemCount() {
        return mDataTv.size();
    }

    public class SerialViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView title,overview;
        ImageView cover;

        private SerialViewHolder(@NonNull View itemView){
            super(itemView);

            title = itemView.findViewById(R.id.tv_title);
            overview = itemView.findViewById(R.id.tv_overview);
            cover = itemView.findViewById(R.id.img_cover);

            itemView.setOnClickListener(this);
        }

        private void bind (TvModel tvModel){
            title.setText(tvModel.getTitle());
            overview.setText(tvModel.getOverview());
            Glide.with(itemView).load(URL+tvModel.getCover()).into(cover);
        }

        public void onClick(View view){
            int position = getAdapterPosition();
            TvModel tvModel = mDataTv.get(position);

            tvModel.setId(tvModel.getId());
            tvModel.setTitle(tvModel.getTitle());
            tvModel.setCover(tvModel.getCover());
            tvModel.setOverview(tvModel.getOverview());
            tvModel.setRateTv(tvModel.getRateTv());
            tvModel.setBgTv(tvModel.getBgTv());
            tvModel.setDateRelease(tvModel.getDateRelease());

            Intent tvIntent = new Intent(itemView.getContext(), DetailTvActivity.class);
            tvIntent.putExtra(DetailTvActivity.EXTRA_FILM,tvModel);
            itemView.getContext().startActivity(tvIntent);
        }
    }

}
