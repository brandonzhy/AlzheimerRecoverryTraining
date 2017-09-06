package com.stone.app.login;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;

import static com.mob.tools.utils.R.getStringRes;


/**
 * Created by Brandon Zhang on 2017/8/25.
 */

public class getcodeServices {
    private Handler handlerText, myhandler;
    private Context mycontext;
    private Button mybutton;
    private TextView mytextView;
    private EditText myphone, mycode;
    private boolean myflag = true;
    private int time = 60;
    private boolean resultflag;


    public getcodeServices(Context context) {
        initSMSSDK(context);
        init();
    }

    private void init() {
        handlerText = new Handler() {
            //收到message，接下来要干什么事情
            public void handleMessage(Message msg) {
                if (msg.what == 1) {
                    if (time > 0) {
                        mytextView.setText("重新获取" + time + "秒");
                        time--;
                        handlerText.sendEmptyMessageDelayed(1, 1000);
                    } else {
                        time = 60;
                        mytextView.setVisibility(View.GONE);
                        mybutton.setVisibility(View.VISIBLE);
                    }
                } else {
                    //                    mytextView.setText("");
                    //                    mycode.setText("");
                    time = 60;
                    mytextView.setVisibility(View.GONE);
                    mybutton.setVisibility(View.VISIBLE);
                }
            }
        };
        EventHandler eventHandler = new EventHandler() {
            @Override
            public void afterEvent(int event, int result, Object data) {
                Message msg = new Message();
                msg.arg1 = event;
                msg.arg2 = result;
                msg.obj = data;
                //发送信息,将massage对象发送出去
                //
                myhandler.sendMessage(msg);
            }
        };
        SMSSDK.registerEventHandler(eventHandler);
    }


    public boolean getCodeResult() {
      return  resultflag;
    }

    private void setCodeResult(boolean flag) {
        resultflag = flag;
        Log.i("TAG", "setCodeResult被调用，flag的值为" + resultflag+"时间为 "+System.currentTimeMillis());
    }

    public void setFlag(boolean flag) {
        myflag = flag;
    }

    private void initSMSSDK(Context context) {
        //    public void initSMSSDK(Context context) {
        mycontext = context;
//        SMSSDK.initSDK(mycontext, "206fe0b01b336", "f41b38b4e818ac72e4ce62d7763ba8b9");
                                SMSSDK.initSDK(mycontext, "11e440d09d630", "46c4a6a718f7204860ad768d5a13c5e1");

    }

    public void getcode(Context context, Button btn, TextView textView, EditText phone, EditText code) {
        mybutton = btn;
        mytextView = textView;
        myphone = phone;
        mycode = code;
        mycontext = context;


        myhandler = new Handler() {

            @Override
            public void handleMessage(Message msg) {
                // TODO Auto-generated method stub
                //Subclasses must implement this to receive messages.
                super.handleMessage(msg);
                int event = msg.arg1;
                int result = msg.arg2;
                Object data = msg.obj;
                Log.e("event", "event=" + event);
                if (result == SMSSDK.RESULT_COMPLETE) {

                    if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE) {//服务器验证码发送成功
                        mytextView.setVisibility(View.VISIBLE);
                        handlerText.sendEmptyMessageDelayed(1, 200);
                        Toast.makeText(mycontext, "验证码已经发送", Toast.LENGTH_SHORT).show();
                    } else if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {//提交验证码成功,验证通过
                        Log.i("TAG", "验证码校验成功");
                        setCodeResult(true);
                        handlerText.sendEmptyMessage(2);
                        //                        handlerText.sendEmptyMessage(1);

                        //短信注册成功后，返回MainActivity
                        //                        Intent intent = new Intent(RegisterActivity.this, loginActivity.class);
                        //                        startActivity(intent);
                    }
                } else {
                    if (myflag) {
                        mybutton.setVisibility(View.VISIBLE);
                        Toast.makeText(mycontext, "验证码获取失败，请重新获取", Toast.LENGTH_SHORT).show();
                        setCodeResult(false);
                        myphone.requestFocus();
                    } else {
                        //已经确认输入的验证码是四位数，但是没有得到服务端的确认，那就是输入错误
                        ((Throwable) data).printStackTrace();
                        int resId = getStringRes(mycontext, "smssdk_network_error");
                        Toast.makeText(mycontext, "验证码错误", Toast.LENGTH_SHORT).show();
                        setCodeResult(false);
                        mycode.selectAll();
                        if (resId > 0) {
                            Toast.makeText(mycontext, resId, Toast.LENGTH_SHORT).show();
                        }
                    }

                }

            }

        };
        //注册监听器


    }


}
