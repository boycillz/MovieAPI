package io.github.azismihsan.movieapi.activity;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

import io.github.azismihsan.movieapi.R;
import io.github.azismihsan.movieapi.adapter.FragmentAdapter;
import io.github.azismihsan.movieapi.fragment.FragmentFilm;
import io.github.azismihsan.movieapi.fragment.FragmentTv;

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
    public boolean onCreateOptionsMenu(Menu menu) {
        //call menu change language
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.option_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            //action to change language
            case R.id.menu_bahasa:
                Intent intentBahasa = new Intent(Settings.ACTION_LOCALE_SETTINGS);
                startActivity(intentBahasa);
                break;
            //action to menu favorite
            case R.id.menu_favorite:
                Intent intentFavorite = new Intent(MainActivity.this,
                        FavoriteActivity.class);
                startActivity(intentFavorite);
                break;
            //action to menu notification
            case R.id.menu_notification:
                Intent intentNotification = new Intent(MainActivity.this,
                        SettingActivity.class);
                startActivity(intentNotification);
                break;
            //action to menu search
            case R.id.menu_search:
                Intent intentSearching = new Intent(MainActivity.this,
                        SearchingMovieActivity.class);
                startActivity(intentSearching);
                break;
        }
        return true;
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
        fragmentAdapter.addFragment(new FragmentFilm(),getResources().getString(R.string.tab_movie));
        fragmentAdapter.addFragment(new FragmentTv(),getResources().getString(R.string.tab_tv));
        viewPager.setAdapter(fragmentAdapter);
    }
}