package com.stone.app.mainPage;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.stone.app.R;
import com.stone.app.dataBase.DataBaseManager;
import com.stone.app.dataBase.GameRecordData;

import java.util.List;

import static com.stone.app.Util.staticConstUtil.GAME_JUDGAE;


public class GameRecord extends Activity {
    private DataBaseManager dataBaseManager;
    List<GameRecordData>glist;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_record);
        dataBaseManager=new DataBaseManager();
        Intent intent=getIntent();

        glist= dataBaseManager.getGameRecordList("",intent.getStringExtra("memberID"), GAME_JUDGAE,0,0);
        Button btn_gamerecord_back = (Button) findViewById(R.id.btn_gamerecord_back);
        btn_gamerecord_back.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View v) {
                finish();
            }
        });
    }
}
