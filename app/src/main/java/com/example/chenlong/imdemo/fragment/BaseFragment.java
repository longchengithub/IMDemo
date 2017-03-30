package com.example.chenlong.imdemo.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by ChenLong on 2017/2/21.
 */

public abstract class BaseFragment extends Fragment
{

    private View mChildView;

    private Unbinder mUnbinder;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        if (mChildView == null)
        {
            mChildView = inflater.inflate(getChildResId(), null);
        }
        return mChildView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);
        mUnbinder = ButterKnife.bind(this, view);
        finishViewCreated();
    }


    @Override
    public void onDestroy()
    {
        super.onDestroy();
        mUnbinder.unbind();
    }

    protected abstract int getChildResId();

    protected abstract void finishViewCreated();


}
