package com.stone.app.login;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.stone.app.R;
import com.stone.app.Util.ToastUtil;
import com.stone.app.mainPage.mainPage;

import cn.smssdk.SMSSDK;

public class phone_loginActivity extends Activity implements View.OnClickListener {
    private TextView tv_phone_login_passwordlogin, tv_phone_login_code, tv_phone_login_passcode, phone_login_now;
    private EditText et_phone_login_password, et_phone_login_code, et_phone_login_phone;
    private ImageView img_phone_login_eye;
    private Button btn_phone_login_getcode, btn_phone_login;
    private String mycode, myphone;
    SharedPreferences.Editor editor;
    private boolean passwordloginFlag = true,resultflag=false;
    //    private DataBaseManager dataBaseManager ;
    private int img[] = {R.mipmap.ic_password_eye_on, R.mipmap.ic_password_eye_off};
    private int position = 0;
    private getcodeServices getcodeServices;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_phone_login);
        init();
    }

    private void init() {
        tv_phone_login_passwordlogin = findViewById(R.id.tv_phone_login_passwordlogin);
        tv_phone_login_code = findViewById(R.id.tv_phone_login_code);
        tv_phone_login_passcode = findViewById(R.id.tv_phone_login_passcode);
        et_phone_login_password = findViewById(R.id.et_phone_login_password);
        et_phone_login_code = findViewById(R.id.et_phone_login_code);
        et_phone_login_phone = findViewById(R.id.et_phone_login_phone);
        phone_login_now = findViewById(R.id.phone_login_now);
        img_phone_login_eye = findViewById(R.id.img_phone_login_eye);
        btn_phone_login_getcode = findViewById(R.id.btn_phone_login_getcode);
        btn_phone_login = findViewById(R.id.btn_phone_login);
        btn_phone_login.setOnClickListener(phone_loginActivity.this);
        btn_phone_login_getcode.setOnClickListener(phone_loginActivity.this);
        tv_phone_login_passwordlogin.setOnClickListener(phone_loginActivity.this);
        tv_phone_login_code.setOnClickListener(phone_loginActivity.this);
        img_phone_login_eye.setOnClickListener(phone_loginActivity.this);
        getcodeServices = new getcodeServices(phone_loginActivity.this);
        //        dataBaseManager=new DataBaseManager();

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            //点击上方的密码登陆触发的事件响应
            case R.id.tv_phone_login_passwordlogin:
                et_phone_login_password.setVisibility(View.VISIBLE);
                img_phone_login_eye.setVisibility(View.VISIBLE);
                btn_phone_login_getcode.setVisibility(View.GONE);
                et_phone_login_code.setVisibility(View.GONE);
                phone_login_now.setVisibility(View.GONE);
                tv_phone_login_passcode.setText("密码");
                tv_phone_login_passwordlogin.setTextColor(getResources().getColor(R.color.white));
                tv_phone_login_code.setTextColor(getResources().getColor(R.color.light_gray));
                passwordloginFlag = true;
                break;
            //点击上方的验证码登陆字样触发的事件响应
            case R.id.tv_phone_login_code:
                //                getcodeServices.initSMSSDK(phone_loginActivity.this);

                et_phone_login_password.setVisibility(View.GONE);
                img_phone_login_eye.setVisibility(View.GONE);
                btn_phone_login_getcode.setVisibility(View.VISIBLE);
                et_phone_login_code.setVisibility(View.VISIBLE);
                tv_phone_login_passcode.setText("验证码");
                tv_phone_login_code.setTextColor(getResources().getColor(R.color.white));
                tv_phone_login_passwordlogin.setTextColor(getResources().getColor(R.color.light_gray));
                passwordloginFlag = false;
                break;
            //点击眼睛小图标触发的事件响应（密码显示及隐藏）
            case R.id.img_phone_login_eye:
                if (position == 0) {
                    img_phone_login_eye.setImageResource(img[position]);
                    et_phone_login_password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    position = 1;
                } else if (position == 1) {
                    img_phone_login_eye.setImageResource(img[position]);
                    et_phone_login_password.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    position = 0;
                }
                et_phone_login_password.setSelection(et_phone_login_password.getText().length());
                break;
            //点击获取验证码触发的事件响应
            case R.id.btn_phone_login_getcode:

                //传入要修改的控件
                getcodeServices.getcode(phone_loginActivity.this, btn_phone_login_getcode, phone_login_now, et_phone_login_phone, et_phone_login_code);
                if (!TextUtils.isEmpty(et_phone_login_phone.getText().toString().trim())) {
                    if (et_phone_login_phone.getText().toString().trim().length() == 11) {
                        myphone = et_phone_login_phone.getText().toString().trim();
                        SMSSDK.getVerificationCode("86", myphone);
                        et_phone_login_code.requestFocus();
                        btn_phone_login_getcode.setVisibility(View.GONE);
                    } else {
                        ToastUtil.showToast(phone_loginActivity.this, "请输入完整电话号码");
                        et_phone_login_phone.requestFocus();
                    }
                } else {
                    ToastUtil.showToast(phone_loginActivity.this, "请输入您的电话号码");
                    et_phone_login_phone.requestFocus();
                }
                break;
            case R.id.btn_phone_login:
                if (!passwordloginFlag) {
                    //验证码登录事件响应
                    if (!TextUtils.isEmpty(et_phone_login_code.getText().toString().trim())) {
                        if (et_phone_login_code.getText().toString().trim().length() == 4) {
                            mycode = et_phone_login_code.getText().toString().trim();
                            SMSSDK.submitVerificationCode("86", myphone, mycode);
                            getcodeServices.setFlag(false);
                            Handler handler =new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    resultflag=getcodeServices.getCodeResult();
                                    if (resultflag) {
//                                        验证码正确的话登录
//                                        try {
//                                            dataBaseManager.LoginCheck_Phone(et_phone_login_phone.getText().toString().trim(), "");
//                                        } catch (DataBaseError dataBaseError) {
//
//                                        } catch (DataBaseSignal dataBaseSignal) {
//                                            if (dataBaseSignal.getSignalType() == DataBaseSignal.LoginSucceed) {
//                                                //登录成功
//                                                ToastUtil.showToast(phone_loginActivity.this, "登录成功");
//                                                转到主界面
                                                //gotoMainpage();
                                        editor = getSharedPreferences("autologin", MODE_PRIVATE).edit();
                                        editor.putString("phone", et_phone_login_phone.getText().toString());
                                       startActivity(new Intent(phone_loginActivity.this,mainPage.class));
                                                   //   finish();
//                                            } else {
//                                                ToastUtil.showToast(phone_loginActivity.this, "用户名不存在");
//                                            }
//                                        }

                                    }
                                }
                            },200);
                        } else {
                            ToastUtil.showToast(phone_loginActivity.this, "请输入完整验证码");
                            et_phone_login_code.requestFocus();
                        }
                    } else {
                        ToastUtil.showToast(phone_loginActivity.this, "请输入验证码");
                        et_phone_login_code.requestFocus();
                    }
                } else {
                    //密码登录事件响应
                    if (!TextUtils.isEmpty(et_phone_login_phone.getText().toString().trim())) {
                        if (et_phone_login_phone.getText().toString().trim().length() == 11) {
                            if (!TextUtils.isEmpty(et_phone_login_password.getText().toString().trim())) {
                                //                            try {
                                //                                DataBaseManager.LoginCheck_Phone(et_phone_login_phone.getText().toString().trim(), et_phone_login_password..getText().toString().trim());
                                //                            } catch (DataBaseError dataBaseError) {
                                //
                                //                            } catch (DataBaseSignal dataBaseSignal) {
                                //                                if (dataBaseSignal.getSignalType() == DataBaseSignal.LoginSucceed) {
                                //                                    //登录成功
                                //                                    ToastUtil.showToast(phone_loginActivity.this, "登录成功");
                                                                        //gotoMainpage();
                                                                        //   finish();
                                //                                } else {
                                //                                    ToastUtil.showToast(phone_loginActivity.this, "用户名密码错误");
                                //
                                //                                }
                                //                            }
                                //                            ToastUtil.showToast(phone_loginActivity.this, "密码登录成功");
//                                startActivity(new Intent(phone_loginActivity.this, mainPage.class));
                                editor = getSharedPreferences("autologin", MODE_PRIVATE).edit();
                                editor.putString("phone", et_phone_login_phone.getText().toString());
                                editor.putString("password", et_phone_login_password.getText().toString());
                                editor.apply();
                                startActivity(new Intent(phone_loginActivity.this,mainPage.class));


                            } else {
                                ToastUtil.showToast(phone_loginActivity.this, "请输入密码");
                            }

                        } else {

                            ToastUtil.showToast(phone_loginActivity.this, "请输入完整电话号码");
                        }
                    } else {
                        ToastUtil.showToast(phone_loginActivity.this, "请输入手机号码");
                    }

                }

                break;
        }


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //        SMSSDK.unregisterAllEventHandler();
    }
}

