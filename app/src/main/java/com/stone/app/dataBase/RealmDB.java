package com.stone.app.dataBase;

import android.app.Application;

import io.realm.Realm;
import io.realm.RealmConfiguration;

public class RealmDB extends Application {
    private static DataBaseManager dbm;

    public void onCreate() {
        super.onCreate();
        Realm.init(this);
        RealmConfiguration config = new RealmConfiguration.Builder().build();
        Realm.setDefaultConfiguration(config);
//        if (LeakCanary.isInAnalyzerProcess(this)) {
//            // This process is dedicated to LeakCanary for heap analysis.
//            // You should not init your app in this process.
//            return;
//        }
        //LeakCanary.install(this);
        dbm = new DataBaseManager();
    }

    public static DataBaseManager getDataBaseManager() {
        return dbm;
    }
//    void startAsyncTask() {
    //
    //        // This async task is an anonymous class and therefore has a hidden reference to the outer
    //        // class MainActivity. If the activity gets destroyed before the task finishes (e.g. rotation),
    //        // the activity instance will leak.
    //        new AsyncTask<Void, Void, Void>() {
    //            @Override
    //            protected Void doInBackground(Void... params) {
    //                // Do some slow work in background
    //                SystemClock.sleep(200000);
    //                return null;
    //            }
    //        }.execute();
    //        Toast.makeText(this, "请关闭这个A完成泄露", Toast.LENGTH_SHORT).show();
    //    }
}
