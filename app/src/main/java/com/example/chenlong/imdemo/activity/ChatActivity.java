package com.example.chenlong.imdemo.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.TextView;

import com.example.chenlong.imdemo.R;
import com.example.chenlong.imdemo.adapter.ChatAdapter;
import com.example.chenlong.imdemo.bean.IMmsg;
import com.example.chenlong.imdemo.bean.IMmsgDao;
import com.example.chenlong.imdemo.db.GreenDaoManager;
import com.example.chenlong.imdemo.imcore.ConnectionManager;

import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.ChatManager;
import org.jivesoftware.smack.MessageListener;
import org.jivesoftware.smack.XMPPException;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ChatActivity extends AppCompatActivity
{

    @BindView(R.id.title)
    TextView mTitle;
    @BindView(R.id.chat_recycler)
    RecyclerView mChatRecycler;
    @BindView(R.id.send_msg)
    EditText mSendMsg;

    private Chat mChat;
    private List<IMmsg> mIMmsgList = new ArrayList<>();     //存放消息的集合
    private String mJid;
    private String mName;
    private ChatAdapter mChatAdapter;
    private LinearLayoutManager mLayoutManager;
    private IMmsgDao mDao;
    private IMmsg mMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        ButterKnife.bind(this);

        Bundle chat = getIntent().getBundleExtra("chat");
        mName = chat.getString("name");
        mJid = chat.getString("jid");

        mTitle.setText("正在与" + mName + "聊天中");

        //获取ConnectionManager对象
        ConnectionManager mInstance = ConnectionManager.getInstance();
        //获取ChatManager对象
        ChatManager chatManager = mInstance.getChatManager();
        //创建会话(聊天) 参数1:唯一标识:jid 参数2:接收消息的监听
        mChat = chatManager.createChat(mJid, mMessageListener);

        mDao = GreenDaoManager.getmDaoSession().getIMmsgDao();
        List<IMmsg> iMmsgs = mDao.queryBuilder()
                .where(IMmsgDao.Properties.Jid.eq(mJid))
                .build()
                .list();
        if (iMmsgs != null && iMmsgs.size() > 0)
        {
            mIMmsgList.addAll(iMmsgs);
        }

        mChatAdapter = new ChatAdapter(mIMmsgList);
        mLayoutManager = new LinearLayoutManager(this);
        mChatRecycler.setLayoutManager(mLayoutManager);
        mChatRecycler.setAdapter(mChatAdapter);
    }

    //接收消息的监听方法,注意:这个监听方法是在子线程里面通知
    private MessageListener mMessageListener = (chat, message) ->
            runOnUiThread(() -> {
                mMessage = new IMmsg(mJid, mName, message.getBody(), IMmsg.RECEIVE);
                mIMmsgList.add(mMessage);
                mChatAdapter.notifyItemInserted(mIMmsgList.size());
                mChatRecycler.smoothScrollToPosition(mIMmsgList.size());

                mDao.insert(mMessage);
            });

    @OnClick(R.id.send)
    public void onClick()
    {
        //获取用户输入的信息
        String content = mSendMsg.getText().toString().trim();
        //非空校验
        if (TextUtils.isEmpty(content))
        {
            return;
        }
        try
        {
            //通过mChat发送消息
            mChat.sendMessage(content);
            mMessage = new IMmsg(mJid, mName, content, IMmsg.SEND);
            mIMmsgList.add(mMessage);
            mChatAdapter.notifyItemInserted(mIMmsgList.size());
            mChatRecycler.smoothScrollToPosition(mIMmsgList.size());

            mDao.insert(mMessage);
            //发送完毕后,清空文本
            mSendMsg.setText("");
        } catch (XMPPException e)
        {
            e.printStackTrace();
        }
    }
}
