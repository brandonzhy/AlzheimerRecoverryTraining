package com.stone.app.Game.GameRecord;

/**
 * Created by Brandon Zhang on 2017/9/11.
 */

class GameItem {
    String data;
    double result;
    int type;

    public GameItem(String data, double result,int type) {
        this.data = data;
        this.result = result;
        this.type = type;
    }

    public void setData(String data) {
        this.data = data;
    }

    public void setResult(double result) {
        this.result = result;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getData() {
        return data;
    }

    public double getResult() {
        return result;
    }
}
