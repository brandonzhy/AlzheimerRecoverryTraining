package com.stone.app.Game.GameRecord;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;

import com.stone.app.R;
import com.stone.app.dataBase.DataBaseManager;
import com.stone.app.dataBase.GameRecordData;

import java.util.ArrayList;
import java.util.List;

import static com.stone.app.Util.staticConstUtil.GAME_JUDGAE;


public class JudgeDayRecord extends Activity {
    private List<GameItem> gameItemList = new ArrayList<GameItem>();
    private String memberID;
    private DataBaseManager dataBaseManager;
    private GameRecordAdapter adapter;
    private List<GameRecordData> glist;
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_day_record);
        memberID = getIntent().getStringExtra("memberID");
        listView = findViewById(R.id.lv_dayrecord);
        glist = dataBaseManager.getGameRecordList("", memberID, GAME_JUDGAE, 0, 0);
        initData();
        adapter = new GameRecordAdapter(this, R.layout.game_item, gameItemList);
        listView.setAdapter(adapter);
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
}
