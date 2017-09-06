//package dataBase;
//
//import io.realm.RealmObject;
//import io.realm.annotations.PrimaryKey;
//import io.realm.annotations.Required;
//
//public class MemberData extends RealmObject {
//    @PrimaryKey
//    private String ID;
//
//    @Required
//    private String nickname;
//    @Required
//    private String password;
//    @Required
//    private String familyID;
//
//    private String name;
//    private Boolean gender;
//
//    public String getID(){
//        return ID;
//    }
//
//    public String getNickName(){
//        return nickname;
//    }
//
//    public String getPassword(){
//        return password;
//    }
//
//    public String getFamilyID(){
//        return familyID;
//    }
//
//    public String getName(){
//        return name;
//    }
//
//    public Boolean getGender(){
//        return gender;
//    }
//
//    public void setID(String ID){
//        this.ID = ID;
//    }
//
//    public void setNickName(String NickName){
//        this.nickname = NickName;
//    }
//
//    public void setPassword(String Password){
//        this.password = Password;
//    }
//
//    public void setFamilyID(String FamilyID){
//        this.familyID = FamilyID;
//    }
//
//    public void setName(String name){
//        this.name = name;
//    }
//
//    public void setGender(Boolean Gender){
//        this.gender = Gender;
//    }
//}
