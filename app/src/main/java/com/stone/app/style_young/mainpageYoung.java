package com.stone.app.style_young;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;

import com.stone.app.Game.GameRecord.GameRecord;
import com.stone.app.Game.game_judge.game_judgeActivity;
import com.stone.app.Game.game_puzzle.gamestart;
import com.stone.app.R;
import com.stone.app.Util.ToastUtil;
import com.stone.app.Util.getDataUtil;
import com.stone.app.addMember.familyInformation;
import com.stone.app.addMember.searchMemberActivity;
import com.stone.app.dataBase.DataBaseError;
import com.stone.app.dataBase.DataBaseManager;
import com.stone.app.dataBase.FamilyData;
import com.stone.app.dataBase.RealmDB;
import com.stone.app.login.loginActivity;
import com.stone.app.mainPage.MyInformation;
import com.stone.app.photoBroswer.photoBroswerActivity;
import com.stone.app.photoUpload.photoUploadActivity;
import com.zzt.inbox.interfaces.OnDragStateChangeListener;
import com.zzt.inbox.widget.InboxBackgroundScrollView;
import com.zzt.inbox.widget.InboxLayoutBase;
import com.zzt.inbox.widget.InboxLayoutListView;

import java.util.ArrayList;


public class mainpageYoung extends AppCompatActivity {
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
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mainpage_young);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(0xdd000000));
        initData();
        Intent intent = getIntent();
        memberID = getDataUtil.getmemberID(mainpageYoung.this);
        Log.i("TAG", "mainpageYoung获得的memberID为 ：" + memberID);
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
        dataBaseManager = RealmDB.getDataBaseManager();
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
        imagAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, list_image);
        gameAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, list_game);
        //        memberAdapter=new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,list_member);
        familyAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, list_family);
        settingAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, list_setting);
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
                        switch (i) {
                            case 0:
                                Intent intentphotoUP = new Intent(mainpageYoung.this, photoUploadActivity.class);
                                intentphotoUP.putExtra("memberID", memberID);
                                startActivity(intentphotoUP);
                                //
                                Log.i("TAG", "第0项上传图片被点击了");
                                break;
                            case 1:
                                Log.i("TAG", "第1项浏览图片被点击了");
                                Intent intentphotoBrowser = new Intent(mainpageYoung.this, photoBroswerActivity.class);
                                intentphotoBrowser.putExtra("memberID", memberID);
                                startActivity(intentphotoBrowser);
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
                        switch (i) {
                            case 0:
                                Log.i("TAG", "第0项拼图游戏被点击了");
                                Intent intent_puzzle = new Intent(mainpageYoung.this, gamestart.class);
                                intent_puzzle.putExtra("memberID", memberID);
                                startActivity(intent_puzzle);
                                break;
                            case 1:
                                Log.i("TAG", "第1项判断游戏被点击了");
                                Intent intent_judge = new Intent(mainpageYoung.this, game_judgeActivity.class);
                                intent_judge.putExtra("memberID", memberID);
                                startActivity(intent_judge);
                                break;
                            case 2:
                                Log.i("TAG", "第2项游戏记录被点击了");
                                Intent intent_record = new Intent(mainpageYoung.this, GameRecord.class);
                                intent_record.putExtra("memberID", memberID);
                                startActivity(intent_record);
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
                Intent intentMemberInfo = new Intent(mainpageYoung.this, MyInformation.class);
                intentMemberInfo.putExtra("memberID", memberID);
                startActivity(intentMemberInfo);
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
                        switch (i) {
                            case 0:
                                Log.i("TAG", "第0项更改系统风格被点击了");
                                break;
                            case 1:
                                Log.i("TAG", "第1项账号与安全被点击了");
                                break;
                            case 2:
                                Log.i("TAG", "第2项退出登录被点击了");
//                                try {
//                                    dataBaseManager.MemberHibernate(memberID);
//                                } catch (DataBaseError dataBaseError) {
//                                    dataBaseError.printStackTrace();
//                                } catch (DataBaseSignal dataBaseSignal) {
//                                    dataBaseSignal.printStackTrace();
//                                    if (dataBaseSignal.getSignalType() == MemberHibernationSucceed) {
//                                        editor = getSharedPreferences("autologin", MODE_PRIVATE).edit();
//                                        editor.putString("memberID", "");
//                                        ToastUtil.showToast(mainpageYoung.this, "注销成功");
//                                        startActivity(new Intent(mainpageYoung.this, loginActivity.class));
//                                        finish();
//                                    }
//                                }
                                    editor = getSharedPreferences("autologin", MODE_PRIVATE).edit();
                                    editor.putString("memberID", "");
                                    ToastUtil.showToast(mainpageYoung.this, "注销成功");
                                    startActivity(new Intent(mainpageYoung.this, loginActivity.class));
                                    finish();
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
                        switch (i) {
                            case 0:
                                Log.i("TAG", "第0项家庭信息被点击了");
                                Intent intent_familyinfo = new Intent(mainpageYoung.this, familyInformation.class);
                                intent_familyinfo.putExtra("memberID", memberID);
                                startActivity(intent_familyinfo);
                                break;
                            case 1:
                                Log.i("TAG", "第1项创建家庭被点击了");
                                try {
                                    FamilyData familyData = dataBaseManager.AddFamily("", memberID,"");
                                    familyData.getID();
                                    Intent intent_updatefamily = new Intent(mainpageYoung.this, familyInformation.class);
                                    intent_updatefamily.putExtra("memberID", memberID);
                                    startActivity(intent_updatefamily);

                                } catch (DataBaseError dataBaseError) {
                                    if (dataBaseError.getErrorType() == DataBaseError.ErrorType.MemberHasFamilyAlready) {
                                        ToastUtil.showToast(mainpageYoung.this, "创建失败，你已经创建过一个家庭了");
                                    }
                                    dataBaseError.printStackTrace();
                                }
                                break;
                            case 2:
                                Log.i("TAG", "第2项加入家庭被点击了");
                                Intent intent_addfamily = new Intent(mainpageYoung.this, searchMemberActivity.class);
                                intent_addfamily.putExtra("memberID", memberID);
                                startActivity(intent_addfamily);


                                break;
                        }
                    }
                });
            }
        });
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }
}
