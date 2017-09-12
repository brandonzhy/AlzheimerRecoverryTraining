//package dataBase;
//
//import java.util.Date;
//
//import io.realm.RealmObject;
//import io.realm.annotations.PrimaryKey;
//import io.realm.annotations.Required;
//
//public class GameRecordData extends RealmObject {
//    @PrimaryKey
//    private String recordID;
//
//    @Required
//    private String memberID;
//
//    private double correctness;
//    private Date date;
//    private int gameType;
//
//    public String getRecordID(){
//        return recordID;
//    }
//
//    public String getMemberID(){
//        return memberID;
//    }
//
//    public double getCorrectness(){
//        return correctness;
//    }
//
//    public Date getDate(){
//        return date;
//    }
//
//    public int getGameType(){
//        return gameType;
//    }
//
//    public void setRecordID(String RecordID){
//        recordID = RecordID;
//    }
//
//    public void setMemberID(String MemberID){
//        memberID = MemberID;
//    }
//
//    public void setCorrectness(double Correctness){
//        correctness = Correctness;
//    }
//
//    public void setDate(Date date){
//        this.date = date;
//    }
//
//    public void setGameType(int GameType){
//        gameType = GameType;
//    }
//}
