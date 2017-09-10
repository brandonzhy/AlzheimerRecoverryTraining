package com.stone.app.addMember;

/**
 * Created by Brandon Zhang on 2017/9/10.
 */

public class familyItem {
    private  String familyName="";
    private  String familyID="";
    private  String familyCreaterName="";
    private  String familyCreaterID="";
    private  String imagePath="";

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setFamilyName(String familyName) {
        this.familyName = familyName;
    }

    public void setFamilyID(String familyID) {
        this.familyID = familyID;
    }

    public void setFamilyCreaterName(String familyCreaterName) {
        this.familyCreaterName = familyCreaterName;
    }

    public void setFamilyCreaterID(String familyCreaterID) {
        this.familyCreaterID = familyCreaterID;
    }

    public String getFamilyName() {
        return familyName;
    }

    public String getFamilyID() {
        return familyID;
    }

    public String getFamilyCreaterName() {
        return familyCreaterName;
    }

    public String getFamilyCreaterID() {
        return familyCreaterID;
    }
}
