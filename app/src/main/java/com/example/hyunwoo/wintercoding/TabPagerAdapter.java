package com.example.hyunwoo.wintercoding;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

public class TabPagerAdapter extends FragmentStatePagerAdapter {
    private final int TAB_COUNT;
    private MonthFragment monthFragment;
    private WeekFragment weekFragment;
    private DayFragment dayFragment;
    public TabPagerAdapter(FragmentManager fm, int tabCount) {
        super(fm);
        TAB_COUNT = tabCount;
        monthFragment = new MonthFragment();
        weekFragment = new WeekFragment();
        dayFragment = new DayFragment();
    }

    @Override
    public Fragment getItem(int position) {
        switch (position)
        {
            case 0:
                return monthFragment;
            case 1:
                return weekFragment;
            case 2:
                return dayFragment;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return TAB_COUNT;
    }
}
