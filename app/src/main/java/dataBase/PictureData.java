//package dataBase;
//
//import java.util.Date;
//
//import io.realm.RealmObject;
//import io.realm.annotations.PrimaryKey;
//import io.realm.annotations.Required;
//
//public class PictureData extends RealmObject {
//    @PrimaryKey
//    private String ID;
//
//    @Required
//    private String imagePath;
//
//    private String name;
//    private String memberID;
//    private String location;
//    private Date date;
//    private String note;
//    private String parentImage;
//
//    public String getID(){
//        return ID;
//    }
//
//    public String getImagePath(){
//        return imagePath;
//    }
//
//    public String getName(){
//        return name;
//    }
//
//    public String getMemberID(){
//        return memberID;
//    }
//
//    public String getLocation(){
//        return location;
//    }
//
//    public Date getDate(){
//        return date;
//    }
//
//    public String getNote(){
//        return note;
//    }
//
//    public String getParentImage(){
//        return parentImage;
//    }
//
//    public void setID(String ID){
//        this.ID = ID;
//    }
//
//    public void setImagePath(String ImagePath){
//        this.imagePath = ImagePath;
//    }
//
//    public void setName(String Name){
//        this.name = Name;
//    }
//
//    public void setMemberID(String MemberID){
//        this.memberID = MemberID;
//    }
//
//    public void setLocation(String Location){
//        this.location = Location;
//    }
//
//    public void setDate(Date Date){
//        this.date = Date;
//    }
//
//    public void setNote(String Note){
//        this.note = Note;
//    }
//
//    public void setParentImage(String ParentImage){
//        this.parentImage = ParentImage;
//    }
//}
