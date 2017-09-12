package com.stone.app.dataBase;

import android.util.Log;

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

    public static final long DB_DATE_CHECK_DELTA = 24 * 60 * 60;

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
        long min = Long.parseLong("10000000000000");
        long max = Long.parseLong("99999999999999");
        if (0 != date) {
            if (now < date - DB_DATE_CHECK_DELTA) {
                Log.i("TAG", "Current Time : " + String.valueOf(now));
                Log.i("TAG", "Your Time : " + String.valueOf(date));
                throw new DataBaseError(DataBaseError.ErrorType.AddingFutureDate);
            } else if (date < min || date > max) {
                Log.i("TAG", "Current Time : " + String.valueOf(now));
                Log.i("TAG", "Your Time : " + String.valueOf(date));
                Log.i("TAG", "Legal range is : " + String.valueOf(min) + " to " + String.valueOf(max));
                throw new DataBaseError(DataBaseError.ErrorType.NotStandardDateLength);
            }
        }
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
