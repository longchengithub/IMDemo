package com.example.chenlong.imdemo.imcore;

import android.text.TextUtils;

import org.jivesoftware.smack.ChatManager;
import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.Roster;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;

/**
 * Created by ChenLong on 2017/2/20.
 * 凡是涉及到跟服务器打交道的,界面找ConnectionManager,ConnectionManager找asmark,就相当于界面访问了服务器
 * 1.建立连接
 * 2.登录
 * 3.退出
 * 4.发送消息
 * 5.接收消息
 */

public class ConnectionManager
{

    private XMPPConnection mXmppConnection;

    //单例模式
    private ConnectionManager()
    {
    }

    private static ConnectionManager sConnectionManager;

    public static ConnectionManager getInstance()
    {
        if (sConnectionManager == null)
        {
            synchronized (ConnectionManager.class)
            {
                if (sConnectionManager == null)
                {
                    sConnectionManager = new ConnectionManager();
                    return sConnectionManager;
                }
            }
        }
        return sConnectionManager;
    }

    /**
     * 1.建立asmark的连接
     */
    public static String HOST = "192.168.1.129";  //默认ip
    public static int POST = 5222;                //默认端口号

    public void connect(String ip) throws XMPPException
    {
        if (!TextUtils.isEmpty(ip))
        {
            HOST = ip;
        }
        //初始化连接配置,ip地址跟端口号
        ConnectionConfiguration configuration = new ConnectionConfiguration(HOST, POST);
        //关闭SASL安全认证
        configuration.setSASLAuthenticationEnabled(false);
        configuration.setSecurityMode(ConnectionConfiguration.SecurityMode.disabled);
        //找asmark连接
        mXmppConnection = new XMPPConnection(configuration);
        //必须抛出异常,让服务器处理并弹出错误信息
        mXmppConnection.connect();
    }

    /**
     * 2.登录
     * 找asmark的api中xmppConnnection的登录方法
     */
    public void login(String account, String password) throws XMPPException
    {
        mXmppConnection.login(account, password);
    }

    /**
     * 3.判断是否是连接状态
     */
    public boolean isConnect()
    {
        return mXmppConnection != null && mXmppConnection.isConnected();
    }

    /**
     * 4.判断是否是登录状态
     */
    public boolean isLogin()
    {
        return mXmppConnection.isAuthenticated();
    }

    /**
     * 5.获取用户名等信息
     */
    public String getUserJid()
    {
        return mXmppConnection.getUser();
    }

    /**
     * 6.获取所有联系人 也就是Roster花名册对象
     */
    public Roster getRoster()
    {
        return mXmppConnection.getRoster();
    }

    /**
     * 7.获取聊天管理对象
     */
    public ChatManager getChatManager()
    {
        return mXmppConnection.getChatManager();
    }
}
