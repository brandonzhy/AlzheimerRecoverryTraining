package com.stone.app.Game.GameRecord;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

import com.stone.app.R;
import com.stone.app.dataBase.DataBaseError;
import com.stone.app.dataBase.DataBaseManager;
import com.stone.app.dataBase.GameRecordData;
import com.stone.app.dataBase.RealmDB;

import java.util.ArrayList;
import java.util.List;


public class DayRecord extends Activity implements View.OnClickListener {
    private List<GameItem> gameItemList = new ArrayList<GameItem>();
    private String memberID;
    private DataBaseManager dataBaseManager;
    private GameRecordAdapter adapter;
    private List<GameRecordData> glist;
    private ListView listView;
    private int gameType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_day_record);
        Intent intent = getIntent();
        memberID = intent.getStringExtra("memberID");
        dataBaseManager= RealmDB.getDataBaseManager();
        gameType = intent.getIntExtra("gameType", -1);
        Log.i("TAG", "memberID= " + memberID + "gameType =" + gameType);
        listView = findViewById(R.id.lv_dayrecord);
        try {
            glist = dataBaseManager.getGameRecordList("", memberID, gameType, 0, 0);
            if(glist!=null){

                initData();
                adapter = new GameRecordAdapter(this, R.layout.game_item, gameItemList);
                listView.setAdapter(adapter);
            }
        } catch (DataBaseError dataBaseError) {
            dataBaseError.printStackTrace();
            Log.i("TAG", "浏览失败，错误类型为: " + dataBaseError.getErrorType());
        }
    }

    private void initData() {
        for (GameRecordData recordData : glist) {
            String date = String.valueOf(recordData.getDate()).substring(0, 12);
            StringBuffer sbf = new StringBuffer(date);
            sbf.insert(4, "年");
            sbf.insert(6, "月");
            sbf.insert(8, "日");
            sbf.insert(10, "时");
            sbf.insert(12, "分");
            date = sbf.toString();
            GameItem gameItem = new GameItem(date, recordData.getFactor());
            gameItemList.add(gameItem);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_left_back:
                finish();
                break;
        }
    }
}
