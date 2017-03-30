package com.example.chenlong.imdemo.activity;

import android.os.Bundle;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.CycleInterpolator;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.example.chenlong.imdemo.R;
import com.example.chenlong.imdemo.adapter.MainFragmentAdapter;
import com.example.chenlong.imdemo.fragment.BaseFragment;
import com.example.chenlong.imdemo.fragment.FragmentContact;
import com.example.chenlong.imdemo.fragment.FragmentSession;
import com.example.chenlong.imdemo.imcore.ConnectionManager;
import com.example.chenlong.imdemo.util.Cheeses;
import com.example.chenlong.imdemo.widget.DragViewLayout;
import com.example.chenlong.imdemo.widget.NoScrollViewPager;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity
{

    @BindView(R.id.dragView)
    DragViewLayout mDragView;
    @BindView(R.id.menu_list)
    ListView mMenuList;
    @BindView(R.id.textView_menu)
    TextView mTextViewMenu;
    @BindView(R.id.main_icon)
    ImageView mMainIcon;
    @BindView(R.id.viewPager)
    NoScrollViewPager mViewPager;
    @BindView(R.id.rb_session)
    RadioButton mRbSession;
    @BindView(R.id.rb_contact)
    RadioButton mRbContact;
    @BindView(R.id.rg_session_contact)
    RadioGroup mRgSessionContact;
    private ConnectionManager mConnectionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        mConnectionManager = ConnectionManager.getInstance();

        initMenu();

        initMainData();

    }

    private void initMainData()
    {
        ///设置dragView 拖拽的监听
        mDragView.setOnDragStateChangedListener(new DragViewLayout.OnDragStateChangedListener()
        {
            @Override
            public void onOpen()
            {

            }

            @Override
            public void onClose()
            {
                ViewCompat.animate(mMainIcon)
                        .translationX(5)
                        .setInterpolator(new CycleInterpolator(5))
                        .setDuration(500)
                        .start();
            }

            @Override
            public void onDragging(float percent)
            {
                ViewCompat.setAlpha(mMainIcon, 1 - percent);
            }
        });

        //初次设置一个默认选择的
        mRbSession.setChecked(true);

        mRgSessionContact.setOnCheckedChangeListener((group, checkedId) -> {
            //根据点击的RadioButton切换到对应的viewPager页面
            View checkedView = group.findViewById(checkedId);
            int index = group.indexOfChild(checkedView);

            mViewPager.setCurrentItem(index);
        });

        //初始化Fragment 并添加适配器到adapter
        initFragment();
        mViewPager.setAdapter(new MainFragmentAdapter(getSupportFragmentManager(), mFragments));
    }


    private List<BaseFragment> mFragments = new ArrayList<>();

    private void initFragment()
    {
        mFragments.add(FragmentSession.newInstance());
        mFragments.add(FragmentContact.newInstance());
    }

    private void initMenu()
    {
        String jid = mConnectionManager.getUserJid();
        mTextViewMenu.setText(jid);

        mMenuList.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, Cheeses.QQ_FUCTIONS));
    }
}
