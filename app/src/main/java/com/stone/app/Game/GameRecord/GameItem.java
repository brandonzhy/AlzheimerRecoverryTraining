package com.stone.app.Game.GameRecord;

/**
 * Created by Brandon Zhang on 2017/9/11.
 */

class GameItem {
    String data;
    double result;

    public GameItem(String data, double result) {
        this.data = data;
        this.result = result;
    }

    public void setData(String data) {
        this.data = data;
    }

    public void setResult(double result) {
        this.result = result;
    }



    public String getData() {
        return data;
    }

    public double getResult() {
        return result;
    }
}
