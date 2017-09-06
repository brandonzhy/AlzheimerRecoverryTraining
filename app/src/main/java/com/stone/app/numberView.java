package com.stone.app;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.TextView;

/**
 * Created by Brandon Zhang on 2017/9/4.
 */

public class numberView extends TextView {
    //动画时长
    private int duration = 1500;
    //显示数字
    private float number;
    //显示表达式
    private String regex;

    //显示表示式
    public static final String INTREGEX = "%1$01.0f";//不保留小数，整数
    public static final String FLOATREGEX = "%1$01.2f";//保留2位小数


    public numberView(Context context , AttributeSet attr) {
        super(context,attr);
    }


    public void showNumberWithAnimation(float number, String regex) {

        this.regex = regex;

        //修改number属性，会调用setNumber方法
        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(this, "number", 0, number);
        objectAnimator.setDuration(duration);
        //加速器，从慢到快到再到慢
        objectAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        objectAnimator.start();
        Log.i("TAG","showNumber 被调用了" +number );

    }



     //* 根据正则表达式，显示对应数字样式

    public void setNumber(float number) {
        this.number = number;
        setText("正确率为：" + String.format(regex, number) + "%");
        Log.i("TAG","setNumber 被调用了" +number );
    }
}

