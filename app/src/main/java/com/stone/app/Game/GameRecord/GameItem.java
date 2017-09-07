package com.stone.app.Game.GameRecord;

/**
 * Created by Brandon Zhang on 2017/9/7.
 */

public class GameItem {
    private  String date;
    private  double result;
    public GameItem(String date, double result) {
        this.date = date;
        this.result = result;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setResult(double result) {
        this.result = result;
    }

    public String getDate() {
        return date;
    }

    public double getResult() {
        return result;
    }
}
