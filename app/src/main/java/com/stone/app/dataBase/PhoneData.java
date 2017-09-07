package com.stone.app.dataBase;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

public class PhoneData extends RealmObject {
    @PrimaryKey
    private String phone;

    @Required
    private String memberID;

    public static final int DB_PHONE_NUMBER_LENGTH = 11;

    public String getMemberID() {
        return memberID;
    }

    public String getPhone() {
        return phone;
    }

    void setMemberID(String MemberID) throws DataBaseError {
        Pattern p = Pattern.compile("\\D");
        Matcher m = p.matcher(MemberID);
        if(m.find())
            throw new DataBaseError(DataBaseError.ErrorType.NotStandardID);
        this.memberID = MemberID;
    }

    void setPhone(String Phone) throws DataBaseError {
        Pattern p = Pattern.compile("\\D");
        Matcher m = p.matcher(Phone);
        if(m.find() || DB_PHONE_NUMBER_LENGTH != Phone.length())
            throw new DataBaseError(DataBaseError.ErrorType.NotStandardPhone);
        this.phone = Phone;
    }
}
