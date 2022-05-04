package io.github.azismihsan.moviefavorite.activity;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

import io.github.azismihsan.moviefavorite.R;
import io.github.azismihsan.moviefavorite.adapter.FragmentAdapter;
import io.github.azismihsan.moviefavorite.fragment.FragmentFilmFavorite;
import io.github.azismihsan.moviefavorite.fragment.FragmentTvFavorite;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //setting toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        initView();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    //function for inisialization view
    private void initView(){
        //calling viewpager for fragment
        ViewPager viewPager = findViewById(R.id.view_pager);

        //setting viewpager
        setUpViewPager(viewPager);
        TabLayout tab = findViewById(R.id.tabs);

        //setup tab layout with viewpager
        tab.setupWithViewPager(viewPager);
    }

    private void setUpViewPager(ViewPager viewPager) {
        FragmentAdapter fragmentAdapter = new FragmentAdapter(getSupportFragmentManager());
        fragmentAdapter.addFragment(new FragmentFilmFavorite(),getResources().getString(R.string.tab_movie));
        fragmentAdapter.addFragment(new FragmentTvFavorite(),getResources().getString(R.string.tab_tv));
        viewPager.setAdapter(fragmentAdapter);
    }
}
