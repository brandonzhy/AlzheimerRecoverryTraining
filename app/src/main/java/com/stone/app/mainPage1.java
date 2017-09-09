package com.stone.app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.stone.app.addMember.searchMemberActivity;

public class mainPage1 extends Activity {

    private String memberID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page1);
        Intent intent=getIntent();
        memberID=intent.getStringExtra("memberID");
        Log.i("TAG","mainPage 通过intent获得得到ID为： " +memberID );
    }
//   public boolean onCreateOptionsMenu(Menu menu){
//       getMenuInflater().inflate(R.menu.toolbar_main,menu);
//       return true;
//
//   }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.toolbar_main,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.addfamily:
                Intent intentCreate=new Intent(mainPage1.this,searchMemberActivity.class)
                intentCreate.putExtra("memberID",memberID);
                startActivity(intentCreate);
            break;
            case R.id.createfamily:

                break;
        }
        return true;
    }
}
