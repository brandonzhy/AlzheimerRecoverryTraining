package com.stone.app.dataBase;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

public class FamilyData extends RealmObject {
    @PrimaryKey
    private String ID;
    @Required
    private String name;
    private String rootMemberID;
    private boolean activate;

    public String getID(){
        return ID;
    }

    public String getName(){
        return name;
    }

    public boolean getActivate(){
        return activate;
    }

    public String getRootMemberID(){
        return rootMemberID;
    }

    protected void setID(String ID){
        this.ID = ID;
    }

    public void setName(String name){
        this.name = name;
    }

    protected void setRootMemberID(String RootMemberID){
        this.rootMemberID = RootMemberID;
    }

    protected void setActivate(boolean activate) {
        this.activate = activate;
    }
}
