package com.stone.app.dataBase;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

public class GameRecordData extends RealmObject {
    @PrimaryKey
    private String recordID;

    @Required
    private String memberID;

    private double correctness;
    private long date;
    private int gameType;
    private boolean activate;

    public String getRecordID(){
        return recordID;
    }

    public String getMemberID(){
        return memberID;
    }

    public double getCorrectness(){
        return correctness;
    }

    public long getDate(){
        return date;
    }

    public int getGameType(){
        return gameType;
    }

    public boolean getActivate(){
        return activate;
    }

    protected void setRecordID(String RecordID){
        recordID = RecordID;
    }

    protected void setMemberID(String MemberID){
        memberID = MemberID;
    }

    public void setCorrectness(double Correctness){
        correctness = Correctness;
    }

    public void setDate(long date){
        this.date = date;
    }

    public void setGameType(int GameType){
        gameType = GameType;
    }

    protected void setActivate(boolean activate) {
        this.activate = activate;
    }
}
