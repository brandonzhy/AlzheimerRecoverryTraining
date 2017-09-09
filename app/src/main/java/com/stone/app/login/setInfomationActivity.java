package com.stone.app.login;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;

import com.stone.app.R;
import com.stone.app.dataBase.DataBaseError;
import com.stone.app.dataBase.DataBaseManager;
import com.stone.app.dataBase.MemberData;
import com.stone.app.dataBase.RealmDB;
import com.stone.app.mainPage.mainPage;

import static com.stone.app.Util.staticConstUtil.FEMALE;
import static com.stone.app.Util.staticConstUtil.MALE;


public class setInfomationActivity extends Activity implements View.OnClickListener, RadioGroup.OnCheckedChangeListener {
    private EditText et_info_familyname, et_info_gender, et_info_nickname, et_info_name;
    private Button bt_info_submit;
    private int gendrType = 0;
    private DataBaseManager dataBaseManager;        //数据库模块
    private RadioGroup rb_gender;
    MemberData memberData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setinfomation);
        init();

    }

    private void init() {
        //        et_info_gender=findViewById(R.id.et_info_gender);
        et_info_nickname = findViewById(R.id.et_info_nickname);
        et_info_name = findViewById(R.id.et_info_name);
        bt_info_submit = findViewById(R.id.bt_info_submit);
        rb_gender = findViewById(R.id.rb_gender);
        rb_gender.setOnCheckedChangeListener(setInfomationActivity.this);
        bt_info_submit.setOnClickListener(setInfomationActivity.this);
//        dataBaseManager = new DataBaseManager();    //数据库模块
        dataBaseManager= RealmDB.getDataBaseManager();
    }

    @Override
    public void onClick(View view) {
        Intent intent = getIntent();
        // 数据库模块

        try {
//            memberData = dataBaseManager.AddMember(et_info_nickname.getText().toString(), intent.getStringExtra("password"),
//                    "", et_info_name.getText().toString(),
//                    gendrType, intent.getStringExtra("phone"));
            long phone = 111111110 + dataBaseManager.getPhoneList( "","").size();

            memberData = dataBaseManager.AddMember(et_info_nickname.getText().toString(),"123456" ,
                    "", et_info_name.getText().toString(),
                    gendrType, "11"+String.valueOf(phone));
            Intent intentmainPage = new Intent(setInfomationActivity.this, mainPage.class);
            if (memberData != null) {
                String memberID = memberData.getID();
                String memberName = memberData.getName();
                String memberNickName = memberData.getNickName();
                int memberGender = memberData.getGender();

                intentmainPage.putExtra("memberID", memberID);
                intentmainPage.putExtra("memberName", memberName);
                intentmainPage.putExtra("memberNickName", memberNickName);
                intentmainPage.putExtra("memberGender", memberGender);
                Log.i("TAG","memberID= " +memberID );
                //PreferenceManager.getDefaultSharedPreferences利用包名来命名SharedPreferences文件
                SharedPreferences.Editor editor = getSharedPreferences("autologin", MODE_PRIVATE).edit();
                editor.putBoolean("isFirstLogin",true);
                editor.putString("memberID",memberID);
                editor.apply();
                Log.i("TAG","登陆成功啦啦啦" );
            }
            startActivity(intentmainPage);
            finish();


        } catch (DataBaseError dataBaseError) {
            dataBaseError.printStackTrace();
            Log.i("TAG","注册失败，错误类型为: " +dataBaseError.getErrorType() );
        }

        //转到主界面
        //gotomainpage

    }

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, @IdRes int i) {
        switch (radioGroup.getCheckedRadioButtonId()) {
            case R.id.rb_info_male:
                gendrType = MALE;
                Log.i("TAG", "性别选择为男");
                break;
            case R.id.rb_info_female:
                gendrType = FEMALE;
                Log.i("TAG", "性别选择为女");
                break;

        }
    }
}
