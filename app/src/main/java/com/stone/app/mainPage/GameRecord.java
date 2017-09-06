package com.stone.app.mainPage;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.stone.app.R;


public class GameRecord extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_record);
        Button btn_gamerecord_back = (Button) findViewById(R.id.btn_gamerecord_back);
        btn_gamerecord_back.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View v) {
                finish();
            }
        });
    }
}
