//package com.stone.app.login;
//
//import android.app.Activity;
//import android.content.Intent;
//import android.content.SharedPreferences;
//import android.os.Bundle;
//import android.util.Log;
//import android.view.animation.AlphaAnimation;
//import android.view.animation.Animation;
//import android.widget.ImageView;
//
//import com.stone.app.R;
//
//
//public class MainActivity extends Activity {
//    private SharedPreferences pref;
//    private ImageView welcomeImg = null;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//        welcomeImg = findViewById(R.id.welcome_img);
//        pref = getSharedPreferences("autologin", MODE_PRIVATE);
//        AlphaAnimation anima = new AlphaAnimation(0.3f, 1.0f);
//        anima.setDuration(3500);// 设置动画显示时间
//        //        anima.setRepeatCount(2);
//        welcomeImg.startAnimation(anima);
//        anima.setAnimationListener(new AnimationImpl());
//        //
//    }
//
//    private class AnimationImpl implements Animation.AnimationListener {
//
//        @Override
//        public void onAnimationStart(Animation animation) {
//            welcomeImg.setBackgroundResource(R.mipmap.boot2);
////            welcomeImg.setBackgroundResource(R.mipmap.boot3);
//        }
//
//        @Override
//        public void onAnimationEnd(Animation animation) {
//            skip(); // 动画结束后跳转到别的页面
//        }
//
//        @Override
//        public void onAnimationRepeat(Animation animation) {
//
//        }
//
//    }
//
//    private void skip() {
//       // 检验本地是否有登录信息，有的话自动登陆
//        if (pref.getString("phone", "").isEmpty() && pref.getString("password", "").isEmpty()) {
//            //跳转到 LoginActivity，最后换成gotoLoginActivity
//        startActivity(new Intent(MainActivity.this, loginActivity.class));
//                 //   gotoLoginActivity();
//            Log.i("TAG", "没有自动登陆");
//        } else {
//            //自动转到主界面
//            //gotoMainpage();
//            Log.i("TAG", "自动登陆");
//        }
//        finish();
//    }
//
//}
