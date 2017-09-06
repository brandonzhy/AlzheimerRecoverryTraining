package com.stone.app.mainPage;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.stone.app.R;
import com.stone.app.game_puzzle.game_centre;
import com.stone.app.photoUpload.photoUploadActivity;


public class mainPage extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_page);
        Button btn_info = (Button) findViewById(R.id.btn_info);
        Button btn_game = (Button) findViewById(R.id.btn_game);
        Button btn_uploadpicture = (Button) findViewById(R.id.btn_uploadpicture);
//        Button btn_uploadpicture = (Button) findViewById(R.id.btn_uploadpicture);
        btn_uploadpicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(mainPage.this,photoUploadActivity.class));
            }
        });
        btn_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mainPage.this,MyInformation.class);
                startActivityForResult(intent,1);
            }
        });
        btn_game.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View V){
                Intent intent = new Intent(mainPage.this, game_centre.class);
                startActivityForResult(intent,1);
            }
        });
    }
}
