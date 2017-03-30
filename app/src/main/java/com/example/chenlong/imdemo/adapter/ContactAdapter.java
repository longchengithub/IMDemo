package com.example.chenlong.imdemo.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.chenlong.imdemo.R;
import com.example.chenlong.imdemo.activity.ChatActivity;
import com.example.chenlong.imdemo.bean.BestFriend;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by ChenLong on 2017/2/23.
 */
public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ContactViewHolder>
{
    private List<BestFriend> mBestFriends;
    private Context mContext;

    public ContactAdapter(List<BestFriend> bestFriends, Context context)
    {
        this.mBestFriends = bestFriends;
        this.mContext = context;
    }

    @Override
    public ContactAdapter.ContactViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_contact, parent, false);
        return new ContactViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ContactAdapter.ContactViewHolder holder, int position)
    {
        BestFriend bestFriend = mBestFriends.get(position);
        String indexLetter = bestFriend.getPinyin().charAt(0) + "";

        if (position == 0)
        {
            holder.mContactIndex.setVisibility(View.VISIBLE);
        } else
        {
            String preIndexLetter = mBestFriends.get(position - 1).getPinyin().charAt(0) + "";
            boolean flag = TextUtils.equals(preIndexLetter, indexLetter);
            holder.mContactIndex.setVisibility(flag ? View.GONE : View.VISIBLE);

        }

        holder.mContactIndex.setText(indexLetter);
        holder.mContactName.setText(bestFriend.getNameOrJid());

        holder.mContactName.setOnClickListener(v -> {
            //跳转到聊天界面
            Intent intent = new Intent(mContext, ChatActivity.class);
            //携带昵称和jid 昵称用于显示名称  jid是唯一标识 与人通讯时需要
            Bundle bundle = new Bundle();
            bundle.putString("name", bestFriend.getName());
            bundle.putString("jid", bestFriend.getJid());
            intent.putExtra("chat", bundle);
            mContext.startActivity(intent);
        });
    }

    @Override
    public int getItemCount()
    {
        return mBestFriends == null ? 0 : mBestFriends.size();
    }

    public static class ContactViewHolder extends RecyclerView.ViewHolder
    {
        @BindView(R.id.contact_index)
        TextView mContactIndex;
        @BindView(R.id.contact_name)
        TextView mContactName;

        public ContactViewHolder(View itemView)
        {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
