package com.example.chenlong.imdemo.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by ChenLong on 2017/2/25.
 */

public class SessionAdapter extends RecyclerView.Adapter<SessionAdapter.SessionViewHolder>
{
    @Override
    public SessionAdapter.SessionViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        return null;
    }

    @Override
    public void onBindViewHolder(SessionAdapter.SessionViewHolder holder, int position)
    {

    }

    @Override
    public int getItemCount()
    {
        return 0;
    }

    public static class SessionViewHolder extends RecyclerView.ViewHolder
    {
        public SessionViewHolder(View itemView)
        {
            super(itemView);
        }
    }
}
