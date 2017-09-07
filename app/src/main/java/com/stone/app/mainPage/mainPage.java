package com.stone.app.mainPage;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.stone.app.R;
import com.stone.app.dataBase.DataBaseManager;
import com.stone.app.game_puzzle.game_centre;
import com.stone.app.photoUpload.photoUploadActivity;


public class mainPage extends Activity {
    private DataBaseManager dataBaseManager;
    private Intent intent;
    String memberID;
    String memberNickName;
    String memberName;
    int memberGender;
    private String memberFamilyID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_page);
        dataBaseManager = new DataBaseManager();
        Button btn_info = (Button) findViewById(R.id.btn_info);
        Button btn_game = (Button) findViewById(R.id.btn_game);
        Button btn_uploadpicture = (Button) findViewById(R.id.btn_uploadpicture);
        //Util  获得信息
        intent = getIntent();
        memberID = intent.getStringExtra("memberID");
        memberNickName = intent.getStringExtra("memberNickName");
        memberFamilyID = intent.getStringExtra("memberFamilyID");
        memberName = intent.getStringExtra("memberName");
        memberGender = intent.getIntExtra("memberGender", 0);
        //        Button btn_uploadpicture = (Button) findViewById(R.id.btn_uploadpicture);
        btn_uploadpicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentphotoUP = new Intent(mainPage.this, photoUploadActivity.class);
                intentphotoUP.putExtra("memberID", memberID);
                startActivity(intentphotoUP);
            }
        });
        btn_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //                intent.getStringExtra("memberName");
                //                intent.getStringExtra("memberNickName");
                //                intent.getIntExtra("memberGender",0);
                Intent intentinfo = new Intent(mainPage.this, MyInformation.class);
                intentinfo.putExtra("memberID", memberID);
                intentinfo.putExtra("memberName", memberName);
                intentinfo.putExtra("memberNickName", memberNickName);
                intentinfo.putExtra("memberGender", memberGender);
                intentinfo.putExtra("memberFamilyID", memberFamilyID);

                startActivityForResult(intentinfo, 1);
            }
        });
        btn_game.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View V) {
                Intent intentgame = new Intent(mainPage.this, game_centre.class);
                intentgame.putExtra("memberID", memberID);
                startActivityForResult(intentgame, 1);
            }
        });
    }
}
