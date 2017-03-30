package com.example.chenlong.imdemo.widget;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by ChenLong on 2017/2/21.
 */

public class NoScrollViewPager extends ViewPager
{
    public NoScrollViewPager(Context context)
    {
        this(context,null);
    }

    public NoScrollViewPager(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }

    //原生的viewPager能滑动 因为拦截了 自己处理 所以这里不拦截
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev)
    {
        return false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev)
    {
        return super.onTouchEvent(ev);
    }
}
