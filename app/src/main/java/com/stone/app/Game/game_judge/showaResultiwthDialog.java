package com.stone.app.Game.game_judge;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.Button;

import com.stone.app.R;
import com.stone.app.Util.DateUtil;
import com.stone.app.dataBase.DataBaseError;
import com.stone.app.dataBase.DataBaseManager;
import com.stone.app.dataBase.DataBaseSignal;
import com.stone.app.dataBase.RealmDB;
import com.stone.app.style_young.mainpageYoung;

import static com.stone.app.Util.staticConstUtil.GAME_JUDGAE;

public class showaResultiwthDialog extends Activity implements View.OnClickListener {
    private int duration = 2500;
    private float pf = 0.9f;
    private Button btn_back;
    DiaCharView chart6 = null;
    DataBaseManager dataBaseManager;
    String memberID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_dial_chart3);
        //		chart = (DialChart03View)findViewById(R.id.circle_view);
        chart6 = (DiaCharView) findViewById(R.id.circle_view2);
        btn_back = findViewById(R.id.btn_back);
        btn_back.setOnClickListener(this);
        Intent intent = getIntent();
        pf = intent.getFloatExtra("result", 0);
        memberID = intent.getStringExtra("memberID");
        dataBaseManager = RealmDB.getDataBaseManager();
        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(this, "pf", 0, pf);
        objectAnimator.setDuration(duration);
        //加速器，从慢到快到再到慢
        objectAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        objectAnimator.start();


    }

    public void setPf(float number) {
        this.pf = number;
        chart6.setCurrentStatus(pf);
        chart6.invalidate();
    }


    @Override
    public void onClick(View view) {
        //        返回游戏界面
        //        gotoGamePage();
        try {
            long date = DateUtil.getTime();
            Log.i("TAG", " 判断游戏上传的date= " + date);
            dataBaseManager.AddGameRecord(memberID, (double) pf, date, GAME_JUDGAE);
        } catch (DataBaseSignal dataBaseSignal) {
            dataBaseSignal.printStackTrace();
        } catch (DataBaseError dataBaseError) {
            dataBaseError.printStackTrace();
        }
        startActivity(new Intent(showaResultiwthDialog.this, mainpageYoung.class));
        finish();
    }
}
