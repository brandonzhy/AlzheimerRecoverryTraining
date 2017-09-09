package com.stone.app.login;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.LinkMovementMethod;
import android.text.method.PasswordTransformationMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.stone.app.R;
import com.stone.app.Util.ToastUtil;

import cn.smssdk.SMSSDK;

public class RegisterActivity extends Activity implements View.OnClickListener {
    private EditText et_register_password, et_register_phone, et_register_code;
    private TextView tv_user_protocol;
    private ImageView img_password_eye;
    private Button bnt_register_getcode, bt_register_id;
    private String myphone, mycode;
    private SharedPreferences.Editor editor;
    //    private boolean flag = true;
    private int img[] = {R.mipmap.ic_password_eye_on, R.mipmap.ic_password_eye_off};
    private int position = 0;
    private TextView now;

    private getcodeServices getcodeServices;
    private boolean registerfalg = false, resultflag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_register);
        init();
        tv_user_protocol.setText(getClickableSpan());
        tv_user_protocol.setMovementMethod(LinkMovementMethod.getInstance());

    }

    private void init() {
        now = findViewById(R.id.now);
        et_register_password = (EditText) findViewById(R.id.et_register_password);
        et_register_phone = (EditText) findViewById(R.id.et_register_phone);
        et_register_code = (EditText) findViewById(R.id.et_register_code);
        tv_user_protocol = (TextView) findViewById(R.id.tv_register_user_protocol);
        img_password_eye = (ImageView) findViewById(R.id.img_password_eye);
        bnt_register_getcode = (Button) findViewById(R.id.bnt_register_getcode);
        bnt_register_getcode.setOnClickListener(RegisterActivity.this);
        bt_register_id = (Button) findViewById(R.id.bt_register_id);
        bt_register_id.setOnClickListener(RegisterActivity.this);
        getcodeServices = new getcodeServices(RegisterActivity.this);
        //        getcodeServices.initSMSSDK(RegisterActivity.this);
    }

    //    private boolean codeButtonischecked() {
    //        if (bnt_register_getcode.getVisibility() == View.VISIBLE) {
    //            return false;
    //        }
    //        return true;
    //    }

    public void passwordShow(View view) {
        if (position == 0) {
            img_password_eye.setImageResource(img[position]);
            et_register_password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            position = 1;
        } else if (position == 1) {
            img_password_eye.setImageResource(img[position]);
            et_register_password.setTransformationMethod(PasswordTransformationMethod.getInstance());
            position = 0;
        }
        et_register_password.setSelection(et_register_password.getText().length());
    }

    //设置文字下划线及点击实现
    private SpannableString getClickableSpan() {
        SpannableString spanStr = new SpannableString(getResources().getString(R.string.user_protocol));
        spanStr.setSpan(new ForegroundColorSpan(Color.WHITE), 0, 16, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        //设置下划线文字
        spanStr.setSpan(new UnderlineSpan(), 7, 11, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        //设置文字的单击事件
        spanStr.setSpan(new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                //用户协议页面的跳转
                //                startActivity(new Intent(RegisterActivity.this, RegisterActivity.class));
            }
        }, 7, 11, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spanStr.setSpan(new UnderlineSpan(), 12, 16, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        spanStr.setSpan(new ClickableSpan() {
            @Override
            public void onClick(View view) {

            }
        }, 12, 16, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return spanStr;
    }

    public void setCursor(View view) {
        switch (view.getId()) {
            case R.id.et_register_password:
                et_register_password.setCursorVisible(true);
                break;
            case R.id.et_register_phone:
                et_register_phone.setCursorVisible(true);
                break;
            case R.id.et_register_code:
                et_register_code.setCursorVisible(true);
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //获取验证码点击事件响应
            case R.id.bnt_register_getcode:
                getcodeServices.getcode(RegisterActivity.this, bnt_register_getcode, now, et_register_phone, et_register_code);
                registerfalg = true;
                if (!TextUtils.isEmpty(et_register_phone.getText().toString().trim())) {
                    if (et_register_phone.getText().toString().trim().length() == 11) {
                        myphone = et_register_phone.getText().toString().trim();
                        SMSSDK.getVerificationCode("86", myphone);
                        et_register_code.requestFocus();
                        bnt_register_getcode.setVisibility(View.GONE);
                    } else {
                        ToastUtil.showToast(RegisterActivity.this, "请输入完整电话号码");
                        et_register_phone.requestFocus();
                    }
                } else {
                    ToastUtil.showToast(RegisterActivity.this, "请输入您的电话号码");
                    et_register_phone.requestFocus();
                }
                break;

            case R.id.bt_register_id:
                //注册按钮点击后
                if (!TextUtils.isEmpty(et_register_code.getText().toString().trim())) {
                    if (et_register_code.getText().toString().trim().length() == 4) {
                        mycode = et_register_code.getText().toString().trim();
                        SMSSDK.submitVerificationCode("86", myphone, mycode);

                        //                        getcodeServices.setFlag(false);
                        //                        try {
                        //                            Thread.currentThread().sleep(800);
                        //                        } catch (InterruptedException e) {
                        //                            e.printStackTrace();
                        //                        }
                        //                        if (getcodeServices.getCodeResult()) {
                        if (!TextUtils.isEmpty(et_register_password.getText().toString().trim())) {
                            //                            Log.i("TAG", "password is not empty");
                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    resultflag = getcodeServices.getCodeResult();
                                    Log.i("TAG", "getCodeResult先被调用，resultflag 的值为 " + resultflag + "时间为 " + System.currentTimeMillis());
                                    if (resultflag) {
                                        //将文件写入本地
                                        //PreferenceManager.getDefaultSharedPreferences利用包名来命名SharedPreferences文件
                                        SharedPreferences.Editor editor = getSharedPreferences("autologin", MODE_PRIVATE).edit();
                                        editor.putString("phone", et_register_phone.getText().toString());
                                        editor.putString("password", et_register_password.getText().toString());
                                        editor.apply();
                                        //跳转到informationActivity，后面换成gotoInformationpage()
                                        startActivity(new Intent(RegisterActivity.this, setInfomationActivity.class));
                                        finish();
                                        //  gotoInformationpage();
                                    }
                                }
                            }, 300);
                            //                                                                finish();
                        } else {
                            ToastUtil.showToast(RegisterActivity.this, "密码不能为空");
                        }

                    } else {
                        Toast.makeText(RegisterActivity.this, "请输入完整验证码", Toast.LENGTH_LONG).show();
                        et_register_code.requestFocus();
                    }
                } else {
                    Toast.makeText(RegisterActivity.this, "请输入验证码", Toast.LENGTH_LONG).show();
                    et_register_code.requestFocus();
                }
                break;
            default:
                break;
        }
    }

    //注销SDK：
    @Override
    protected void onDestroy() {
        super.onDestroy();
        SMSSDK.unregisterAllEventHandler();
    }
}
