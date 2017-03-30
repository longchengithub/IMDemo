package com.example.chenlong.imdemo.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.chenlong.imdemo.fragment.BaseFragment;

import java.util.List;

/**
 * Created by ChenLong on 2017/2/21.
 */

public class MainFragmentAdapter extends FragmentPagerAdapter
{
    private List<BaseFragment> mBaseFragmentList;

    public MainFragmentAdapter(FragmentManager fm, List<BaseFragment> mBaseFragmentList)
    {
        super(fm);
        this.mBaseFragmentList = mBaseFragmentList;
    }

    @Override
    public Fragment getItem(int position)
    {
        return mBaseFragmentList.get(position);
    }

    @Override
    public int getCount()
    {
        return mBaseFragmentList == null ? 0 : mBaseFragmentList.size();
    }
}
