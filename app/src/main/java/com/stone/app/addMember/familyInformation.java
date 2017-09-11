package com.stone.app.addMember;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import com.stone.app.R;
import com.stone.app.dataBase.DataBaseError;
import com.stone.app.dataBase.DataBaseManager;
import com.stone.app.dataBase.FamilyData;
import com.stone.app.dataBase.MemberData;
import com.stone.app.dataBase.RealmDB;
import com.stone.app.style_young.mainpageYoung;

import java.util.ArrayList;
import java.util.List;


public class familyInformation extends Activity {

    private DataBaseManager dataBaseManager;
    private String memberID = "";
    private String familyID = "";
    private ListView lv_familyinfo, lv_familymember_info;
    private List<familyItem> flist = new ArrayList<familyItem>();
    private List<familyMemberItem> fmemberlist = new ArrayList<familyMemberItem>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_family_information2);
        dataBaseManager = RealmDB.getDataBaseManager();
        lv_familyinfo = findViewById(R.id.lv_familyinfo);
        lv_familymember_info = findViewById(R.id.lv_familymember_info);
        ImageView imageView=findViewById(R.id.iv_familyinfo_leftback);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        final Intent intent = getIntent();
        memberID = intent.getStringExtra("memberID");
        try {
            List<MemberData> list = dataBaseManager.getMemberList(memberID, "", "", "");
            if (list != null) {
                familyID = list.get(0).getFamilyID();
            } else {
                Log.i("TAG", " memberID 不存在");
            }
        } catch (DataBaseError dataBaseError) {
            Log.i("TAG", "familyInformation  error info: " + dataBaseError.getMessage());
            dataBaseError.printStackTrace();
        }
        if (!familyID.equals("")) {
            List<FamilyData> familyDataList = null;
            List<MemberData> familymemberList = null;
            try {
                familyDataList = dataBaseManager.getFamilyList(familyID, "", "");
                if (familyDataList != null) {
                    FamilyData familyData = familyDataList.get(0);
                    familyItem familyItem = new familyItem();
                    familyItem.setFamilyID(familyData.getID());
                    familyItem.setFamilyName(familyData.getName());
                    familyItem.setFamilyCreaterID(familyData.getRootMemberID());
                    familyItem.setFamilyCreaterName(dataBaseManager.getMemberList(familyData.getRootMemberID(), "", "", "").get(0).getName());
                    flist.add(familyItem);
                    familyAdapter familyAdapter = new familyAdapter(familyInformation.this, R.layout.family_item, flist);
                    lv_familyinfo.setAdapter(familyAdapter);
                    lv_familyinfo.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                            switch(i){
                                case 0:
                                    Intent intent_update=new Intent(familyInformation.this,updateFamily.class);
                                    intent_update.putExtra("familyID",familyID);
                                    intent_update.putExtra("Type",0);
                                    startActivity(intent_update);

                                break;
                            }
                        }
                    });
                } else {
                    Log.i("TAG", "familyDataList为空");
                }

            } catch (DataBaseError dataBaseError) {
                Log.i("TAG", "familyinformation获取familylist错误 ,信息为： " + dataBaseError.getMessage());
                dataBaseError.printStackTrace();
            }
            try {
                familymemberList = dataBaseManager.getMemberList("", familyID, "", "");
            } catch (DataBaseError dataBaseError) {
                dataBaseError.printStackTrace();
                Log.i("TAG", "familyInformation  error info: " + dataBaseError.getMessage());

            }
            if (familymemberList != null) {
                for (MemberData memberData : familymemberList) {
                    familyMemberItem familyMemberItem = new familyMemberItem();
                    familyMemberItem.setMemberID(memberData.getID());
                    familyMemberItem.setMemberName(memberData.getName());
                    familyMemberItem.setImagePath(memberData.getPortraitID());
                    fmemberlist.add(familyMemberItem);
                }
                familyMemberAdapter familyMemberAdapter = new familyMemberAdapter(familyInformation.this, R.layout.family_member_item, fmemberlist);
                lv_familymember_info.setAdapter(familyMemberAdapter);
            } else {
                Log.i("TAG", " familymemberList为空");
            }

        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(familyInformation.this);
            builder.setTitle("家庭信息");
            builder.setCancelable(false);
            builder.setMessage("啊偶，家庭信息不存在，快去加入或者创建一个家庭吧");
            builder.setPositiveButton("好的", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    startActivity(new Intent(familyInformation.this, mainpageYoung.class));
                }
            });
            builder.show();

        }

    }
}
