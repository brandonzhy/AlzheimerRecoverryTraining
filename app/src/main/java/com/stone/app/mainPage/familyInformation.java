//package com.stone.app.mainPage;
//
//import android.app.Activity;
//import android.os.Bundle;
//
//import com.stone.app.R;
//import com.stone.app.Util.getDataUtil;
//import com.stone.app.dataBase.DataBaseError;
//import com.stone.app.dataBase.DataBaseManager;
//import com.stone.app.dataBase.FamilyData;
//
//import java.util.List;
//
//
//public class familyInformation extends Activity {
//private  String  memberID;
//    private String familyID;
//    private String familyName;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_family_information);
//        memberID= getDataUtil.getmemberID(this);
//        //
//        DataBaseManager dataBaseManager =new DataBaseManager();
//        try {
//           List<FamilyData>list= dataBaseManager.getFamilyList(memberID,"","");
//            //家庭已经存在则显示信息
//            if(list!=null){
//               familyID= list.get(0).getID();
//                familyName=list.get(0).getName();
//
//            }else{
//                //新建一个家庭
//            }
//        } catch (DataBaseError dataBaseError) {
//            dataBaseError.printStackTrace();
//        }
//    }
//}
