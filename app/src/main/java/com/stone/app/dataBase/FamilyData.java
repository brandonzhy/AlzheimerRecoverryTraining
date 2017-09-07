package com.stone.app.dataBase;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class FamilyData extends RealmObject {
    @PrimaryKey
    private String ID;

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

    void setID(String ID) throws DataBaseError {
        Pattern p = Pattern.compile("\\D");
        Matcher m = p.matcher(ID);
        if(m.find())
            throw new DataBaseError(DataBaseError.ErrorType.NotStandardID);
        this.ID = ID;
    }

    void setName(String name) throws DataBaseError {
        Pattern p = Pattern.compile("[^0-9a-zA-Z_.]");
        Matcher m = p.matcher(name);
        if(m.find())
            throw new DataBaseError(DataBaseError.ErrorType.IllegalName_DisapprovedCharacter);
        this.name = name;
    }

    void setRootMemberID(String RootMemberID) throws DataBaseError {
        Pattern p = Pattern.compile("\\D");
        Matcher m = p.matcher(RootMemberID);
        if(m.find())
            throw new DataBaseError(DataBaseError.ErrorType.NotStandardID);
        this.rootMemberID = RootMemberID;
    }

    void setActivate(boolean activate) {
        this.activate = activate;
    }
}
