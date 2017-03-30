package com.example.chenlong.imdemo.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.chenlong.imdemo.R;
import com.example.chenlong.imdemo.imcore.ConnectionManager;

import org.jivesoftware.smack.XMPPException;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LoginActivity extends AppCompatActivity
{

    @BindView(R.id.login_btn)
    Button mLoginBtn;
    @BindView(R.id.login_account)
    EditText mLoginAccount;
    @BindView(R.id.login_password)
    EditText mLoginPassword;
    private ConnectionManager mInstance;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        mInstance = ConnectionManager.getInstance();

        mLoginBtn.setOnClickListener(v -> {
            String account = mLoginAccount.getText().toString().trim();
            String password = mLoginPassword.getText().toString().trim();

            if (TextUtils.isEmpty(account) || TextUtils.isEmpty(password))
            {
                Toast.makeText(this, "用户名与密码不能为空!", Toast.LENGTH_SHORT).show();
                return;
            }

            try
            {
                mInstance.login(account, password);
                startActivity(new Intent(this, MainActivity.class));
                finish();
            } catch (XMPPException e)
            {
                e.printStackTrace();
                Toast.makeText(this, "登录失败!请确认账户名或密码无误!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
