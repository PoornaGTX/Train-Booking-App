package com.example.trainbookingapp.model;

import com.google.gson.annotations.SerializedName;

public class AvailableTimes {
    @SerializedName("time1")
    private String time1;

    @SerializedName("time2")
    private String time2;

    @SerializedName("time3")
    private String time3;

    public String getTime1() {
        return time1;
    }

    public void setTime1(String time1) {
        this.time1 = time1;
    }

    public String getTime2() {
        return time2;
    }

    public void setTime2(String time2) {
        this.time2 = time2;
    }

    public String getTime3() {
        return time3;
    }

    public void setTime3(String time3) {
        this.time3 = time3;
    }
}
