package com.stone.app.Util;

import android.content.Context;
import android.content.SharedPreferences;

import com.stone.app.dataBase.DataBaseManager;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Brandon Zhang on 2017/9/8.
 */

public class getDataUtil {
    private static DataBaseManager dataBaseManager;

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

    public static DataBaseManager getDataBaseManager() {
        return dataBaseManager;
    }

    //    public static
    public static String getfamilyID(Context context) {
        SharedPreferences pref = context.getSharedPreferences("autologin", MODE_PRIVATE);
        return pref.getString("familyID", "");
    }
}
