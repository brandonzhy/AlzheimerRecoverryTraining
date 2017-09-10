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
        dbm = new DataBaseManager();
    }

    public static DataBaseManager getDataBaseManager() {
        return dbm;
    }
}
