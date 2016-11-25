package edu.dhbw.andobjviewer.homepage;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Edwin on 24/11/2016.
 */

public class HomeViewPagerAdapter extends FragmentPagerAdapter {
    private List<Fragment> frag = new ArrayList<>();

    public HomeViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    public void addFragment(Fragment fragment) {
        frag.add(fragment);
    }

    @Override public Fragment getItem(int position) {
        return frag.get(position);
    }

    @Override public int getCount() {
        return this.frag.size();
    }
}
