package com.stone.app.dataBase;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.Date;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class FamilyData extends RealmObject {
    @PrimaryKey
    private String ID;

    private String name;
    private String rootMemberID;
    private String portraitID;
    private boolean activate;

    public String getID() {
        return ID;
    }

    public String getName() {
        return name;
    }

    public boolean getActivate() {
        return activate;
    }

    public String getRootMemberID() {
        return rootMemberID;
    }

    public String getPortraitID() {
        return portraitID;
    }

    void checkID() throws DataBaseError {
        Pattern p = Pattern.compile("\\D");
        Matcher m = p.matcher(this.ID);
        if (m.find())
            throw new DataBaseError(DataBaseError.ErrorType.NotStandardID);
        if (this.ID.equals(""))
            this.ID = "NULL" + String.valueOf(new Date());
    }

    void setName(String name) throws DataBaseError {
        Pattern p = Pattern.compile("[^0-9a-zA-Z_.\\u4E00-\\u9FA5]");
        Matcher m = p.matcher(name);
        if (m.find())
            throw new DataBaseError(DataBaseError.ErrorType.IllegalName_DisapprovedCharacter);
        this.name = name;
    }

    void setRootMemberID(String RootMemberID) throws DataBaseError {
        Pattern p = Pattern.compile("\\D");
        Matcher m = p.matcher(RootMemberID);
        if (m.find())
            throw new DataBaseError(DataBaseError.ErrorType.NotStandardID);
        this.rootMemberID = RootMemberID;
    }

    void setPortraitID(String PortraitID) throws DataBaseError {
        Pattern p = Pattern.compile("\\D");
        Matcher m = p.matcher(PortraitID);
        if (m.find())
            throw new DataBaseError(DataBaseError.ErrorType.NotStandardID);
        this.portraitID = PortraitID;
    }

    void setActivate(boolean activate) {
        this.activate = activate;
    }
}
