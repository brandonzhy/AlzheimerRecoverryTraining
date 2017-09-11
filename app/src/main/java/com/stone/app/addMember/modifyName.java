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

import static com.stone.app.dataBase.DataBaseError.ErrorType.IllegalName_DisapprovedCharacter;
import static com.stone.app.dataBase.DataBaseSignal.SignalType.FamilyUpdated;
import static com.stone.app.dataBase.DataBaseSignal.SignalType.MemberUpdated;


public class modifyName extends Activity implements View.OnClickListener {

    private DataBaseManager dataBaseManager;
    private String familyID;
    EditText et_update_familyName;
    private String name, memberID;
    private int modifyType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_family_name);

        Intent intent = getIntent();
        familyID = intent.getStringExtra("familyID");
        modifyType = intent.getIntExtra("modifyType", 10);
        memberID = intent.getStringExtra("memberID");
        Log.i("TAG", "familyID " + familyID + " modifyType " + modifyType + " memberID " + memberID);
        et_update_familyName = findViewById(R.id.et_update_familyName);

        Button btn_modify_rightsave = findViewById(R.id.btn_modify_rightsave);
        ImageView iv_modify_leftback = findViewById(R.id.iv_modify_leftback);
        btn_modify_rightsave.setOnClickListener(this);
        iv_modify_leftback.setOnClickListener(this);
        dataBaseManager = RealmDB.getDataBaseManager();
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.btn_modify_rightsave:
                //
                name = et_update_familyName.getText().toString().trim();

                if (!TextUtils.isEmpty(name)) {
                    if (modifyType == 0) {
                        Log.i("TAG", "修改家庭名");
                        try {
                            dataBaseManager.UpdateFamily(familyID, name, "", "");
                        } catch (DataBaseError dataBaseError
                                ) {
                            if (dataBaseError.getErrorType() == IllegalName_DisapprovedCharacter) {
                                ToastUtil.showToast(modifyName.this, "名字不合法，请重新输入");
                            }
                            dataBaseError.printStackTrace();
                        } catch (DataBaseSignal dataBaseSignal) {
                            dataBaseSignal.printStackTrace();
                            if (dataBaseSignal.getSignalType() == FamilyUpdated) {
                                ToastUtil.showToast(modifyName.this, "修改成功");
                                Intent intent = new Intent(modifyName.this, familyInformation.class);
                                intent.putExtra("memberID", memberID);
                                intent.putExtra("familyID", familyID);
                                intent.putExtra("modifyType", 0);
                                startActivity(intent);
                                Log.i("TAG", " modify 的finish被调用");
                                finish();
                            }
                        }
                    } else if (modifyType == 1) {
                        Log.i("TAG", "修改个体名");
                        try {
                            dataBaseManager.UpdateMember(memberID, "", 0, name, "", "");
                        } catch (DataBaseError dataBaseError) {
                            ToastUtil.showToast(modifyName.this, "名字不合法，请重新输入");
                            onCreate(null);
                            dataBaseError.printStackTrace();
                        } catch (DataBaseSignal dataBaseSignal) {
                            if (dataBaseSignal.getSignalType() == MemberUpdated) {
                                ToastUtil.showToast(modifyName.this, "修改成功");
                                Intent intent = new Intent(modifyName.this, updateFamily.class);
                                intent.putExtra("memberID", memberID);
                                intent.putExtra("modifyType", 1);
                                startActivity(intent);
                                finish();
                                //                                finish();
                            }
                            dataBaseSignal.printStackTrace();
                        }
                    } else if (modifyType == 2) {
                        Log.i("TAG", "修改nickname");
                        try {
                            dataBaseManager.UpdateMember(memberID, "", 0, "", name, "");
                        } catch (DataBaseError dataBaseError) {
                            ToastUtil.showToast(modifyName.this, "名字不合法，请重新输入");
                            onCreate(null);
                            dataBaseError.printStackTrace();
                        } catch (DataBaseSignal dataBaseSignal) {
                            if (dataBaseSignal.getSignalType() == MemberUpdated) {
                                ToastUtil.showToast(modifyName.this, "修改成功");
                                Intent intent = new Intent(modifyName.this, updateFamily.class);
                                intent.putExtra("memberID", memberID);
                                intent.putExtra("modifyType", 1);
                                startActivity(intent);
                                finish();
                            }
                            dataBaseSignal.printStackTrace();
                        }
                    }

                } else {
                    ToastUtil.showToast(modifyName.this, "请输入一个名字");
                }
                break;
            case R.id.iv_modify_leftback:
                Log.i("TAG", "返回上一级");
                finish();
                break;
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
