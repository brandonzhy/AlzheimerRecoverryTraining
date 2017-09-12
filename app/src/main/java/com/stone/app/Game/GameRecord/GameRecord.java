package com.stone.app.Game.GameRecord;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;

import com.stone.app.R;
import com.stone.app.dataBase.GameRecordData;

import java.util.ArrayList;
import java.util.List;

import static com.stone.app.Util.staticConstUtil.GAME_JUDGAE;
import static com.stone.app.Util.staticConstUtil.GAME_PUZZLE;


public class GameRecord extends Activity {
    //    private DataBaseManager dataBaseManager;
    List<GameRecordData> glist;
    private ListView lv_puzzle, lv_judge;
    private String recordItems[] = {"拼图每日游戏记录", "拼图每月游戏记录", "图片判断每日游戏记录", "图片判断每月游戏记录"};
    private ArrayList<String> list_puzzle, list_judge;
    private String memberID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_record);
        ImageView imageView = findViewById(R.id.iv_gamerecord_leftback);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
         Intent intent = getIntent();
        memberID = intent.getStringExtra("memberID");
        list_puzzle = new ArrayList<String>();
        list_judge = new ArrayList<String>();
        for (int i = 0; i < recordItems.length; i++) {

            if (i > 1) {
                list_judge.add(recordItems[i]);
            } else {
                list_puzzle.add(recordItems[i]);
            }
        }
                ArrayAdapter<String> adapter_puzzle = new ArrayAdapter<String>(this, R.layout.single_item, list_puzzle);
                ArrayAdapter<String> adapter_judge = new ArrayAdapter<String>(this, R.layout.single_item, list_judge);
//        ArrayAdapter<String> adapter_puzzle = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, list_puzzle);
//        ArrayAdapter<String> adapter_judge =  new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, list_judge);
        lv_puzzle = findViewById(R.id.lv_gamerecord_puzzle);
        lv_judge = findViewById(R.id.lv_gamerecord_judge);
        lv_judge.setAdapter(adapter_judge);
        lv_judge.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                switch (i) {
                    case 0:
                        Log.i("TAG", "判断每日游戏记录被点击了");
                        Intent intent_judje_day = new Intent(GameRecord.this, DayRecord.class);
                        intent_judje_day.putExtra("memberID", memberID);
                        intent_judje_day.putExtra("gameType", GAME_JUDGAE);
                        startActivity(intent_judje_day);
                        finish();
                        break;
                    case 1:
                        Log.i("TAG", "判断每月游戏记录被点击了");
                        Intent intent_judje_month = new Intent(GameRecord.this, JudgeMonthRecord.class);
                        intent_judje_month.putExtra("memberID", memberID);
                        intent_judje_month.putExtra("gameType", GAME_JUDGAE);
                        startActivity(intent_judje_month);
                        finish();
                        break;
                }
            }
        });
        lv_puzzle.setAdapter(adapter_puzzle);
        lv_puzzle.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                switch (i) {
                    case 0:
                        Log.i("TAG", "拼图每日游戏记录被点击了");
                        Intent intent_puzzle_day = new Intent(GameRecord.this, DayRecord.class);
                        intent_puzzle_day.putExtra("memberID", memberID);
                        intent_puzzle_day.putExtra("gameType", GAME_PUZZLE);
                        startActivity(intent_puzzle_day);
                        finish();
                        break;
                    case 1:
                        Log.i("TAG", "拼图每月游戏记录被点击了");
                        Intent intent_puzzle_month = new Intent(GameRecord.this, PuzzleMonthRecord.class);
                        intent_puzzle_month.putExtra("memberID", memberID);
                        intent_puzzle_month.putExtra("gameType", GAME_PUZZLE);
                        startActivity(intent_puzzle_month);
                        finish();
                        break;
                }
            }
        });
        //        dataBaseManager = new DataBaseManager();
        //        ArrayAdapter<String> adapter = new ArrayAdapter<String>(GameRecord.this, android.R.layout.simple_list_item_1, recordItems);


        //        glist = dataBaseManager.getGameRecordList("", memberID, GAME_JUDGAE, 0, 0);
        //        Button btn_gamerecord_back = (Button) findViewById(R.id.btn_gamerecord_back);
        //        btn_gamerecord_back.setOnClickListener(new View.OnClickListener() {
        //            @Override
        //
        //            public void onClick(View v) {
        //                finish();
        //            }
        //        });
    }


}
