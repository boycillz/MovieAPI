package io.github.azismihsan.movieapi.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import io.github.azismihsan.movieapi.R;
import io.github.azismihsan.movieapi.adapter.TvAdapter;
import io.github.azismihsan.movieapi.model.TvModel;
import io.github.azismihsan.movieapi.viewmodel.MainViewModel;

public class FragmentTv extends Fragment {

    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private TvAdapter tvAdapter;
    private MainViewModel mainViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_film,container,false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init(view);
        mainViewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        mainViewModel.getAllTv().observe(this, new Observer<List<TvModel>>() {
            @Override
            public void onChanged(List<TvModel> tvModels) {
                showLoading(false);
                ShowsDataMovie(tvModels);
            }
        });

    }

    private void init(View view){
        recyclerView = view.findViewById(R.id.rv_film);
        progressBar = view.findViewById(R.id.progress_bar);
        MainViewModel mainViewModel;
        showLoading(true);
    }

    private void ShowsDataMovie(List<TvModel> mv){
        tvAdapter = new TvAdapter(mv);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(tvAdapter);
        tvAdapter.notifyDataSetChanged();
    }

    private void showLoading(Boolean status){
        if(status){
            progressBar.setVisibility(View.VISIBLE);
        }else{
            progressBar.setVisibility(View.GONE);
        }
    }
}