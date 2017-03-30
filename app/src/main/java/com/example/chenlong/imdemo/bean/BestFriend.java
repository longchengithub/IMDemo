package com.example.chenlong.imdemo.bean;

import com.example.chenlong.imdemo.util.CommonUtils;

/**
 * Created by ChenLong on 2017/2/22.
 */

public class BestFriend implements Comparable<BestFriend>
{
    private String jid;

    private String name;

    private String pinyin;

    private String nameOrJid;

    public BestFriend()
    {
    }

    public BestFriend(String jid, String name)
    {
        this.jid = jid;
        this.name = name;
        this.nameOrJid = CommonUtils.getNed(jid, name);
        this.pinyin = CommonUtils.hanZi2PinYin(nameOrJid);
    }

    public String getJid()
    {
        return jid;
    }

    public void setJid(String jid)
    {
        this.jid = jid;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getPinyin()
    {
        return pinyin;
    }

    public void setPinyin(String pinyin)
    {
        this.pinyin = pinyin;
    }

    public String getNameOrJid()
    {
        return nameOrJid;
    }

    public void setNameOrJid(String nameOrJid)
    {
        this.nameOrJid = nameOrJid;
    }

    @Override
    public int compareTo(BestFriend o)
    {
        return this.getPinyin().compareTo(o.getPinyin());
    }
}
