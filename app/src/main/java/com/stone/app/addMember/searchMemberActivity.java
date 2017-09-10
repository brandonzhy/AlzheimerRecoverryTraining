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
import com.stone.app.dataBase.DataBaseError;
import com.stone.app.dataBase.DataBaseManager;
import com.stone.app.dataBase.FamilyData;
import com.stone.app.dataBase.RealmDB;

import java.util.ArrayList;
import java.util.List;

public class searchMemberActivity extends Activity implements SearchView.OnQueryTextListener {
    private ListView listView;
    private ArrayAdapter<String> adapter;
    private String ID[] = {};
    List<FamilyData>familylist=null;
    private SearchView searchView;
    private ArrayList<String> list = new ArrayList<String>();
    private String memberID;
    private DataBaseManager dataBaseManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_member);
        Intent intent=getIntent();
        memberID=intent.getStringExtra("memberID");

        initData();
        initView();

        searchView.setOnQueryTextListener(this);
    }

    private void initView() {
        listView = findViewById(R.id.lv_serach);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intentadd = new Intent(searchMemberActivity.this, addFamilyMember.class);
                intentadd.putExtra("familyID", ((TextView) view).getText());
                intentadd.putExtra("memberID",memberID);
                startActivity(intentadd);
                Log.i("TAG", "item的项值为" + ((TextView) view).getText());
            }
        });
        searchView = findViewById(R.id.searchview);
        ImageView imageView=findViewById(R.id.iv_search_leftback);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void initData() {
//        DataBaseManager dataBaseManager = new DataBaseManager();
        dataBaseManager= RealmDB.getDataBaseManager();
        try {
           familylist= dataBaseManager.getFamilyList("", "", "");
        } catch (DataBaseError dataBaseError) {
            Log.i("TAG","错误类型为" +dataBaseError.getMessage() );
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
                showlist.add(list.get(i));
                adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, showlist);
                listView.setAdapter(adapter);
            }
        }
        return false;
    }
}
