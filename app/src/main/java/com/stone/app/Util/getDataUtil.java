package com.stone.app.Util;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.stone.app.dataBase.DataBaseError;
import com.stone.app.dataBase.DataBaseManager;
import com.stone.app.dataBase.MemberData;

import java.util.List;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Brandon Zhang on 2017/9/8.
 */

public class getDataUtil {
//    private static DataBaseManager dataBaseManager;

    //    private static String memberID;
    //
    //
    //
    //    public static String getMemberID(Context context) {
    //        return memberID;
    //    }
    //
    //    public void setMemberID(Context context) {
    //        SharedPreferences pref =context.getSharedPreferences("autologin", MODE_PRIVATE);
    //        this.memberID= pref.getString("memberID", "")
    //    }
    public static  String getmemberID(Context context) {
        SharedPreferences pref = context.getSharedPreferences("autologin", MODE_PRIVATE);
        return pref.getString("memberID", "");
    }
    public static  String getfamilyID(String memberID ,DataBaseManager dbma) {
        try {
        List<MemberData> list= dbma.getMemberList(memberID,"","","");
            if(list.size()>0){
                Log.i("TAG","Util获得的memberlist不为空");
                return list.get(0).getFamilyID();
            }

        } catch (DataBaseError dataBaseError) {
            dataBaseError.printStackTrace();
        }
//        try {
//            dbma.CloseDataBase();
//        } catch (DataBaseError dataBaseError) {
//            dataBaseError.printStackTrace();
//        }
        return "";
    }
//    public static DataBaseManager getDataBaseManager() {
//        return dataBaseManager;
//    }

    //    public static
//    public static String getfamilyID(Context context) {
//        SharedPreferences pref = context.getSharedPreferences("autologin", MODE_PRIVATE);
//        return pref.getString("familyID", "");
//    }
    public static  String setPicName() {
        return String.valueOf(DateUtil.getTime())+"pic_portrait";
    }
}
