package com.stone.app.mainPage;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.stone.app.R;
import com.stone.app.photoBroswer.photoBroswerActivity;

import static com.stone.app.R.id.btn_uploadphoto;


public class MyInformation extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       setContentView(R.layout.activity_my_information);
        Button btn_detail = (Button) findViewById(R.id.btn_detail);
        Button btn_game_record = (Button) findViewById(R.id.btn_game_record);
        Button btn_uploadedphoto = (Button) findViewById(btn_uploadphoto);
        Button btn_myinfo_back = (Button) findViewById(R.id.btn_myinfo_back);
        btn_detail.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View v) {
                Intent intent = new Intent(MyInformation.this, basicInformation.class);
                intent.putExtra("name","张三");
                Log.i("TAG","详细信息被点击了" );
                startActivity(intent);
            }
        });
        btn_uploadedphoto.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View v) {
                Intent intent = new Intent(MyInformation.this, photoBroswerActivity.class);
                startActivityForResult(intent,1);
            }
        });
        btn_game_record.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View v) {
                Intent intent = new Intent(MyInformation.this, GameRecord.class);
                startActivityForResult(intent,1);
            }
        });
        btn_myinfo_back.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View v) {
                finish();
            }
        });
    }
}
