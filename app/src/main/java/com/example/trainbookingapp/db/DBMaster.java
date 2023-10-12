package com.example.trainbookingapp.db;

import android.provider.BaseColumns;

public class DBMaster {

    private DBMaster(){}

    //Book class to store data to local db
    public static class Book implements BaseColumns {

        public static final String TABLE_NAME1="Booking";
        public static final String COLUMN_DBID="dbID";
        public static final String COLUMN_NAME_START="startingPoint";
        public static final String COLUMN_NAME_DESTNATION="destination";
        public static final String COLUMN_NAME_DATE="date";
        public static final String COLUMN_NAME_TIME="time";
        public static final String COLUMN_NAME_EMAIL="userEmail";

    }
}
