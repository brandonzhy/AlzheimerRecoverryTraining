package com.stone.app.style_young;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;

import com.stone.app.R;
import com.stone.app.dataBase.DataBaseManager;
import com.stone.app.dataBase.RealmDB;
import com.stone.app.photoUpload.photoUploadActivity;
import com.zzt.inbox.interfaces.OnDragStateChangeListener;
import com.zzt.inbox.widget.InboxBackgroundScrollView;
import com.zzt.inbox.widget.InboxLayoutBase;
import com.zzt.inbox.widget.InboxLayoutListView;

import java.util.ArrayList;


public class mainpageYoung extends ActionBarActivity{
    private ArrayAdapter<String> imagAdapter;
    private ArrayAdapter<String> gameAdapter;
   private ArrayAdapter<String> memberAdapter;
    private ArrayAdapter<String> familyAdapter;
    private ArrayAdapter<String> settingAdapter;
    private InboxLayoutListView inboxlayout_image;
    private InboxLayoutListView inboxlayout_game;
    private InboxLayoutListView inboxlayout_member;
    private InboxLayoutListView inboxlayout_family;
    private InboxLayoutListView inboxlayout_seeting;
    private ArrayList<String> list_image, list_game, list_member, list_family, list_setting;
    private String memberID;
    private DataBaseManager dataBaseManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mainpage_young);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(0xdd000000));
        initData();
        Intent intent=getIntent();
        memberID=intent.getStringExtra("memberID");
        final InboxBackgroundScrollView inboxBackgroundScrollView = (InboxBackgroundScrollView) findViewById(R.id.scroll);
        inboxlayout_image = (InboxLayoutListView) findViewById(R.id.inboxlayout_image);
        inboxlayout_game = (InboxLayoutListView) findViewById(R.id.inboxlayout_game);
        inboxlayout_member = (InboxLayoutListView) findViewById(R.id.inboxlayout_member);
        inboxlayout_family = (InboxLayoutListView) findViewById(R.id.inboxlayout_family);
        inboxlayout_seeting = (InboxLayoutListView) findViewById(R.id.inboxlayout_seeting);
        inboxlayout_image.setBackgroundScrollView(inboxBackgroundScrollView);//绑定scrollview
        inboxlayout_game.setBackgroundScrollView(inboxBackgroundScrollView);//绑定scrollview
        inboxlayout_member.setBackgroundScrollView(inboxBackgroundScrollView);//绑定scrollview
        inboxlayout_family.setBackgroundScrollView(inboxBackgroundScrollView);//绑定scrollview
        inboxlayout_seeting.setBackgroundScrollView(inboxBackgroundScrollView);//绑定scrollview
        inboxlayout_image.setAdapter(imagAdapter);
        inboxlayout_game.setAdapter(gameAdapter);
        //        inboxlayout_member.setAdapter(memberAdapter);
        inboxlayout_family.setAdapter(familyAdapter);
        inboxlayout_seeting.setAdapter(settingAdapter);
        inboxlayout_image.setOnDragStateChangeListener(new OnDragStateChangeListener() {
            @Override
            public void dragStateChange(InboxLayoutBase.DragState state) {
                switch (state) {
                    case CANCLOSE:
                        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(0xff5e5e5e));
                        getSupportActionBar().setTitle("back");
                        break;
                    case CANNOTCLOSE:
                        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(0xdd000000));
                        getSupportActionBar().setTitle("InboxLayout");
                        break;
                }
            }
        });
        inboxlayout_game.setOnDragStateChangeListener(new OnDragStateChangeListener() {
            @Override
            public void dragStateChange(InboxLayoutBase.DragState state) {
                switch (state) {
                    case CANCLOSE:
                        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(0xff5e5e5e));
                        getSupportActionBar().setTitle("back");
                        break;
                    case CANNOTCLOSE:
                        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(0xdd000000));
                        getSupportActionBar().setTitle("InboxLayout");
                        break;
                }
            }
        });
        inboxlayout_member.setOnDragStateChangeListener(new OnDragStateChangeListener() {
            @Override
            public void dragStateChange(InboxLayoutBase.DragState state) {
                switch (state) {
                    case CANCLOSE:
                        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(0xff5e5e5e));
                        getSupportActionBar().setTitle("back");
                        break;
                    case CANNOTCLOSE:
                        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(0xdd000000));
                        getSupportActionBar().setTitle("InboxLayout");
                        break;
                }
            }
        });
        inboxlayout_family.setOnDragStateChangeListener(new OnDragStateChangeListener() {
            @Override
            public void dragStateChange(InboxLayoutBase.DragState state) {
                switch (state) {
                    case CANCLOSE:
                        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(0xff5e5e5e));
                        getSupportActionBar().setTitle("back");
                        break;
                    case CANNOTCLOSE:
                        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(0xdd000000));
                        getSupportActionBar().setTitle("InboxLayout");
                        break;
                }
            }
        });
        inboxlayout_seeting.setOnDragStateChangeListener(new OnDragStateChangeListener() {
            @Override
            public void dragStateChange(InboxLayoutBase.DragState state) {
                switch (state) {
                    case CANCLOSE:
                        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(0xff5e5e5e));
                        getSupportActionBar().setTitle("back");
                        break;
                    case CANNOTCLOSE:
                        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(0xdd000000));
                        getSupportActionBar().setTitle("InboxLayout");
                        break;
                }
            }
        });
        init();
    }

    private void initData() {
        dataBaseManager= RealmDB.getDataBaseManager();
        list_image = new ArrayList<String>();
        list_game = new ArrayList<String>();
        list_family = new ArrayList<String>();
        list_setting = new ArrayList<String>();
        list_image.add("上传图片");
        list_image.add("浏览图片");
        list_game.add("拼图游戏");
        list_game.add("看图判断游戏");
        list_game.add("游戏记录");
        list_family.add("家庭信息");
        list_family.add("创建家庭");
        list_family.add("加入已有家庭");
        list_setting.add("更改系统风格");
        list_setting.add("账号与安全");
        list_setting.add("退出登录");
        imagAdapter = new ArrayAdapter<String>(this,  android.R.layout.simple_list_item_1, list_image);
        gameAdapter = new ArrayAdapter<String>(this,  android.R.layout.simple_list_item_1, list_game);
        //        memberAdapter=new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,list_member);
        familyAdapter = new ArrayAdapter<String>(this,  android.R.layout.simple_list_item_1, list_family);
        settingAdapter = new ArrayAdapter<String>(this,  android.R.layout.simple_list_item_1, list_setting);
    }

    private void init() {
        final LinearLayout ly_image = (LinearLayout) findViewById(R.id.ly_image);
        ly_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inboxlayout_image.setVisibility(View.VISIBLE);
                inboxlayout_game.setVisibility(View.GONE);
                inboxlayout_member.setVisibility(View.GONE);
                inboxlayout_family.setVisibility(View.GONE);
                inboxlayout_seeting.setVisibility(View.GONE);
                inboxlayout_image.setCloseDistance(50);
                inboxlayout_image.openWithAnim(ly_image);
                inboxlayout_image.getDragableView().setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        switch(i){
                            case 0:
                                Intent intentphotoUP = new Intent(mainpageYoung.this, photoUploadActivity.class);
                                intentphotoUP.putExtra("memberID", memberID);
                                startActivity(intentphotoUP);
                                //
                                Log.i("TAG","第0项被点击了"  );
                                break;
                            case 1:
                                Log.i("TAG","第1项被点击了"  );
                                break;
                        }
                    }
                });
            }
        });

        final LinearLayout ly_game = (LinearLayout) findViewById(R.id.ly_game);
        ly_game.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inboxlayout_game.setVisibility(View.VISIBLE);
                inboxlayout_image.setVisibility(View.GONE);
                inboxlayout_member.setVisibility(View.GONE);
                inboxlayout_family.setVisibility(View.GONE);
                inboxlayout_seeting.setVisibility(View.GONE);

                inboxlayout_game.setCloseDistance(50);
                inboxlayout_game.openWithAnim(ly_game);
                inboxlayout_game.getDragableView().setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        switch(i){
                            case 0:
                                Log.i("TAG","第0项被点击了"  );
                                break;
                            case 1:
                                Log.i("TAG","第1项被点击了"  );
                                break;
                        }
                    }
                });
            }
        });

        //       ding_dan

        final LinearLayout member = (LinearLayout) findViewById(R.id.member);
        member.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inboxlayout_member.setVisibility(View.VISIBLE);
                inboxlayout_game.setVisibility(View.GONE);
                inboxlayout_image.setVisibility(View.GONE);
                inboxlayout_family.setVisibility(View.GONE);
                inboxlayout_seeting.setVisibility(View.GONE);
                inboxlayout_member.setCloseDistance(50);

                //                inboxlayout_game.openWithAnim(ly_game);
                //                inboxLayoutListView.openWithAnim(member);
                //                Intent intent =new Intent(ListViewActivity.this,baseinformation.class);
                //                intent.putExtra("memberID",memberID);
                //                startActivity(intent);
            }
        });

        final LinearLayout ly_seeting = (LinearLayout) findViewById(R.id.ly_seeting);
        ly_seeting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //                inboxLayoutListView.openWithAnim(ly_seeting);
                inboxlayout_seeting.setVisibility(View.VISIBLE);
                inboxlayout_game.setVisibility(View.GONE);
                inboxlayout_member.setVisibility(View.GONE);
                inboxlayout_family.setVisibility(View.GONE);
                inboxlayout_image.setVisibility(View.GONE);
                inboxlayout_seeting.setCloseDistance(50);
                inboxlayout_seeting.openWithAnim(ly_seeting);
                inboxlayout_seeting.getDragableView().setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        switch(i){
                            case 0:
                                Log.i("TAG","第0项被点击了"  );
                                break;
                            case 1:
                                Log.i("TAG","第1项被点击了"  );
                                break;
                        }
                    }
                });

            }

        });

        final LinearLayout lv_family = (LinearLayout) findViewById(R.id.lv_family);
        lv_family.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inboxlayout_family.setVisibility(View.VISIBLE);
                inboxlayout_family.setCloseDistance(50);
                inboxlayout_family.openWithAnim(lv_family);//bottom item set true
                inboxlayout_family.getDragableView().setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        switch(i){
                            case 0:

                                Log.i("TAG","第0项被点击了"  );
                                break;
                            case 1:
                                Log.i("TAG","第1项被点击了"  );
                                break;
                        }
                    }
                });
            }
        });
    }

}
