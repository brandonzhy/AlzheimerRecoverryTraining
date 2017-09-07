package com.stone.app.Game.game_puzzle;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.stone.app.R;
import com.stone.app.Game.game_judge.game_judgeActivity;

import static com.stone.app.R.id.btn_game_puzzle;


public class game_centre extends Activity implements View.OnClickListener {

    String memberID;
    //进入的初始页面
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_centre);
        Intent intent=getIntent();
        memberID=intent.getStringExtra("memberID");
        Button btn_game_puzzle = (Button) findViewById(R.id.btn_game_puzzle);
        Button btn_game_judge = (Button) findViewById(R.id.btn_game_judge);
        ImageView img_gamecenter_back= (ImageView) findViewById(R.id.img_gamecenter_back);
        btn_game_judge.setOnClickListener(this);
        btn_game_puzzle.setOnClickListener(this);

        //        Diffcult.setOnClickListener(new View.OnClickListener() {
        //            @Override
        //
        //                public void onClick(View V){
        //                Intent intent = new Intent(game_centre.this, gamestart.class);
        //                  startActivityForResult(intent,1);
        //                }
        //            }
        //        );


    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_game_judge:
                Intent intent_judge = new Intent(game_centre.this, game_judgeActivity.class);
                intent_judge.putExtra("memberID",memberID);
                startActivity(intent_judge);
                Log.i("TAG","判断题按钮被点击" );
                break;
            case btn_game_puzzle:
                Intent intent_puzzle=new Intent(game_centre.this,gamestart.class);
                intent_puzzle.putExtra("memberID",memberID);
                startActivity(intent_puzzle);
                Log.i("TAG","拼图题按钮被点击" );
                break;
            case R.id.img_gamecenter_back:
               finish();
                break;

        }
    }
}

