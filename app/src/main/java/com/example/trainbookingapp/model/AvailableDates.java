package com.example.trainbookingapp.model;
import com.google.gson.annotations.SerializedName;


public class AvailableDates {
        @SerializedName("date1")
        private String date1;

        @SerializedName("date2")
        private String date2;

        @SerializedName("date3")
        private String date3;

        public String getDate1() {
            return date1;
        }

        public void setDate1(String date1) {
            this.date1 = date1;
        }

        public String getDate2() {
            return date2;
        }

        public void setDate2(String date2) {
            this.date2 = date2;
        }

        public String getDate3() {
            return date3;
        }

        public void setDate3(String date3) {
            this.date3 = date3;
        }

    }
