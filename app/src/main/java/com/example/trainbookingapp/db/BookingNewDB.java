package com.example.trainbookingapp.db;

import static com.example.trainbookingapp.db.DBMaster.Book.TABLE_NAME1;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.trainbookingapp.model.BookingSQL;

import java.util.ArrayList;
import java.util.List;

public class BookingNewDB extends SQLiteOpenHelper {

    private static final int VERSION = 10; //version
    private static final String DB_NAME = "booking_db"; //database name
    public BookingNewDB(Context context) {
        super(context,DB_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        //creating booking table
        String SQL_CREATE_USER_ENTRIES =
                "CREATE TABLE "+ TABLE_NAME1 + " (" +
                        DBMaster.Book._ID + " INTEGER PRIMARY KEY," +
                        DBMaster.Book.COLUMN_DBID + " TEXT," +
                        DBMaster.Book.COLUMN_NAME_START + " TEXT," +
                        DBMaster.Book.COLUMN_NAME_DESTNATION + " TEXT," +
                        DBMaster.Book.COLUMN_NAME_EMAIL + " TEXT," +
                        DBMaster.Book.COLUMN_NAME_DATE + " TEXT," +
                        DBMaster.Book.COLUMN_NAME_TIME + " TEXT)";
        sqLiteDatabase.execSQL(SQL_CREATE_USER_ENTRIES);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        String DROP_TABLE_QUERY1 = "DROP TABLE IF EXISTS "+ TABLE_NAME1;
        // Drop older table if existed
        sqLiteDatabase.execSQL(DROP_TABLE_QUERY1);

        // Create tables again
        onCreate(sqLiteDatabase);
    }

    //add a new booking
    public boolean insertBooking(BookingSQL booking){
        SQLiteDatabase db= getWritableDatabase();
        ContentValues values= new ContentValues();
        values.put(DBMaster.Book.COLUMN_DBID,booking.get_id());
        values.put(DBMaster.Book.COLUMN_NAME_START,booking.getStartingPoint());
        values.put(DBMaster.Book.COLUMN_NAME_DESTNATION,booking.getDestination());
        values.put(DBMaster.Book.COLUMN_NAME_DATE,booking.getDate());
        values.put(DBMaster.Book.COLUMN_NAME_TIME,booking.getTime());
        values.put(DBMaster.Book.COLUMN_NAME_EMAIL,booking.getUserEmail());

        long newRowId= db.insert(TABLE_NAME1,null,values);
        if (newRowId>=1)
            return true;
        else
            return false;
    }

    // Get all bookings into a list
    public List<BookingSQL> getAllBooking(){

        List<BookingSQL> booking = new ArrayList();
        SQLiteDatabase db = getReadableDatabase();
        String query = "SELECT * FROM "+ TABLE_NAME1;

        Cursor cursor = db.rawQuery(query,null);

        if(cursor.moveToFirst()){
            do {
                // Create new book object
                BookingSQL book = new BookingSQL();

                book.setId(cursor.getInt(0));
                book.set_id(cursor.getString(1));
                book.setStartingPoint(cursor.getString(2));
                book.setDestination(cursor.getString(3));
                book.setUserEmail(cursor.getString(4));
                book.setTime(cursor.getString(5));
                book.setDate(cursor.getString(6));

                booking.add(book);
            }while (cursor.moveToNext());
        }
        return booking;
    }

    // Check if a booking with the given _id exists in the database
    public boolean isBookingExists(String bookingId) {
        SQLiteDatabase db = getReadableDatabase();
        String query = "SELECT " + DBMaster.Book._ID +
                " FROM " + TABLE_NAME1 +
                " WHERE " + DBMaster.Book.COLUMN_DBID + " = ?";

        Cursor cursor = db.rawQuery(query, new String[]{bookingId});
        boolean exists = (cursor.getCount() > 0);

        cursor.close();
        return exists;
    }
}
