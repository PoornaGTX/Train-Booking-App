package com.example.trainbookingapp.model;
import androidx.room.Entity;
import androidx.room.PrimaryKey;


@Entity(tableName = "bookings")
public class BookingSQL {

    @PrimaryKey(autoGenerate = true)
    private long id; //auto-generated id
    private String destination;
    private String startingPoint;
    private String date;
    private String time;
    private String userEmail;

    private  String _id;

    public BookingSQL(){}

    // Constructor
    public BookingSQL(String destination, String startingPoint, String date, String time, String userEmail,String _id) {
        this.destination = destination;
        this.startingPoint = startingPoint;
        this.date = date;
        this.time = time;
        this.userEmail = userEmail;
        this._id = _id;
    }


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = String.valueOf(_id);
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getStartingPoint() {
        return startingPoint;
    }

    public void setStartingPoint(String startingPoint) {
        this.startingPoint = startingPoint;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }
}
