package com.stone.app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class showResultActivity extends Activity {

    private numberView tv_result;
    private float result;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_result);
        tv_result = findViewById(R.id.tv_result);
        Intent intent = getIntent();
        result = intent.getFloatExtra("result",0);
        tv_result.showNumberWithAnimation(result, numberView.FLOATREGEX);
    }


}
