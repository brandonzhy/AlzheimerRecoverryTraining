package com.stone.app.dataBase;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import io.realm.RealmObject;
import io.realm.annotations.Required;

public class ThirdPartyAccountData extends RealmObject {
    @Required
    private String memberID;

    private String account;
    private int thirdPartyType;

    public String getMemberID() {
        return memberID;
    }

    public String getAccount() {
        return account;
    }

    public int getThirdPartyType() {
        return thirdPartyType;
    }

    void setMemberID(String MemberID) throws DataBaseError {
        Pattern p = Pattern.compile("\\D");
        Matcher m = p.matcher(MemberID);
        if (m.find())
            throw new DataBaseError(DataBaseError.ErrorType.NotStandardID);
        this.memberID = MemberID;
    }

    void setAccount(String Account) {
        this.account = Account;
    }

    void setThirdPartyType(int ThirdPartyType) throws DataBaseError {
        if (ThirdPartyType < 0)
            throw new DataBaseError(DataBaseError.ErrorType.NotStandardType);
        this.thirdPartyType = ThirdPartyType;
    }
}
