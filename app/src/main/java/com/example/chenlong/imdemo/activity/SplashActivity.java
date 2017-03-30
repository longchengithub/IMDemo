package com.example.chenlong.imdemo.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.chenlong.imdemo.R;
import com.example.chenlong.imdemo.activity.LoginActivity;
import com.example.chenlong.imdemo.activity.MainActivity;
import com.example.chenlong.imdemo.imcore.ConnectionManager;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class SplashActivity extends AppCompatActivity
{

    @BindView(R.id.splash_host)
    EditText mSplashHost;
    @BindView(R.id.splash_login)
    Button mSplashLogin;
    private ConnectionManager mConnectionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ButterKnife.bind(this);

        //1.首先创建ConnectionManager
        mConnectionManager = ConnectionManager.getInstance();

        if (mConnectionManager.isConnect())
        {
            //与服务器建立连接成功
            if (mConnectionManager.isLogin())
            {
                //登录成功
                startActivity(new Intent(this, MainActivity.class));
            } else
            {
                startActivity(new Intent(this, LoginActivity.class));
            }
            finish();
        } else
        {
            connect("10.0.2.2");
        }

        //变更ip点击登录
        mSplashLogin.setOnClickListener(v -> connect(mSplashHost.getText().toString().trim()));
    }

    /**
     * 统一处理登录是第一次连接
     *
     * @param ip
     */
    public void connect(String ip)
    {
        Observable.just(ip)
                .doOnSubscribe(disposable -> mConnectionManager.connect(ip))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(s -> {
                    Log.d("SplashActivity", "登录成功");
                    startActivity(new Intent(this, LoginActivity.class));
                    finish();
                }, throwable -> {
                    Toast.makeText(this, "登录异常", Toast.LENGTH_SHORT).show();
                    Log.d("SplashActivity", "登录异常" + throwable.toString());
                });
    }
}
