package io.github.azismihsan.movieapi.adapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class FragmentAdapter extends FragmentPagerAdapter {

    //array for fragment
    private List<Fragment> mFragment = new ArrayList<>();

    //array for title fragment
    private List<String> titleFragment = new ArrayList<>();

    public FragmentAdapter(FragmentManager fm) {
        super(fm, FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return mFragment.get(position);
    }

    @Override
    public int getCount() {
        return mFragment.size();
    }

    public void addFragment (Fragment fragment, String title){
        //Add fragment and title fragment
        mFragment.add(fragment);
        titleFragment.add(title);
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position){
        return titleFragment.get(position);
    }
}
