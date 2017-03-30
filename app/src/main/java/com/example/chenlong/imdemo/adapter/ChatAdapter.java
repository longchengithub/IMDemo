package com.example.chenlong.imdemo.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.chenlong.imdemo.R;
import com.example.chenlong.imdemo.bean.IMmsg;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by ChenLong on 2017/2/24.
 */

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ChatViewHolder>
{
    private List<IMmsg> mIMmsgList;

    public ChatAdapter(List<IMmsg> iMmsgList)
    {
        this.mIMmsgList = iMmsgList;
    }

    @Override
    public ChatAdapter.ChatViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_chat, parent, false);

        return new ChatViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ChatAdapter.ChatViewHolder holder, int position)
    {
        IMmsg iMmsg = mIMmsgList.get(position);
        if (iMmsg.getIsReceive())
        {
            holder.mLeft.setVisibility(View.GONE);
            holder.mRight.setVisibility(View.VISIBLE);
            holder.mRightView.setText(iMmsg.getContent());
            holder.mName2View.setText(iMmsg.getName());
        } else
        {
            holder.mLeft.setVisibility(View.VISIBLE);
            holder.mRight.setVisibility(View.GONE);
            holder.mLeftView.setText(iMmsg.getContent());
            holder.mNameView.setText(iMmsg.getName());
        }

        holder.mTitleView.setText(iMmsg.getTime());
    }

    @Override
    public int getItemCount()
    {
        return mIMmsgList == null ? 0 : mIMmsgList.size();
    }

    public static class ChatViewHolder extends RecyclerView.ViewHolder
    {
        @BindView(R.id.title)
        TextView mTitleView;
        @BindView(R.id.name)
        TextView mNameView;
        @BindView(R.id.tv_left)
        TextView mLeftView;
        @BindView(R.id.name2)
        TextView mName2View;
        @BindView(R.id.tv_right)
        TextView mRightView;
        @BindView(R.id.layout_left)
        RelativeLayout mLeft;
        @BindView(R.id.layout_right)
        RelativeLayout mRight;

        public ChatViewHolder(View itemView)
        {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
