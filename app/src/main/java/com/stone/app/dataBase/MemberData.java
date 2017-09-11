package com.stone.app.dataBase;

import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

public class MemberData extends RealmObject {
    @PrimaryKey
    private String ID;

    @Required
    private String password;

    private String familyID;
    private String name;
    private String nickname;
    private int gender;
    private String portraitID;
    private boolean activate;

    public static final int DB_GENDER_AMOUNT = 7;
    public static final int DB_GENDER_UNKNOWN = 0;
    public static final int DB_GENDER_MALE = 1;
    public static final int DB_GENDER_FEMALE = 2;
    public static final int DB_GENDER_TRANS = 3;
    public static final int DB_GENDER_BISEXUAL = 4;
    public static final int DB_GENDER_ASEXUAL = 5;
    public static final int DB_GENDER_SPECIAL = 6;

    public String getID() {
        return ID;
    }

    public String getNickName() {
        return nickname;
    }

    public String getPassword() {
        return password;
    }

    public String getFamilyID() {
        return familyID;
    }

    public String getName() {
        return name;
    }

    public int getGender() {
        return gender;
    }

    public String getPortraitID() {
        return portraitID;
    }

    public boolean getActivate() {
        return activate;
    }

    void checkID() throws DataBaseError {
        Pattern p = Pattern.compile("\\D");
        Matcher m = p.matcher(this.ID);
        if (m.find())
            throw new DataBaseError(DataBaseError.ErrorType.NotStandardID);
        if (this.ID.equals(""))
            this.ID = "NULL" + String.valueOf(new Date());
    }

    void setNickName(String NickName) throws DataBaseError {
        Pattern p = Pattern.compile("[^0-9a-zA-Z_.\\u4E00-\\u9FA5]");
        Matcher m = p.matcher(NickName);
        if (m.find())
            throw new DataBaseError(DataBaseError.ErrorType.IllegalName_DisapprovedCharacter);
        this.nickname = NickName;
    }

    void setPassword(String Password) {
        this.password = Password;
    }

    void setFamilyID(String FamilyID) throws DataBaseError {
        Pattern p = Pattern.compile("\\D");
        Matcher m = p.matcher(FamilyID);
        if (m.find())
            throw new DataBaseError(DataBaseError.ErrorType.NotStandardID);
        this.familyID = FamilyID;
    }

    void setName(String Name) throws DataBaseError {
        Pattern p = Pattern.compile("\\d");
        Matcher m = p.matcher(Name);
        if (m.find())
            throw new DataBaseError(DataBaseError.ErrorType.IllegalName_DigitExistInRealName);
        p = Pattern.compile("[^a-zA-Z_.\\u4E00-\\u9FA5]");
        m = p.matcher(Name);
        if (m.find())
            throw new DataBaseError(DataBaseError.ErrorType.IllegalName_DisapprovedCharacter);
        p = Pattern.compile("[a-zA-Z]");
        m = p.matcher(Name);
        if (m.find()) {
            p = Pattern.compile("[^a-zA-Z_.]");
            m = p.matcher(Name);
            if (m.find())
                throw new DataBaseError(DataBaseError.ErrorType.IllegalName_ChineseMingleWithEnglish);
        }
        this.name = Name;
    }

    void setGender(int Gender) throws DataBaseError {
        if (Gender >= 0 && Gender < DB_GENDER_AMOUNT)
            this.gender = Gender;
        else
            throw new DataBaseError(DataBaseError.ErrorType.UnspecifiedGender);
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
