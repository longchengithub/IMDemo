package com.example.chenlong.imdemo.fragment;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.chenlong.imdemo.R;
import com.example.chenlong.imdemo.bean.IMmsgDao;
import com.example.chenlong.imdemo.db.GreenDaoManager;
import com.example.chenlong.imdemo.imcore.ConnectionManager;

import butterknife.BindView;

/**
 * Created by ChenLong on 2017/2/21.
 */

public class FragmentSession extends BaseFragment
{
    @BindView(R.id.session_recycler)
    RecyclerView mSessionRecycler;

    private ConnectionManager mInstance;
    private LinearLayoutManager mLayoutManager;
    private IMmsgDao mDao;

    @Override
    protected int getChildResId()
    {
        return R.layout.session_fragment;
    }

    @Override
    protected void finishViewCreated()
    {
        //获取ConnectionManager对象
        mInstance = ConnectionManager.getInstance();

        mLayoutManager = new LinearLayoutManager(getContext());
        mSessionRecycler.setLayoutManager(mLayoutManager);

        mDao = GreenDaoManager.getmDaoSession().getIMmsgDao();
        mDao.queryRawCreate("");
    }

    public static FragmentSession newInstance()
    {
        return new FragmentSession();
    }


}
