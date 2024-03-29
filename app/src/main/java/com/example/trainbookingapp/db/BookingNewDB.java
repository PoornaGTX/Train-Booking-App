package com.example.trainbookingapp.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.trainbookingapp.model.BookingSQL;

import java.util.ArrayList;
import java.util.List;

public class BookingNewDB extends SQLiteOpenHelper {

    private static final int VERSION = 35; //version
    private static final String DB_NAME = "booking_db"; //database name
    public BookingNewDB(Context context) {
        super(context,DB_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        //creating booking table
        String SQL_CREATE_USER_ENTRIES =
                "CREATE TABLE "+ DBMaster.Book.TABLE_NAME1 + " (" +
                        DBMaster.Book._ID + " INTEGER PRIMARY KEY," +
                        DBMaster.Book.COLUMN_DBID + " TEXT," +
                        DBMaster.Book.COLUMN_NAME_START + " TEXT," +
                        DBMaster.Book.COLUMN_NAME_DESTNATION + " TEXT," +
                        DBMaster.Book.COLUMN_NAME_EMAIL + " TEXT," +
                        DBMaster.Book.COLUMN_NAME_DATE + " TEXT," +
                        DBMaster.Book.COLUMN_NAME_TIME + " TEXT,"+
                        DBMaster.Book.COLUMN_NAME_TRAIN + " TEXT,"+
                        DBMaster.Book.COLUMN_NAME_TICKETPRICE + " TEXT)";
        sqLiteDatabase.execSQL(SQL_CREATE_USER_ENTRIES);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        String DROP_TABLE_QUERY1 = "DROP TABLE IF EXISTS "+ DBMaster.Book.TABLE_NAME1;
        // Drop older table if existed
        sqLiteDatabase.execSQL(DROP_TABLE_QUERY1);

        // Create tables again
        onCreate(sqLiteDatabase);
    }

    //add a new booking
    public boolean insertBooking(BookingSQL booking){
        SQLiteDatabase db= getWritableDatabase();
        ContentValues values= new ContentValues();

        values.put(DBMaster.Book.COLUMN_DBID, booking.getId());
        values.put(DBMaster.Book.COLUMN_NAME_START, booking.getStartingPoint());
        values.put(DBMaster.Book.COLUMN_NAME_DESTNATION, booking.getDestination());
        values.put(DBMaster.Book.COLUMN_NAME_DATE, booking.getDate());
        values.put(DBMaster.Book.COLUMN_NAME_TIME, booking.getTime());
        values.put(DBMaster.Book.COLUMN_NAME_EMAIL, booking.getUserEmail());
        values.put(DBMaster.Book.COLUMN_NAME_TRAIN, booking.getTicketPrice());
        values.put(DBMaster.Book.COLUMN_NAME_TICKETPRICE, booking.getName());

        long newRowId= db.insert(DBMaster.Book.TABLE_NAME1,null,values);
        if (newRowId>=1)
            return true;
        else
            return false;
    }

    // Get all bookings into a list
    public List<BookingSQL> getAllBooking(){

        List<BookingSQL> booking = new ArrayList();
        SQLiteDatabase db = getReadableDatabase();
        String query = "SELECT * FROM "+ DBMaster.Book.TABLE_NAME1;

        Cursor cursor = db.rawQuery(query,null);

        if(cursor.moveToFirst()){
            do {
                // Create new book object
                BookingSQL book = new BookingSQL();

                book.setId(cursor.getString(0));
                book.setdbID(cursor.getInt(1));
                book.setStartingPoint(cursor.getString(2));
                book.setDestination(cursor.getString(3));
                book.setTime(cursor.getString(6));
                book.setDate(cursor.getString(5));
                book.setTimeTwo(cursor.getString(6));
                book.setTicketPrice(cursor.getString(7));
                book.setName(cursor.getString(8));


                booking.add(book);
            }while (cursor.moveToNext());
        }
        return booking;
    }

    // Check if a booking with the given _id exists in the database
    public boolean isBookingExists(String bookingId) {
        SQLiteDatabase db = getReadableDatabase();
        String query = "SELECT " + DBMaster.Book.COLUMN_DBID +
                " FROM " + DBMaster.Book.TABLE_NAME1 +
                " WHERE " + DBMaster.Book.COLUMN_DBID + " = ?";

        Cursor cursor = db.rawQuery(query, new String[]{bookingId});
        boolean exists = (cursor.getCount() > 0);

        cursor.close();
        return exists;
    }

    //delete booking
    public void deleteBooking(String id) {

        SQLiteDatabase db = this.getReadableDatabase();

        db.delete(DBMaster.Book.TABLE_NAME1, DBMaster.Book.COLUMN_DBID + " LIKE ?",new String[]{id});
        db.close();

    }
}
