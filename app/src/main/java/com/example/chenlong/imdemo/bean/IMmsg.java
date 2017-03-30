package com.example.chenlong.imdemo.bean;

import com.example.chenlong.imdemo.util.CommonUtils;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Index;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by ChenLong on 2017/2/24.
 */

@Entity
public class IMmsg
{
    public static final boolean SEND = false;
    public static final boolean RECEIVE = true;

    @Id(autoincrement = true)
    private Long id;
    @Index
    private String jid;
    private String name;
    private String content;
    private boolean isReceive;      //false send. true receive
    private String time;

    public IMmsg()
    {
    }

    public IMmsg(String jid, String name, String content, boolean isReceive)
    {
        this.jid = jid;
        this.name = CommonUtils.getNed(jid, name);
        this.content = content;
        this.isReceive = isReceive;
        this.time = CommonUtils.getCurrentTime();
    }

    @Generated(hash = 1683843394)
    public IMmsg(Long id, String jid, String name, String content,
                 boolean isReceive, String time)
    {
        this.id = id;
        this.jid = jid;
        this.name = name;
        this.content = content;
        this.isReceive = isReceive;
        this.time = time;
    }

    public Long getId()
    {
        return id;
    }

    public void setId(Long id)
    {
        this.id = id;
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

    public String getContent()
    {
        return content;
    }

    public void setContent(String content)
    {
        this.content = content;
    }

    public String getTime()
    {
        return time;
    }

    public void setTime(String time)
    {
        this.time = time;
    }

    public boolean getIsReceive()
    {
        return this.isReceive;
    }

    public void setIsReceive(boolean isReceive)
    {
        this.isReceive = isReceive;
    }
}
