package com.stone.app.dataBase;

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

    protected void setMemberID(String MemberID) {
        this.memberID = MemberID;
    }

    protected void setAccount(String Account) {
        this.account = Account;
    }

    protected void setThirdPartyType(int ThirdPartyType) {
        this.thirdPartyType = ThirdPartyType;
    }
}
