package com.example.chenlong.imdemo.fragment;

import android.graphics.Rect;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.chenlong.imdemo.R;
import com.example.chenlong.imdemo.adapter.ContactAdapter;
import com.example.chenlong.imdemo.bean.BestFriend;
import com.example.chenlong.imdemo.imcore.ConnectionManager;
import com.example.chenlong.imdemo.widget.FastIndexBar;

import org.jivesoftware.smack.Roster;
import org.jivesoftware.smack.RosterEntry;
import org.jivesoftware.smack.RosterListener;
import org.jivesoftware.smack.packet.Presence;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by ChenLong on 2017/2/21.
 */

public class FragmentContact extends BaseFragment
{
    @BindView(R.id.contact_recycler)
    RecyclerView mContactRecycler;
    @BindView(R.id.fib)
    FastIndexBar mFib;
    @BindView(R.id.letter)
    TextView mLetter;

    private List<BestFriend> mBestFriends = new ArrayList<>();
    private ContactAdapter mContactAdapter;
    private LinearLayoutManager mLayoutManager;
    private ConnectionManager mInstance;
    private Roster mRoster;

    @Override
    protected int getChildResId()
    {
        return R.layout.contact_fragment;
    }

    private boolean isShowToast;

    @Override
    protected void finishViewCreated()
    {
        //字母索引回调
        mFib.setOnLetterChangedListener(new FastIndexBar.OnLetterChangedListener()
        {
            @Override
            public void onLetterChanged(String letter)
            {
                mLetter.setVisibility(View.VISIBLE);
                mLetter.setText(letter);
                isShowToast = true;

                for (int i = 0; i < mBestFriends.size(); i++)
                {
                    //循环获取拼音去匹配
                    if (letter.equals(mBestFriends.get(i).getPinyin().charAt(0) + ""))
                    {
                        //滚动到指定的位置 用LinearLayoutManager的属性
                        mLayoutManager.scrollToPositionWithOffset(i, 0);
                        mLayoutManager.setStackFromEnd(true);
                        break;
                    }
                }
            }

            @Override
            public void onFinishChanged()
            {
                Observable.timer(2, TimeUnit.SECONDS)
                        .doOnSubscribe(disposable -> isShowToast = false)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(aLong -> {
                            if (!isShowToast)
                            {
                                mLetter.setVisibility(View.INVISIBLE);
                            }
                        });
            }
        });

        //拿到ConnectionManage对象
        mInstance = ConnectionManager.getInstance();

        //获取所有联系人
        updateBestFriends();

        //初始化RecyclerView
        initRecycler();

        //联系人变动后的回调
        mRoster.addRosterListener(new RosterListener()
        {
            //添加好友时触发
            @Override
            public void entriesAdded(Collection<String> collection)
            {
                flushRoster();
            }

            //更新时触发
            //添加时也触发
            @Override
            public void entriesUpdated(Collection<String> collection)
            {
                flushRoster();
            }

            //删除时触发
            @Override
            public void entriesDeleted(Collection<String> collection)
            {
                flushRoster();
            }


            @Override
            public void presenceChanged(Presence presence)
            {

            }
        });
    }

    private void flushRoster()
    {
        Observable.just(mBestFriends)
                .doOnSubscribe(disposable -> {
                    mBestFriends.clear();
                    updateBestFriends();
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(o -> {
                    mContactAdapter.notifyDataSetChanged();
                });
    }

    /**
     * 获取好友列表
     */
    private void updateBestFriends()
    {
        //拿到花名册对象
        mRoster = mInstance.getRoster();
        //获取所有的好友的集合
        Collection<RosterEntry> entries = mRoster.getEntries();

        //重新封装好友对象 只拿到jid 和 昵称  然后在赋值时转换成pinyin 并判断显示昵称还是jid
        for (RosterEntry entry : entries)
        {
            mBestFriends.add(new BestFriend(entry.getUser(), entry.getName()));
        }

        //排序
        Collections.sort(mBestFriends);
    }

    /**
     * 绑定recyclerView
     */
    private void initRecycler()
    {
        mContactAdapter = new ContactAdapter(mBestFriends, getContext());

        mLayoutManager = new LinearLayoutManager(getContext());
        mContactRecycler.setLayoutManager(mLayoutManager);
        mContactRecycler.addItemDecoration(new MyItemDecoration());
        mContactRecycler.setAdapter(mContactAdapter);
    }

    public static FragmentContact newInstance()
    {
        return new FragmentContact();
    }

    /**
     * 给条目添加分割线
     */
    class MyItemDecoration extends RecyclerView.ItemDecoration
    {
        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state)
        {
            outRect.set(0, 0, 0, 2);
        }
    }
}
