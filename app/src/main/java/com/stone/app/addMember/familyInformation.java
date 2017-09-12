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
import com.stone.app.Util.ToastUtil;
import com.stone.app.Util.getDataUtil;
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
    private String familyImagePath = "";
    private String MemberImagePath = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_family_information2);
        dataBaseManager = RealmDB.getDataBaseManager();
        lv_familyinfo = findViewById(R.id.lv_familyinfo);
        lv_familymember_info = findViewById(R.id.lv_familymember_info);
        ImageView imageView = findViewById(R.id.iv_familyinfo_leftback);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        memberID = getDataUtil.getmemberID(familyInformation.this);
        Log.i("TAG", "family获得的 memberID= " + memberID);
        try {
            List<MemberData> list = dataBaseManager.getMemberList(memberID, "", "", "");
            if (list != null && list.size() > 0) {
                familyID = list.get(0).getFamilyID();
            } else {
                Log.i("TAG", " memberID 不存在");
            }
        } catch (DataBaseError dataBaseError) {
            Log.i("TAG", "familyInformation  error info: " + dataBaseError.getMessage());
            dataBaseError.printStackTrace();

            finish();
        }

        if (!familyID.equals("")) {

            List<FamilyData> familyDataList = null;
            List<MemberData> familymemberList = null;
            try {
                Log.i("TAG", " information 获得的familyID =" + familyID);
                familyDataList = dataBaseManager.getFamilyList(familyID, "", "");


            } catch (DataBaseError dataBaseError) {
                Log.i("TAG", "familyinformation获取familylist错误 ,信息为： " + dataBaseError.getMessage());
                dataBaseError.printStackTrace();
            }
            if (familyDataList != null) {

                FamilyData familyData = familyDataList.get(0);
                familyItem familyItem = new familyItem();
//                List<PictureData> pictureDataList = null;

//                if (familyData.getPortraitID() == null) {

//                    try {
//                        pictureDataList = dataBaseManager.getPictureList(familyData.getPortraitID(), "", "", "", "", 0, 0);
//                        familyImagePath = pictureDataList.get(0).getImagePath();
//                    } catch (DataBaseError dataBaseError) {
//                        dataBaseError.printStackTrace();
//                    }
//                }

                //                if (pictureDataList != null&&pictureDataList.size()>0) {
                //                    familyImagePath = pictureDataList.get(0).getImagePath();
                //                }
                try {
                    familyImagePath=dataBaseManager.getFamilyPortraitPath(familyID);
                } catch (DataBaseError dataBaseError) {
                    dataBaseError.printStackTrace();
//                    if(PortraitNotExist==dataBaseError.getErrorType()){
                      Log.i("TAG","getFamilyPortraitPath de dataBaseError= " + dataBaseError.getErrorType()+dataBaseError.getMessage());
//                    }
                }
                familyItem.setImagePath(familyImagePath);
                familyItem.setFamilyID("ID: " + familyData.getID());
                familyItem.setFamilyName("家庭名: " + familyData.getName());
                familyItem.setFamilyCreaterID("创建人ID: " + familyData.getRootMemberID());
                //                    familyItem.setFamilyCreaterName("创建人: "+dataBaseManager.getMemberList(familyData.getRootMemberID(), "", "", "").get(0).getName());
                flist.add(familyItem);
                familyAdapter familyAdapter = new familyAdapter(familyInformation.this, R.layout.family_item, flist);
                lv_familyinfo.setAdapter(familyAdapter);
                lv_familyinfo.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        switch (i) {
                            case 0:
                                Intent intent_update = new Intent(familyInformation.this, updateFamily.class);
                                intent_update.putExtra("familyID", familyID);
                                intent_update.putExtra("modifyType", 0);
                                startActivity(intent_update);
                                finish();
                                break;
                        }
                    }
                });
            } else {
                Log.i("TAG", "familyDataList为空");
                ToastUtil.showToast(familyInformation.this,"familyDataList为空");
            }

            //member列表
            try {
                familymemberList = dataBaseManager.getMemberList("", familyID, "", "");
            } catch (DataBaseError dataBaseError) {
                dataBaseError.printStackTrace();
                Log.i("TAG", "familyInformation  error info: " + dataBaseError.getMessage());

            }
            if (familymemberList != null) {
                for (MemberData memberData : familymemberList) {
                    familyMemberItem familyMemberItem = new familyMemberItem();
                    familyMemberItem.setMemberID("ID: " + memberData.getID());
                    familyMemberItem.setMemberName("姓名: " + memberData.getName());
//                    if (memberData.getPortraitID() != null) {
//                        try {
//                            List<PictureData> pictureDataList = dataBaseManager.getPictureList(memberData.getPortraitID(), "", memberID, "", "", 0, 0);
//                            if (pictureDataList.size() > 0) {
//                                MemberImagePath = pictureDataList.get(0).getImagePath();
//                            }
//                        } catch (DataBaseError dataBaseError) {
//                            Log.i("TAG", " dataBaseManager.getPictureList的 dataBaseError=" + dataBaseError.getErrorType() + dataBaseError.getMessage());
//                            dataBaseError.printStackTrace();
//                        }
//
//                    }
                    try {
                        MemberImagePath=dataBaseManager.getMemberPortraitPath(memberID);
                    } catch (DataBaseError dataBaseError) {
                        dataBaseError.printStackTrace();
                    }
                    familyMemberItem.setImagePath(MemberImagePath);
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
                    finish();
                }
            });
            builder.show();

        }

    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(familyInformation.this, mainpageYoung.class));
        onDestroy();
        finish();
        super.onBackPressed();
    }
}
