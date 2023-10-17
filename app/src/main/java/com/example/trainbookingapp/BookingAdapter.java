package com.example.trainbookingapp;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.icu.text.SimpleDateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.core.net.ParseException;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.trainbookingapp.db.BookingNewDB;
import com.example.trainbookingapp.model.Booking;
import com.example.trainbookingapp.network.BookingApiClient;
import com.example.trainbookingapp.utility.NetworkUtils;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BookingAdapter extends RecyclerView.Adapter<BookingAdapter.BookingViewHolder> {

    private List<Booking> bookings;
    private OnItemClickListener onItemClickListener;
    private Fragment fragment;
    private FragmentManager childFragmentManager;
    private  BookingNewDB bookingNewDB;

    public BookingAdapter(List<Booking> bookings) {
        this.bookings = bookings;
    }
    private Context context;

    public interface OnBookingDeletedListener {
        void onBookingDeleted();
    }

    private OnBookingDeletedListener onBookingDeletedListener;

    public void setOnBookingDeletedListener(OnBookingDeletedListener listener) {
        this.onBookingDeletedListener = listener;
    }

    public BookingAdapter(List<Booking> bookings, Context context,OnBookingDeletedListener listener) {
        this.bookings = bookings;
        this.context = context;
        this.onBookingDeletedListener = listener;

        // Initialize the bookingNewDB object
        bookingNewDB = new BookingNewDB(context);

    }

    public void onEditButtonClick(int position) {
        if (onItemClickListener != null) {
            onItemClickListener.onEditClick(bookings.get(position));
        }
    }

    @NonNull
    @Override
    public BookingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_booking, parent, false);
        return new BookingViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BookingViewHolder holder, int position) {
        Booking booking = bookings.get(position);

        // Bind data to TextViews
        holder.destinationTextView.setText("Destination : " + booking.getDestination());
        holder.startingTextView.setText("From: " + booking.getStartingPoint());
        holder.dateTextView.setText("Date: " + booking.getDate());
        holder.timeTextView.setText("Time: " + booking.getTime());

        // If the network connection not available
        if (!NetworkUtils.isNetworkConnected(context)) {
            holder.closeButton.setEnabled(false);
            holder.editButton.setEnabled(false);

            int disabledButtonColor = ContextCompat.getColor(context, R.color.disabled_button_color);
            holder.closeButton.setBackgroundColor(disabledButtonColor);
            holder.editButton.setBackgroundColor(disabledButtonColor);
        }


        // Set click listeners for buttons
        // booking cancel button
        holder.closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //get the current date
                Calendar currentDate = Calendar.getInstance();
                currentDate.set(Calendar.HOUR_OF_DAY, 0);
                currentDate.set(Calendar.MINUTE, 0);
                currentDate.set(Calendar.SECOND, 0);
                currentDate.set(Calendar.MILLISECOND, 0);

                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

                try {
                    Date bookingDate = sdf.parse(booking.getDate());
                    Calendar bookingDateCalendar = Calendar.getInstance();
                    bookingDateCalendar.setTime(bookingDate);

                    // Set the time components of the booking date to midnight
                    bookingDateCalendar.set(Calendar.HOUR_OF_DAY, 0);
                    bookingDateCalendar.set(Calendar.MINUTE, 0);
                    bookingDateCalendar.set(Calendar.SECOND, 0);
                    bookingDateCalendar.set(Calendar.MILLISECOND, 0);

                    long timeDifferenceInMillis = bookingDateCalendar.getTimeInMillis() - currentDate.getTimeInMillis();
                    long daysDifference = TimeUnit.MILLISECONDS.toDays(timeDifferenceInMillis);

                    // check the booking cancel condition
                    if (daysDifference >= 0) {
                        showDeleteConfirmationDialog(booking);
                        //delete booking from local db
                        bookingNewDB.deleteBooking(booking.getScheduleID());

                    } else {
                        showDeleteStatusDialog(false, "Booking can only be canceled with a minimum of 3 days before the booked date.");
                    }
                } catch (ParseException | java.text.ParseException e) {
                    e.printStackTrace();
                }
            }
        });

        //handler for edit book item
        holder.editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    Booking booking = bookings.get(position);

                    // Calculate the difference in days between the booking date and the current date
                    Calendar currentDate = Calendar.getInstance();
                    currentDate.set(Calendar.HOUR_OF_DAY, 0);
                    currentDate.set(Calendar.MINUTE, 0);
                    currentDate.set(Calendar.SECOND, 0);
                    currentDate.set(Calendar.MILLISECOND, 0);

                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

                    try {
                        Date bookingDate = sdf.parse(booking.getDate());
                        Calendar bookingDateCalendar = Calendar.getInstance();
                        bookingDateCalendar.setTime(bookingDate);

                        // Set the time components of the booking date to midnight
                        bookingDateCalendar.set(Calendar.HOUR_OF_DAY, 0);
                        bookingDateCalendar.set(Calendar.MINUTE, 0);
                        bookingDateCalendar.set(Calendar.SECOND, 0);
                        bookingDateCalendar.set(Calendar.MILLISECOND, 0);

                        long timeDifferenceInMillis = bookingDateCalendar.getTimeInMillis() - currentDate.getTimeInMillis();
                        long daysDifference = TimeUnit.MILLISECONDS.toDays(timeDifferenceInMillis);

                        //check the booking update condition
                        if (daysDifference >= 5) {

                            // Call the listener's method to handle the edit button click
                            if (onItemClickListener != null) {
                                onItemClickListener.onEditClick(booking);
                            }
                            //show edit dialog
                            showEditDialog(booking);
                        } else {
                            // provide error alert for user if the booking cannot be edited
                            showEditStatusDialog(false, "Booking edits must be made at least 5 days in advance.");
                        }
                    } catch (ParseException | java.text.ParseException e) {
                        e.printStackTrace();
                    }
                }
            }

            ;
        });
    }

    //booking cancel confirmation alert
    private void showDeleteConfirmationDialog(Booking booking) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Confirm Deletion");
        builder.setMessage("Are you sure you want to cancel this booking?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // if user confirmed the deletion, call the API to delete the booking
                deleteBooking(booking);
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void showEditStatusDialog(boolean isSuccess, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        // Set the dialog's title based on the edit status
        builder.setTitle(isSuccess ? "Success" : "Error");
        builder.setMessage(message);

        // handler for positive button
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    //delete booking function
    private void deleteBooking(Booking booking) {
        BookingApiClient bookingApiClient = new BookingApiClient();
        bookingApiClient.deleteBooking(booking.getId(), new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(context, "Booking canceled successfully.", Toast.LENGTH_SHORT).show();
                    // Notify the fragment that a booking is deleted
                    if (onBookingDeletedListener != null) {
                        onBookingDeletedListener.onBookingDeleted();
                    }
                } else {
                    Toast.makeText(context, "Failed to cancel booking.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(context, "Failed to cancel booking.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showDeleteStatusDialog(boolean isSuccess,String msg) {
        String message = msg;
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(isSuccess ? "Success" : "Error");
        builder.setMessage(message);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    //edit dialog
    private void showEditDialog(Booking booking) {
        if (onItemClickListener != null && fragment != null) {
            // Create and show the dialog fragment using getSupportFragmentManager
            BookingEditDialogFragment dialogFragment = BookingEditDialogFragment.newInstance(booking);
            dialogFragment.show(fragment.requireActivity().getSupportFragmentManager(), "edit_dialog");
        }
    }

    @Override
    public int getItemCount() {
        return bookings.size();
    }

    public class BookingViewHolder extends RecyclerView.ViewHolder {
        // Views for displaying booking information
        TextView destinationTextView;
        TextView startingTextView;
        TextView timeTextView;
        TextView dateTextView;
        Button editButton;
        Button closeButton;

        public BookingViewHolder(View itemView) {
            super(itemView);
            // Initialize the views
            destinationTextView = itemView.findViewById(R.id.destinationTextView1);
            startingTextView = itemView.findViewById(R.id.startingPointTextView1);
            dateTextView = itemView.findViewById(R.id.dateTextView1);
            timeTextView = itemView.findViewById(R.id.timeTextView1);
            editButton = itemView.findViewById(R.id.editButton);
            closeButton = itemView.findViewById(R.id.closeButton);
        }
    }

    // Listener for handling item click events
    public interface OnItemClickListener {
        void onEditClick(Booking booking);
        void onCloseClick(Booking booking);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }

    // Set the list of bookings
    public void setBookings(List<Booking> bookings) {
        this.bookings = bookings;
        notifyDataSetChanged();
    }
}
