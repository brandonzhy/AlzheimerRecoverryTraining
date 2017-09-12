package com.stone.app.addMember;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import com.stone.app.R;
import com.stone.app.Util.ToastUtil;
import com.stone.app.Util.getDataUtil;
import com.stone.app.dataBase.DataBaseError;
import com.stone.app.dataBase.DataBaseManager;
import com.stone.app.dataBase.DataBaseSignal;
import com.stone.app.dataBase.FamilyData;
import com.stone.app.dataBase.RealmDB;

import java.util.ArrayList;
import java.util.List;

import static com.stone.app.dataBase.DataBaseError.ErrorType.RealmResultAutoUpdateFail;
import static com.stone.app.dataBase.DataBaseSignal.SignalType.AddSingleMemberToFamilySucceed;
import static com.stone.app.dataBase.DataBaseSignal.SignalType.MergeTwoFamiliesSucceed;

public class searchMemberActivity extends Activity implements SearchView.OnQueryTextListener {
    private ListView listView;
    private ArrayAdapter<String> adapter;
    private String ID[] = {};
    List<FamilyData> familylist = null;
    private SearchView searchView;
    private ArrayList<String> list = new ArrayList<String>();
    private String memberID;
    private DataBaseManager dataBaseManager;
    private String familyID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_member);
        Intent intent = getIntent();
        memberID = intent.getStringExtra("memberID");

        initData();
        initView();

        searchView.setOnQueryTextListener(this);
    }

    private void initView() {
        listView = findViewById(R.id.lv_serach);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //                Intent intentadd = new Intent(searchMemberActivity.this, addFamilyMember.class);
                //                intentadd.putExtra("familyID", ((TextView) view).getText());
                //                intentadd.putExtra("memberID",memberID);
                //                startActivity(intentadd);
                familyID = getDataUtil.getfamilyID(memberID,dataBaseManager);
                Log.i("TAG", "Util 获得的familID: " + familyID);
                Log.i("TAG", "search的item的项值为" + ((TextView) view).getText().toString());
                try {
                    Log.i("TAG","search 的memberID"+memberID  );
                    dataBaseManager.AddExistMemberToExistFamily(((TextView) view).getText().toString(),memberID);
                } catch (DataBaseError dataBaseError) {
                    if(RealmResultAutoUpdateFail==dataBaseError.getErrorType()){
                        ToastUtil.showToast(searchMemberActivity.this,"数据库更新数据失败，请更新到最新版本");
                    }
                    dataBaseError.printStackTrace();
                } catch (DataBaseSignal dataBaseSignal) {
                    if(dataBaseSignal.getSignalType()==AddSingleMemberToFamilySucceed){
                        ToastUtil.showToast(searchMemberActivity.this,"加入成功,跳转至家庭信息页面");

                    }else if(dataBaseSignal.getSignalType()==MergeTwoFamiliesSucceed){
                        ToastUtil.showToast(searchMemberActivity.this,"家庭合并成功,跳转至家庭信息页面");

                    }
                    Intent intent_sea_familyinfo=new Intent(searchMemberActivity.this,familyInformation.class);
                    intent_sea_familyinfo.putExtra("memberID",memberID);
                    startActivity(intent_sea_familyinfo);
                    finish();
                    dataBaseSignal.printStackTrace();
                }
                //                if (!familyID.equals("")) {
//                    if (!familyID.equals(((TextView) view).getText())) {
//
//                    }
//                    Intent intent_sea_info = new Intent(searchMemberActivity.this, familyInformation.class);
//                }else {
//
//                }
            }
        });
        searchView = findViewById(R.id.searchview);
        ImageView imageView = findViewById(R.id.iv_search_leftback);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void initData() {
        //        DataBaseManager dataBaseManager = new DataBaseManager();
        dataBaseManager = RealmDB.getDataBaseManager();
        try {
            familylist = dataBaseManager.getFamilyList("", "", "");
        } catch (DataBaseError dataBaseError) {
            Log.i("TAG", "错误类型为" + dataBaseError.getMessage());
            dataBaseError.printStackTrace();
        }
    }

    @Override
    public boolean onQueryTextSubmit(String s) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String s) {
        ArrayList<String> showlist = new ArrayList<String>();
        for (int i = 0; i < familylist.size(); i++) {
            if (familylist.get(i).getID().contains(s)) {
                showlist.add(familylist.get(i).getID());
                adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, showlist);
                listView.setAdapter(adapter);
            }
        }
        return false;
    }
}
