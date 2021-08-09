package edu.pdx.cs410J.heathhan;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.material.timepicker.TimeFormat;

import java.io.BufferedReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;

public class SearchActivity extends AppCompatActivity {


    private TextView setStartDate, setStartTime, setEndDate, setEndTime;
    private String startDate, startTime, endDate, endTime;
    private static final int dateStartId = 0;
    private static final int timeStartId = 1;
    private static final int dateEndId = 2;
    private static final int timeEndId = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        PrettyPrinter pretty;
        BufferedReader reader;
        StringBuilder text = new StringBuilder();
        String line;

        Bundle b = getIntent().getExtras();
        AppointmentBook book = (AppointmentBook) b.get("appointmentBook");
        //File file = getFile(book.getOwnerName());

        //Start Date Search Button
        Button startDateButton = findViewById(R.id.select_start_date);
        setStartDate = findViewById(R.id.set_start_date);
        startDateButton.setOnClickListener(arg0 -> {

            // Show Date dialog
            showDialog(dateStartId);

        });

        //Start Time Search Button
        Button startTimeButton = findViewById(R.id.select_start_time);
        setStartTime = findViewById(R.id.set_start_time);
        startTimeButton.setOnClickListener(arg0 -> {

            // Show time dialog
            showDialog(timeStartId);
        });

        //End Date Search Button
        Button endDateButton = findViewById(R.id.select_end_date);
        setEndDate = findViewById(R.id.set_end_date);
        endDateButton.setOnClickListener(arg0 -> {

            // Show Date dialog
            showDialog(dateEndId);

        });

        //End Time Search Button
        Button endTimeButton = findViewById(R.id.select_end_time);
        setEndTime = findViewById(R.id.set_end_time);
        endTimeButton.setOnClickListener(arg0 -> {

            // Show time dialog
            showDialog(timeEndId);
        });

        Button searchButton = findViewById(R.id.search_button);
        searchButton.setOnClickListener(view -> {
            Date dateStart = convertStringToDate(startDate, startTime);
            //toast(dateStart.toString());
            Date dateEnd = convertStringToDate(endDate, endTime);
            if(dateStart.before(dateEnd)){
                toast("BEFORE");
            }
            else{
                toast("After");
            }
        });

    }

    private void toast(String message) {
        Toast.makeText(SearchActivity.this, message, Toast.LENGTH_LONG).show();
    }

    protected Dialog onCreateDialog(int id) {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int hr = 12;
        int min = 0;


        switch (id) {
            case dateStartId:

                return new DatePickerDialog(SearchActivity.this, date_start_listener, year,
                        month, day);
            case timeStartId:

                return new TimePickerDialog(SearchActivity.this, time_start_listener, hr,
                        min, false);

            case dateEndId:

                return new DatePickerDialog(SearchActivity.this, date_end_listener, year, month, day);

            case timeEndId:

                return new TimePickerDialog(SearchActivity.this, time_end_listener, hr, min, false);

        }
        return null;
    }

    // Date picker dialog
    DatePickerDialog.OnDateSetListener date_start_listener = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int month, int day) {

            startDate = (month + 1) + "/" + day + "/" + year;
            setStartDate.setText(startDate);

        }
    };
    TimePickerDialog.OnTimeSetListener time_start_listener = new TimePickerDialog.OnTimeSetListener() {

        @Override
        public void onTimeSet(TimePicker view, int hr, int min) {
            String marker;

            if(hr == 12){
                marker = "PM";
            } else if(hr > 12){
                hr -= 12;
                marker = "PM";
            } else if(hr == 0){
                hr += 12;
                marker = "AM";
            } else {
                marker = "AM";
            }

            startTime = hr + ":" + min + " " + marker;
            setStartTime.setText(startTime);
        }

    };

    DatePickerDialog.OnDateSetListener date_end_listener = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int month, int day) {
            // store the data in one string and set it to text
            endDate = (month + 1) + "/" + day + "/" + year;
            setEndDate.setText(endDate);

        }
    };
    TimePickerDialog.OnTimeSetListener time_end_listener = new TimePickerDialog.OnTimeSetListener() {

        @Override
        public void onTimeSet(TimePicker view, int hr, int min) {
            String marker;

            if(hr == 12){
                marker = "PM";
            } else if(hr > 12){
                hr -= 12;
                marker = "PM";
            } else if(hr == 0){
                hr += 12;
                marker = "AM";
            } else {
                marker = "AM";
            }

            endTime = hr + ":" + min + " " + marker;
            setEndTime.setText(endTime);
        }

    };






    //*******************************************************************************8



    /**
     * Method to convert Strings to a Date variable
     *
     * @param date - String
     *        String of the date MM/DD/YYYY
     *
     * //@param time - String
     *         String of the time HH:MM
     *
     * //@param marker - String
     *          String of AM or PM
     *
     * @return - Date
     *         New Date variable returned
     *
     */
    private static Date convertStringToDate(String date, String time/*, String marker*/) {
        String str = date + " " + time /*+ " " + marker*/;
        Date d = null;
        try {
            d = new SimpleDateFormat("M/d/yy h:mm a").parse(str); //h:mm a
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return d;
    }





}
