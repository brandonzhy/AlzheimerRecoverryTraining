package com.stone.app.Game.game_puzzle;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.stone.app.R;
import com.stone.app.Util.DateUtil;
import com.stone.app.dataBase.DataBaseError;
import com.stone.app.dataBase.DataBaseSignal;
import com.stone.app.dataBase.RealmDB;
import com.stone.app.style_young.mainpageYoung;

import static com.stone.app.Util.staticConstUtil.GAME_PUZZLE;


//游戏页面

public class gamestart extends Activity {


    private GamePintuLayout mgamePintuLayout;
    private int gametiem;
    private String memberID;
    int gamelevel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        //        ViewGroup viewGroup = (ViewGroup) LayoutInflater.from(this).inflate(R.layout.activity_gamestart2, null);

        setContentView(R.layout.activity_gamestart2);
        memberID=getIntent().getStringExtra("memberID");
        //建立接口
        //        Button back=new Button(this);
        //        mgamePintuLayout=new GamePintuLayout(this);
        //        viewGroup.addView(mgamePintuLayout);
        //        viewGroup.addView(back);

        mgamePintuLayout = (GamePintuLayout) findViewById(R.id.id_gamepintu);
        mgamePintuLayout.setMemberID(memberID);
        mgamePintuLayout.setTimeEnabled();
        //返回游戏选择界面
        Button back = (Button) findViewById(R.id.button3);

        back.setOnClickListener(new View.OnClickListener() {

                                    @Override
                                    public void onClick(View V) {
                                        //mgamePintuLayout.backgame();//退出游戏后的初始化
                                        uploadRecord();
                                        Intent intent = new Intent(gamestart.this, mainpageYoung.class);
                                        startActivity(intent);
                                        mgamePintuLayout.backgame();
                                        finish();
                                    }
                                }
        );
        //弹窗，拼成后弹出

        mgamePintuLayout.setOnGamemListener(new GamePintuLayout.GamePintuListener() {
            @Override
            public void gamefinish() {
                mgamePintuLayout.backgame();

                finish();
            }

            @Override
            public void nextLevel() {

                TextView mlevel = findViewById(R.id.textView1);
                //Log.i("tag","next leval of game is called");
//                new AlertDialog.Builder(gamestart.this).setTitle("升级").setMessage("祝贺!!! 可以提高难度").
//                        setPositiveButton("难度升级", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
                                 gamelevel = mgamePintuLayout.gamelevel();
////                                Log.i("TAG","gamelevel  = " + gamelevel);
//                                //显示下一关的时间
//
//                                //Toast.makeText( gamestart.this,"this is  "+gametiem,Toast.LENGTH_SHORT).show();
//
////                                mlevel.setText("关卡" + gamelevel);
//
//                                //
//
//                            }
//                        }).show();
            }

            @Override
            public void timechanged() {
                int mTime = mgamePintuLayout.gametimechange();
                TextView gameTime = findViewById(R.id.textView2);
                gameTime.setText("剩余" + mTime + "秒");
                //Toast.makeText( gamestart.this,"还剩下  "+time,Toast.LENGTH_SHORT).show();
            }

            //            @Override
            //            public void timechanged(int ctime) {
            //                Log.i("tag","time changed is called");
            //
            //
            //            }
            //游戏失败弹窗，退出游戏
            @Override
            public void gameover() {
                new AlertDialog.Builder(gamestart.this).setTitle("失败").setMessage("Game Over!!!").
                        setPositiveButton("退出", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                uploadRecord();
                                finish();
                            }
                        }).show();

            }


        });
    }

    private void uploadRecord() {
        String memberID = getIntent().getStringExtra("memberID");
//        int time_cost = mgamePintuLayout.getmTime() - mgamePintuLayout.gametimechange();

        try {
            RealmDB.getDataBaseManager().AddGameRecord(memberID, (double) gamelevel, DateUtil.getTime(), GAME_PUZZLE);
        } catch (DataBaseSignal dataBaseSignal) {
            dataBaseSignal.printStackTrace();
        } catch (DataBaseError dataBaseError) {
            dataBaseError.printStackTrace();
        }
    }
}
