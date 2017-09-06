package com.stone.app.login;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;

import com.stone.app.R;
import com.stone.app.mainPage.mainPage;

import static com.stone.app.Util.staticConstUtil.FEMALE;
import static com.stone.app.Util.staticConstUtil.MALE;


public class setInfomationActivity extends Activity implements View.OnClickListener, RadioGroup.OnCheckedChangeListener {
    private EditText et_info_familyname, et_info_gender, et_info_nickname, et_info_name;
    private Button bt_info_submit;
    private int gendrType = 0;

    //    private DataBaseManager dataBaseManager             //数据库模块
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setinfomation);
        init();

    }

    private void init() {
        et_info_familyname = findViewById(R.id.et_info_familyname);
        //        et_info_gender=findViewById(R.id.et_info_gender);
        et_info_nickname = findViewById(R.id.et_info_nickname);
        et_info_name = findViewById(R.id.et_info_name);
        bt_info_submit = findViewById(R.id.bt_info_submit);
        bt_info_submit.setOnClickListener(setInfomationActivity.this);
        //        dataBaseManager=new DataBaseManager();    //数据库模块
    }

    @Override
    public void onClick(View view) {
        Intent intent = getIntent();
        //数据库模块
        //        try {
        //            dataBaseManager.AddHomelessMember(et_info_nickname.getText().toString(),intent.getStringExtra("password"),
        //                     et_info_familyname.getText().toString(),et_info_name.getText().toString(),
        //                    gendrType,  intent.getStringExtra("phone"));
        //        } catch ( DataBaseError dataBaseError) {
        //
        //        }

        //转到主界面
        //gotomainpage
        startActivity(new Intent(setInfomationActivity.this,mainPage.class));
        finish();
    }

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, @IdRes int i) {
        switch (radioGroup.getCheckedRadioButtonId()) {
            case R.id.rb_info_male:
                gendrType = MALE;
                break;
            case R.id.rb_info_female:
                gendrType =FEMALE;
                break;

        }
    }
}
