package com.stone.app.addMember;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Spinner;

import com.stone.app.R;

import java.util.List;

public class addFamilyMember extends Activity {
    private  Myadapter myadapter;
    private List<familyMember> mlist;
    private List<String>relationlist;
    private Spinner sp_name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_family_member);
        Intent intent=getIntent();
        String familyID=intent.getStringExtra("familyID");
        String memberID=intent.getStringExtra("memberID");
        //获得姓名和ID

        sp_name=findViewById(R.id.sp_name);
        myadapter=new Myadapter(addFamilyMember.this,mlist);
        sp_name.setAdapter(myadapter);

    }
}
