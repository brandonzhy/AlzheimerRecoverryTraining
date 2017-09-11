package com.stone.app.dataBase;

import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

public class GameRecordData extends RealmObject {
    @PrimaryKey
    private String recordID;

    @Required
    private String memberID;

    public static final long DB_DATE_CHECK_DELTA = 1;

    private double factor; // 指通关时间/答题准确率
    private long date;
    private int gameType;
    private boolean activate;

    public String getRecordID() {
        return recordID;
    }

    public String getMemberID() {
        return memberID;
    }

    public double getFactor() {
        return factor;
    }

    public long getDate() {
        return date;
    }

    public int getGameType() {
        return gameType;
    }

    public boolean getActivate() {
        return activate;
    }

    void checkRecordID() throws DataBaseError {
        Pattern p = Pattern.compile("\\D");
        Matcher m = p.matcher(this.recordID);
        if (m.find())
            throw new DataBaseError(DataBaseError.ErrorType.NotStandardID);
        if (this.recordID.equals(""))
            this.recordID = "NULL" + String.valueOf(new Date());
    }

    void setMemberID(String MemberID) throws DataBaseError {
        Pattern p = Pattern.compile("\\D");
        Matcher m = p.matcher(MemberID);
        if (m.find())
            throw new DataBaseError(DataBaseError.ErrorType.NotStandardID);
        memberID = MemberID;
    }

    void setFactor(double Factor) throws DataBaseError {
        if (Factor < 0)
            throw new DataBaseError(DataBaseError.ErrorType.NotStandardFactor);
        factor = Factor;
    }

    void setDate(long date, long now) throws DataBaseError {
        if (now < date + DB_DATE_CHECK_DELTA)
            throw new DataBaseError(DataBaseError.ErrorType.AddingFutureDate);
        this.date = date;
    }

    void setGameType(int GameType) throws DataBaseError {
        if (GameType < 0)
            throw new DataBaseError(DataBaseError.ErrorType.NotStandardType);
        gameType = GameType;
    }

    void setActivate(boolean activate) {
        this.activate = activate;
    }
}
