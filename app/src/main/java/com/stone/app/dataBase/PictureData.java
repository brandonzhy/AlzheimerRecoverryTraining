package com.stone.app.dataBase;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

public class PictureData extends RealmObject {
    @PrimaryKey
    private String ID;

    @Required
    private String imagePath;

    private String name;
    private String memberID;
    private String location;
    private long date;
    private String note;
    private String parentImage;
    private boolean activate;

    public String getID(){
        return ID;
    }

    public String getImagePath(){
        return imagePath;
    }

    public String getName(){
        return name;
    }

    public String getMemberID(){
        return memberID;
    }

    public String getLocation(){
        return location;
    }

    public long getDate(){
        return date;
    }

    public String getNote(){
        return note;
    }

    public String getParentImage(){
        return parentImage;
    }

    public boolean getActivate(){
        return activate;
    }

    protected void setID(String ID){
        this.ID = ID;
    }

    public void setImagePath(String ImagePath){
        this.imagePath = ImagePath;
    }

    public void setName(String Name){
        this.name = Name;
    }

    public void setMemberID(String MemberID){
        this.memberID = MemberID;
    }

    public void setLocation(String Location){
        this.location = Location;
    }

    public void setDate(long Date){
        this.date = Date;
    }

    public void setNote(String Note){
        this.note = Note;
    }

    public void setParentImage(String ParentImage){
        this.parentImage = ParentImage;
    }

    protected void setActivate(boolean activate) {
        this.activate = activate;
    }
}
