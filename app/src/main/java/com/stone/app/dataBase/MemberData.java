package com.stone.app.dataBase;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

public class MemberData extends RealmObject {
    @PrimaryKey
    private String ID;

    @Required
    private String nickname;
    @Required
    private String password;
    @Required
    private String familyID;

    private String name;
    private int gender;
    private boolean activate;

    public String getID(){
        return ID;
    }

    public String getNickName(){
        return nickname;
    }

    public String getPassword(){
        return password;
    }

    public String getFamilyID(){
        return familyID;
    }

    public String getName(){
        return name;
    }

    public int getGender(){
        return gender;
    }

    public boolean getActivate(){
        return activate;
    }

    protected void setID(String ID){
        this.ID = ID;
    }

    public void setNickName(String NickName){
        this.nickname = NickName;
    }

    public void setPassword(String Password){
        this.password = Password;
    }

    protected void setFamilyID(String FamilyID){
        this.familyID = FamilyID;
    }

    public void setName(String Name){
        this.name = Name;
    }

    public void setGender(int Gender){
        this.gender = Gender;
    }

    protected void setActivate(boolean activate) {
        this.activate = activate;
    }
}
