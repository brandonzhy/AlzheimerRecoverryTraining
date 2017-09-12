package com.stone.app.login;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.UnderlineSpan;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.stone.app.R;
import com.stone.app.dataBase.RealmDB;


public class loginActivity extends Activity implements View.OnClickListener {
    public TextView tv_register;
    public Button bt_login_phone, btn_qq, btn_wechat, btn_weibo;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        RealmDB.getDataBaseManager();
        setContentView(R.layout.my_login);
        init();
        tv_register.setText(getClickableSpan());
        tv_register.setMovementMethod(LinkMovementMethod.getInstance());

    }

    private SpannableString getClickableSpan() {
        SpannableString spanStr = new SpannableString(getResources().getString(R.string.registercount));
        //设置下划线文字
        spanStr.setSpan(new UnderlineSpan(), 6, 10, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        //设置文字的单击事件
        spanStr.setSpan(new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                startActivity(new Intent(loginActivity.this, RegisterActivity.class));
                finish();
            }
        }, 6, 10, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        return spanStr;
    }

    private void init() {
        tv_register = (TextView) findViewById(R.id.tv_register);
        bt_login_phone = (Button) findViewById(R.id.btn_login_phone);
        btn_qq = (Button) findViewById(R.id.btn_qq);
        btn_wechat = (Button) findViewById(R.id.btn_wechat);
        btn_weibo = (Button) findViewById(R.id.btn_weibo);
        bt_login_phone.setOnClickListener(loginActivity.this);
        btn_wechat.setOnClickListener(loginActivity.this);
        btn_qq.setOnClickListener(loginActivity.this);
        btn_weibo.setOnClickListener(loginActivity.this);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            //手机登录按钮点击后事件响应
            case R.id.btn_login_phone:
                startActivity(new Intent(loginActivity.this, phone_loginActivity.class));
                finish();
                break;
            //QQ登录按钮点击后事件响应
            case R.id.btn_qq:

                break;
            //微信登录按钮点击后事件响应
            case R.id.btn_wechat:

                break;
            //微博登录按钮点击后事件响应
            case R.id.btn_weibo:

                break;
        }
    }
}
