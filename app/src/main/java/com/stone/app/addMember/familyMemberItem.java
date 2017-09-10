package com.stone.app.addMember;

/**
 * Created by Brandon Zhang on 2017/9/10.
 */

public class familyMemberItem {
    private  String memberName="";
    private  String memberID="";
    private  String imagePath="";

    public String getMemberName() {
        return memberName;
    }

    public String getMemberID() {
        return memberID;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }

    public void setMemberID(String memberID) {
        this.memberID = memberID;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }
}
