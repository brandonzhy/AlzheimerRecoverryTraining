package com.stone.app.mainPage;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.stone.app.R;


public class ChangeMessage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_message);
        Button btn = (Button)findViewById(R.id.button_41);//获取按钮
        btn.setOnClickListener(new View.OnClickListener() {		//设置按钮单击事件
            @Override
            public void onClick(View v) {
                EditText name1 = (EditText)findViewById(R.id.editText);//获取edittext组件
                TextView name2 = (TextView)findViewById(R.id.textView10);//获取textview组件
                String name3 = name1.getText().toString();//获取edittext中填写的内容
                name2.setText(name3);//在textview中显示
               Intent intent=new Intent();
                intent.putExtra("name",name3);
                setResult(RESULT_OK,intent);
                finish();
            }

    });
}
}