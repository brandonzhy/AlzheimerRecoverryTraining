package com.stone.app.addMember;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.stone.app.R;
import com.stone.app.Util.ToastUtil;
import com.stone.app.dataBase.DataBaseError;
import com.stone.app.dataBase.DataBaseManager;
import com.stone.app.dataBase.DataBaseSignal;
import com.stone.app.dataBase.RealmDB;

import static com.stone.app.dataBase.DataBaseSignal.SignalType.FamilyUpdated;


public class modifyFamilyName extends Activity  implements View.OnClickListener {

    private DataBaseManager dataBaseManager;
    private String familyID;
    EditText et_update_familyName;
    private String familyName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_family_name);
        et_update_familyName=findViewById(R.id.et_update_familyName);
        Button btn_modify_rightsave=findViewById(R.id.btn_modify_rightsave);
        ImageView iv_modify_leftback=findViewById(R.id.iv_modify_leftback);
        btn_modify_rightsave.setOnClickListener(this);
        iv_modify_leftback.setOnClickListener(this);
        Intent intent=getIntent();
        familyID=intent.getStringExtra("familyID");
        dataBaseManager= RealmDB.getDataBaseManager();
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.btn_modify_rightsave:
                Log.i("TAG","修改家庭名保存"  );
                familyName=et_update_familyName.getText().toString().trim();
                if(!TextUtils.isEmpty( familyName)){

                    try {
                        dataBaseManager.UpdateFamily(familyID,familyName,"");
                    } catch (DataBaseError dataBaseError) {
                        dataBaseError.printStackTrace();
                    } catch (DataBaseSignal dataBaseSignal) {
                        dataBaseSignal.printStackTrace();
                        if(dataBaseSignal.getSignalType()==FamilyUpdated){
                            ToastUtil.showToast(modifyFamilyName.this,"修改成功");
                            finish();
                        }
                    }
                }else {
                    ToastUtil.showToast(modifyFamilyName.this,"请输入一个名字");
                }
            break;
            case R.id.iv_modify_leftback:
                Log.i("TAG","返回上一级"  );
                finish();
                break;
        }

    }
}
