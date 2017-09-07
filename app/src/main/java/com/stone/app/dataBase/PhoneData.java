package com.stone.app.dataBase;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

public class PhoneData extends RealmObject {
    @PrimaryKey
    private String phone;

    @Required
    private String memberID;

    public String getMemberID() {
        return memberID;
    }

    public String getPhone() {
        return phone;
    }

    protected void setMemberID(String MemberID) {
        this.memberID = MemberID;
    }

    protected void setPhone(String Phone) {
        this.phone = Phone;
    }
}