package com.stone.app.dataBase;

import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

    public static final int DB_IMG_NOTE_MAX_LENGTH = 50;
    public static final long DB_DATE_CHECK_DELTA = 1;

    public String getID() {
        return ID;
    }

    public String getImagePath() {
        return imagePath;
    }

    public String getName() {
        return name;
    }

    public String getMemberID() {
        return memberID;
    }

    public String getLocation() {
        return location;
    }

    public long getDate() {
        return date;
    }

    public String getNote() {
        return note;
    }

    public String getParentImage() {
        return parentImage;
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

    void setImagePath(String ImagePath) {
        this.imagePath = ImagePath;
    }

    void setName(String Name) throws DataBaseError {
        Pattern p = Pattern.compile("[^0-9a-zA-Z_.\\u4E00-\\u9FA5]");
        Matcher m = p.matcher(Name);
        if (m.find())
            throw new DataBaseError(DataBaseError.ErrorType.IllegalName_DisapprovedCharacter);
        this.name = Name;
    }

    void setMemberID(String MemberID) throws DataBaseError {
        Pattern p = Pattern.compile("\\D");
        Matcher m = p.matcher(MemberID);
        if (m.find())
            throw new DataBaseError(DataBaseError.ErrorType.NotStandardID);
        this.memberID = MemberID;
    }

    void setLocation(String Location) {
        this.location = Location;
    }

    void setDate(long Date, long Now) throws DataBaseError {
        if (Now < Date - DB_DATE_CHECK_DELTA)
            throw new DataBaseError(DataBaseError.ErrorType.AddingFutureDate);
        this.date = Date;
    }

    void setNote(String Note) throws DataBaseError {
        if (Note.length() > DB_IMG_NOTE_MAX_LENGTH)
            throw new DataBaseError(DataBaseError.ErrorType.NoteTooLong);
        this.note = Note;
    }

    void setParentImage(String ParentImage) throws DataBaseError {
        Pattern p = Pattern.compile("\\D");
        Matcher m = p.matcher(ParentImage);
        if (m.find())
            throw new DataBaseError(DataBaseError.ErrorType.NotStandardID);
        this.parentImage = ParentImage;
    }

    void setActivate(boolean activate) {
        this.activate = activate;
    }
}
