package com.stone.app.addMember;

import android.app.Activity;
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

import java.util.ArrayList;
import java.util.List;

import static com.stone.app.R.id.lv_updatafamily_info;


public class updateFamily extends Activity {
    private DataBaseManager dataBaseManager;
    private String memberID = "";
    private String familyID = "";
    private ListView lv_update_info;
    private List<FamilyData> familyDataList = null;
    private List<MemberData> memberDataList=null;
    final static int ITEMNUM = 4;
    private int Type = 3;
    private List<updateFamilyItem> updateFamilyItemList = new ArrayList<updateFamilyItem>();
    private List<updateFamilyItem> updatememberItemList = new ArrayList<updateFamilyItem>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_family);
        dataBaseManager = RealmDB.getDataBaseManager();
        lv_update_info = findViewById(lv_updatafamily_info);
        ImageView imageView = findViewById(R.id.iv_update_leftback);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //                    finish();
            }
        });
        Intent intent = getIntent();
        Type = intent.getIntExtra("Type", 3);
        //        修改家庭信息
        if (Type == 0) {
            familyID = intent.getStringExtra("familyID");
            Log.i("TAG", "获得的familyID 为： " + familyID);
            try {
                familyDataList = dataBaseManager.getFamilyList(familyID, "", "");
                if (familyDataList != null) {
                    FamilyData familyData = familyDataList.get(0);
                    updateFamilyItem updateFamilyItem = new updateFamilyItem();
                    updateFamilyItem.setLeftText("头像");
                    updateFamilyItemList.add(updateFamilyItem);
                    updateFamilyItem.setRightImagepath(familyData.getPortraitID());

                    updateFamilyItem updateFamilyItem1 = new updateFamilyItem();
                    updateFamilyItem1.setLeftText("家庭名");
                    updateFamilyItem1.setRightText(familyData.getName());
                    updateFamilyItemList.add(updateFamilyItem1);

                    updateFamilyItem updateFamilyItem2 = new updateFamilyItem();
                    updateFamilyItem2.setLeftText("家庭ID");
                    updateFamilyItem2.setRightText(familyData.getID());
                    updateFamilyItemList.add(updateFamilyItem2);

                    updateFamilyItem updateFamilyItem3 = new updateFamilyItem();
                    updateFamilyItem3.setLeftText("家庭人数");
                    updateFamilyItem3.setRightText(String.valueOf(dataBaseManager.getMemberList("", familyID, "", "").size()));
                    updateFamilyItemList.add(updateFamilyItem3);

                    updateAdapter Adapter = new updateAdapter(updateFamily.this, R.layout.updatefamily_item, updateFamilyItemList);
                    lv_update_info.setAdapter(Adapter);
                    lv_update_info.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                            switch (i) {
                                case 0:
                                    Log.i("TAG", "修改家庭图像按钮被点击了");
                                    break;
                                case 1:
                                    Log.i("TAG", "修改家庭名字按钮被点击了");
                                    Intent intentmodify_name = new Intent(updateFamily.this, modifyFamilyName.class);
                                    intentmodify_name.putExtra("familyID", familyID);
                                    startActivity(intentmodify_name);
                                    break;
                            }
                        }
                    });
                }
            } catch (DataBaseError dataBaseError) {
                dataBaseError.printStackTrace();
            }

        }else  if(Type == 1){
            //修改个人信息
            memberID=intent.getStringExtra("memberID");
            Log.i("TAG","update的memberID：" +memberID );
            try {
                memberDataList= dataBaseManager.getMemberList(memberID,"","","");
                if (memberDataList != null) {
                    MemberData memberData = memberDataList.get(0);
                    updateFamilyItem updateFamilyItem = new updateFamilyItem();
                    updateFamilyItem.setLeftText("头像");
                    updateFamilyItemList.add(updateFamilyItem);
                    updateFamilyItem.setRightImagepath(memberData.getPortraitID());

                    updateFamilyItem updateFamilyItem1 = new updateFamilyItem();
                    updateFamilyItem1.setLeftText("姓名");
                    updateFamilyItem1.setRightText(memberData.getName());
                    updateFamilyItemList.add(updateFamilyItem1);

                    updateFamilyItem updateFamilyItem2 = new updateFamilyItem();
                    updateFamilyItem2.setLeftText("ID");
                    updateFamilyItem2.setRightText(memberData.getID());
                    updateFamilyItemList.add(updateFamilyItem2);

                    updateFamilyItem updateFamilyItem3 = new updateFamilyItem();
                    updateFamilyItem3.setLeftText("昵称");
                    updateFamilyItem3.setRightText(memberData.getNickName());
                    updateFamilyItemList.add(updateFamilyItem3);

                    updateAdapter Adapter = new updateAdapter(updateFamily.this, R.layout.updatefamily_item, updateFamilyItemList);
                    lv_update_info.setAdapter(Adapter);
                    lv_update_info.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                            switch (i) {
                                case 0:
                                    Log.i("TAG", "修改家庭图像按钮被点击了");
                                    break;
                                case 1:
                                    Log.i("TAG", "修改家庭名字按钮被点击了");
                                    Intent intentmodify_name = new Intent(updateFamily.this, modifyFamilyName.class);
                                    intentmodify_name.putExtra("familyID", familyID);
                                    startActivity(intentmodify_name);
                                    break;
                            }
                        }
                    });
                }
            } catch (DataBaseError dataBaseError) {
                dataBaseError.printStackTrace();
            }

        }


    }
}
