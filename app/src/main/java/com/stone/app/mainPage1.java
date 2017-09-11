package com.stone.app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.stone.app.addMember.searchMemberActivity;

public class mainPage1 extends Activity {

    private String memberID;
    ImageView iv_test;
//    String imagepath=
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page1);
        iv_test=findViewById(R.id.iv_test);
//        Glide.with(photoBroswerActivity.this).load(itemData.imagePath.trim()).into(iv_test);
        Glide.with(this).load("file:///android_asset/person1.png").into(iv_test);

//        Intent intent=getIntent();
//        memberID=intent.getStringExtra("memberID");
//        Log.i("TAG","mainPage 通过intent获得得到ID为： " +memberID );
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
                Intent intentCreate=new Intent(mainPage1.this,searchMemberActivity.class);
                intentCreate.putExtra("memberID",memberID);
                startActivity(intentCreate);
            break;
            case R.id.createfamily:

                break;
        }
        return true;
    }
}
