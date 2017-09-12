package com.stone.app.Game.GameRecord;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;

import com.stone.app.R;
import com.stone.app.Util.ToastUtil;
import com.stone.app.dataBase.DataBaseError;
import com.stone.app.dataBase.DataBaseManager;
import com.stone.app.dataBase.GameRecordData;
import com.stone.app.dataBase.RealmDB;

import java.util.ArrayList;
import java.util.List;

import static com.stone.app.dataBase.DataBaseError.ErrorType.RequiredResultsReturnNULL;


public class DayRecord extends Activity implements View.OnClickListener {
    private List<GameItem> gameItemList = new ArrayList<GameItem>();
    private String memberID;
    private DataBaseManager dataBaseManager;
    private GameRecordAdapter adapter;
    private List<GameRecordData> glist;
    private ListView listView;
    private int gameType;
private ImageView tv_left_back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_day_record);
        tv_left_back=findViewById(R.id.tv_left_back);
        tv_left_back.setOnClickListener(this);
        Intent intent = getIntent();
        memberID = intent.getStringExtra("memberID");
        dataBaseManager= RealmDB.getDataBaseManager();
        gameType = intent.getIntExtra("gameType", -1);
        Log.i("TAG", "memberID= " + memberID + "gameType =" + gameType);
        listView = findViewById(R.id.lv_dayrecord);
        try {
            glist = dataBaseManager.getGameRecordList("", memberID, gameType, 0, 0);
            if(glist!=null){
                initData(gameType);
                adapter = new GameRecordAdapter(this, R.layout.game_item, gameItemList);
                listView.setAdapter(adapter);
            }
        } catch (DataBaseError dataBaseError) {
            dataBaseError.printStackTrace();
            Log.i("TAG","add gameRecord 的 dataBaseError=  " +dataBaseError.getErrorType() );
            if(RequiredResultsReturnNULL==dataBaseError.getErrorType()){
                ToastUtil.showToast(DayRecord.this,"游戏记录不存在哦，先去玩游戏吧");
                finish();
            }
            Log.i("TAG", "浏览失败，错误类型为: " + dataBaseError.getErrorType());
        }
    }

    private void initData(int Type) {
        for (GameRecordData recordData : glist) {
            String date = String.valueOf(recordData.getDate()).substring(0, 14);
            StringBuffer sbf = new StringBuffer(date);
            //插入后stringBuffer长度改变，每次加三
            sbf.insert(4, "年");
            sbf.insert(7, "月");
            sbf.insert(10, "日");
            sbf.insert(13, "时");
            sbf.insert(16, "分");
            sbf.insert(19, "秒");

            date = sbf.toString();
                //判断
                GameItem gameItem = new GameItem(date, recordData.getFactor(),gameType);
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
