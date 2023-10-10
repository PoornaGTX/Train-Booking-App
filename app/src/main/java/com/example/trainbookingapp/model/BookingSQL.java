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
    private String arrivalTimeToEndStation;

    private String departureTimeFromStartStation;

    private String ticketPrice;
    private String userEmail;

    private  String _id;

    public BookingSQL(){}

    // Constructor
    public BookingSQL(String destination, String startingPoint, String date, String userEmail,String _id,String arrivalTimeToEndStation,String departureTimeFromStartStation) {
        this.destination = destination;
        this.startingPoint = startingPoint;
        this.date = date;
        this.arrivalTimeToEndStation = arrivalTimeToEndStation;
        this.departureTimeFromStartStation = departureTimeFromStartStation;
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
        return arrivalTimeToEndStation;
    }

    public void setTime(String arrivalTimeToEndStation) {
        this.arrivalTimeToEndStation = arrivalTimeToEndStation;
    }

    public String getTimeTwo() {
        return departureTimeFromStartStation;
    }

    public void setTimeTwo(String departureTimeFromStartStation) {
        this.departureTimeFromStartStation = departureTimeFromStartStation;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }
}
