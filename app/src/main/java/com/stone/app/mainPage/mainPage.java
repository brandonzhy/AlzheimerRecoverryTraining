//package com.stone.app.mainPage;
//
//import android.app.Activity;
//import android.content.Intent;
//import android.content.SharedPreferences;
//import android.os.Bundle;
//import android.util.Log;
//import android.view.View;
//import android.widget.Button;
//
//import com.stone.app.Game.game_puzzle.game_centre;
//import com.stone.app.R;
//import com.stone.app.Util.getDataUtil;
//import com.stone.app.dataBase.DataBaseError;
//import com.stone.app.dataBase.DataBaseManager;
//import com.stone.app.dataBase.MemberData;
//import com.stone.app.dataBase.RealmDB;
//import com.stone.app.photoUpload.photoUploadActivity;
//
//import java.util.List;
//
//
//public class mainPage extends Activity {
//    private DataBaseManager dataBaseManager;
//    private Intent intent;
//    String memberID;
//    String memberNickName;
//    String memberName;
//    int memberGender;
//    private String memberFamilyID;
//    private SharedPreferences pref;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_my_page);
//        pref = getSharedPreferences("autologin", MODE_PRIVATE);
////        Realm.init(this);
////        RealmConfiguration config = new RealmConfiguration.Builder().build();
////        Realm.setDefaultConfiguration(config);
////        dataBaseManager = new DataBaseManager();
//        dataBaseManager= RealmDB.getDataBaseManager();
//        Button btn_info = (Button) findViewById(R.id.btn_info);
//        Button btn_game = (Button) findViewById(R.id.btn_game);
//        Button btn_uploadpicture = (Button) findViewById(R.id.btn_uploadpicture);
////        if (pref.getBoolean("isFirstLogin", true)) {
//////第一次登陆主界面，从setInformation界面跳转过来
////            //Util  获得信息
////            Log.i("TAG","是第一次登陆" );
////            intent = getIntent();
////            memberID = intent.getStringExtra("memberID");
////            memberNickName = intent.getStringExtra("memberNickName");
////            memberName = intent.getStringExtra("memberName");
////            memberGender = intent.getIntExtra("memberGender", 0);
////            memberFamilyID="";
////            //PreferenceManager.getDefaultSharedPreferences利用包名来命名SharedPreferences文件
////            SharedPreferences.Editor editor = getSharedPreferences("autologin", MODE_PRIVATE).edit();
////            editor.putString("memberID", memberID);
////            editor.apply();
////        } else {
////            Log.i("TAG","不第一次登陆" );
////            List<MemberData> list = null;
////            try {
////                list = dataBaseManager.getMemberList(pref.getString("memberID", ""), "", "", "");
////            } catch (DataBaseError dataBaseError) {
////                Log.i("TAG","注册失败，错误类型为: " +dataBaseError.getErrorType() );
////                dataBaseError.printStackTrace();
////            }
////            if (list != null) {
////                for (MemberData memberData : list) {
////                    memberID = memberData.getID();
////                    memberNickName = memberData.getNickName();
////                    memberFamilyID = memberData.getFamilyID();
////                    memberName = memberData.getName();
////                    memberGender = memberData.getGender();
////                    Log.i("TAG","mainpage的memberID" +memberID );
////                    break;
////                }
////            }
////
////
////        }
//        List<MemberData> list = null;
//        try {
//            memberID=getDataUtil.getmemberID(mainPage.this);
//            Log.i("TAG","mainpage memberID:" +memberID );
//            list = dataBaseManager.getMemberList(memberID, "", "", "");
//        } catch (DataBaseError dataBaseError) {
//            Log.i("TAG","注册失败，错误类型为: " +dataBaseError.getErrorType() );
//            dataBaseError.printStackTrace();
//        }
//        if (list.size()>0) {
//            for (MemberData memberData : list) {
//                memberID = memberData.getID();
//                memberNickName = memberData.getNickName();
//                memberFamilyID = memberData.getFamilyID();
//                memberName = memberData.getName();
//                memberGender = memberData.getGender();
//                Log.i("TAG","mainpage的memberID为： " +memberID );
//                break;
//            }
//        }
//        btn_uploadpicture.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intentphotoUP = new Intent(mainPage.this, photoUploadActivity.class);
//                intentphotoUP.putExtra("memberID", memberID);
//                startActivity(intentphotoUP);
//            }
//        });
//        btn_info.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                //                intent.getStringExtra("memberName");
//                //                intent.getStringExtra("memberNickName");
//                //                intent.getIntExtra("memberGender",0);
//                Intent intentinfo = new Intent(mainPage.this, MyInformation.class);
//                intentinfo.putExtra("memberID", memberID);
//                intentinfo.putExtra("memberName", memberName);
//                intentinfo.putExtra("memberNickName", memberNickName);
//                intentinfo.putExtra("memberGender", memberGender);
//                intentinfo.putExtra("memberFamilyID", memberFamilyID);
//
//                startActivityForResult(intentinfo, 1);
//            }
//        });
//        btn_game.setOnClickListener(new View.OnClickListener() {
//            @Override
//
//            public void onClick(View V) {
//                Intent intentgame = new Intent(mainPage.this, game_centre.class);
//                intentgame.putExtra("memberID", memberID);
//                startActivityForResult(intentgame, 1);
//            }
//        });
//    }
//}
