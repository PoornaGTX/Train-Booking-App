package com.example.trainbookingapp.model;
import androidx.room.Entity;
import androidx.room.PrimaryKey;


@Entity(tableName = "bookings")
public class BookingSQL {

    @PrimaryKey(autoGenerate = true)
    private long dbID; //auto-generated id
    private String destination;
    private String startingPoint;
    private String date;
    private String arrivalTimeToEndStation;

    private String departureTimeFromStartStation;

    private String ticketPrice;
    private String userEmail;

    private  String id;

    public BookingSQL(){}

    // Constructor
    public BookingSQL(String destination, String startingPoint, String date, String arrivalTimeToEndStation,String departureTimeFromStartStation,String userEmail,String id) {
        this.destination = destination;
        this.startingPoint = startingPoint;
        this.date = date;
        this.arrivalTimeToEndStation = arrivalTimeToEndStation;
        this.departureTimeFromStartStation = departureTimeFromStartStation;
        this.userEmail = userEmail;
        this.id = id;
    }
    public long getdbID() {
        return dbID;
    }

    public void setdbID(long dbID) {
        this.dbID = dbID;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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
